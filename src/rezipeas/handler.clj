(ns rezipeas.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [rezipeas.sql :refer :all]
            [rezipeas.pages :refer :all]
            [rezipeas.sanitize :refer :all]
            [ring.util.response :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(db-setup)

(defn save-new-ingredients [ingredients]
  """Inserts or ignores ingredients into database."""
  (doseq [ing ingredients]
      (insert-ingredient db {:name ing})))

(defn save-new-tags [tags]
  """Inserts or ignores tags into database."""
  (doseq [tag tags]
    (insert-tag db {:name tag})))

(defn save-rec-ing-relations [rec_id ingredients quantities units portions]
  """Inserts or ignores recipe-ingredient relation into database.
     Needs recipe id."""
  (doseq [row (map vector ingredients quantities units)]
    (let [ing_id (:id (first (get-ing-id db {:name (first row)})))
          quantity (/ (second row) portions)
          unit (last row)]
      (insert-rec-ing
       db
       {:rec_id rec_id, :ing_id ing_id
        ,:quantity quantity, :unit unit}))))

(defn save-tag-rec-relations [rec_id tags]
  """Inserts or ignors recipe-tag relation into database.
     Needs recipe id from database."""
  (doseq [tag tags]
    (let [tag_id (:id (first (get-tag-id db {:name tag})))]
      (insert-tag-rec db {:tag_id tag_id, :rec_id rec_id}))))


(defn save-new-recipe [params]
  """Commits a new recipe to the database."""
  (let [name (:name params)
        ingredients (sanitize-ingredients (:ingredient params))
        quantities (sanitize-quantities (:quantity params))
        units (sanitize-units (:unit params))
        intro (:intro params)
        tip (:tip params)
        description (:description params)
        portions (sanitize-portions (:portions params))
        tags (sanitize-tags (:tag params))]
    (insert-recipe db {:name name, :intro intro, :description description, :tip tip})
    (save-new-ingredients ingredients)
    (save-new-tags tags)
    (let [rec_id (:id (first (get-rec-id db {:name name})))]
      (save-rec-ing-relations rec_id ingredients quantities units portions)
      (save-tag-rec-relations rec_id tags))
    (redirect "/recipies")))
      
(defroutes app-routes
  (GET "/" [] (redirect "/recipies"))
  (GET "/recipies" [] (show-all-recipies))
  (GET "/recipies/new" [] (new-recipe))
  (POST "/recipies/new" req (save-new-recipe (:params req)))
  (route/not-found "Not Found"))

(def app
  ;; enable anti-forgery later
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
