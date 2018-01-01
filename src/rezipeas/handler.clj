(ns rezipeas.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [rezipeas.sql :refer :all]
            [rezipeas.pages :refer :all]
            [ring.util.response :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(db-setup)

(defn save-new-recipe [req]
  """Commits a new recipe to the database."""
  (let [params (get req :params)
        name (get params :name)
        ingredient (get params :ingredient)
        quantity (get params :quantity)
        unit (get params :unit)
        intro (get params :intro)
        tip (get params :tip)
        description (get params :description)]
    (insert-recipe db {:name name, :intro intro, :description description, :tip tip})
    (doseq [ing ingredient]
      (insert-ingredient db {:name ing}))
    (doseq [row (map vector ingredient quantity unit)]
      (let [ing_id (get-ing-id db {:name (first row)})
            rec_id (get-rec-id db {:name name})
            quantity (second row)
            unit (nth row 2)]
        (insert-rec-ing db {:rec_id rec_id, :ing_id ing_id, :quantity quantity, :unit unit})))
    (redirect "/recipies")))

      
(defroutes app-routes
  (GET "/" [] (redirect "/recipies"))
  (GET "/recipies" [] (show-all-recipies))
  (GET "/recipies/new" [] (new-recipe))
  (POST "/recipies/new" req (save-new-recipe req))
  (route/not-found "Not Found"))

(def app
  ;; enable anti-forgery later
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
