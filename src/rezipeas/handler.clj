(ns rezipeas.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.java.io :as io]
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
        tip (let [tip (:tip params)] (if tip tip ""))
        description (:description params)
        portions (sanitize-portions (:portions params))
        tags (sanitize-tags (:tag params))
        file? (not (empty? (get-in params [:picture :filename])))
        tempfile (get-in params [:picture :tempfile])
        filename (if file? 
                   (str name (get-file-extension (get-in params [:picture :filename])))
                   nil)]
    (when file?
      (io/copy tempfile (io/file (str rootpath "img" (java.io.File/separator) filename))))
    (insert-recipe db {:name name, :intro intro, :description description, :tip tip :portions portions :image_url filename})
    (save-new-ingredients ingredients)
    (save-new-tags tags)
    (let [rec_id (:id (first (get-rec-id db {:name name})))]
      ;; need to cast portions to double, because hugsql does not convert ratios to reals
      (save-rec-ing-relations rec_id ingredients quantities units (double portions))
      (save-tag-rec-relations rec_id tags)
      (redirect (str "/recipies/" rec_id)))))

(defn save-edit-recipe [id params]
  """Updates database entries for recipe with given id and params."""
  (let [name (:name params)
        ingredients (sanitize-ingredients (:ingredient params))
        quantities (sanitize-quantities (:quantity params))
        units (sanitize-units (:unit params))
        intro (:intro params)
        tip (let [tip (:tip params)] (if tip tip ""))
        description (:description params)
        portions (sanitize-portions (:portions params))
        tags (sanitize-tags (:tag params))
        file? (not (empty? (get-in params [:picture :filename])))
        tempfile (get-in params [:picture :tempfile])
        filename (if file? 
                   (str name (get-file-extension (get-in params [:picture :filename])))
                   nil)]
    (when file?
      (io/copy tempfile
               (io/file
                (str
                 rootpath
                 "img"
                 (java.io.File/separator)
                 filename))))
    (change-recipe db (assoc params :id id :image_url filename))
    (redirect (str "/recipies/" id))))
      
(defroutes app-routes
  (GET "/" [] (redirect "/recipies/new"))
  (GET "/search" [term tags] (show-search-result term tags))
  (GET "/recipies" [] (show-all-recipies))
  (GET "/recipies/new" [] (new-recipe))
  (GET "/recipies/:id" [id] (show-recipe id))
  (GET "/recipies/edit/:id" [id] (edit-recipe-page id))
  (POST "/recipies/edit/:id" [id & params] (save-edit-recipe id params))
  (POST "/recipies/new" req (save-new-recipe (:params req)))
  (route/files "/img/" {:root (str rootpath "img" (java.io.File/separator))})
  (route/resources "/assets/")
  (route/not-found "Not Found"))

(def app
  ;; enable anti-forgery later
  (wrap-defaults app-routes
                 (-> site-defaults
                     (assoc-in [:security :anti-forgery] false))))
