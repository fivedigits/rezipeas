(ns rezipeas.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.core :refer :all]
            [hiccup.form :refer :all]
            [rezipeas.sql :refer :all]
            [rezipeas.pages :refer :all]
            [ring.util.response :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(db-setup)

(defn save-new-recipe [req]
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
            quantity (. Integer valueOf (second row))
            unit (nth row 2)]
        (insert-rec-ing db {:rec_id rec_id, :ing_id ing_id, :quantity quantity, :unit unit})))
    (redirect "/recipe/all")))

      
(defroutes app-routes
  (GET "/" [] (html [:h1 "Rezept des Tages"]))
  (GET "/recipe/all" [] (show-all-recipies))
  (GET "/recipe/new" [] (new-recipe))
  (GET "/recipe/edit" [] (html [:h1 "Rezept bearbeiten"]))
  (GET "/recipe/delete" [] (html [:h1 "Rezept löschen"]))
  (POST "/recipe/new" req (save-new-recipe req))
  (GET "/ingredients" [] (show-all-ingredients))
  (route/not-found "Not Found"))

(def app
  ;; enable anti-forgery later
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
