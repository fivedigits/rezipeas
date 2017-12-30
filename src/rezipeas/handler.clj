(ns rezipeas.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.core :refer :all]
            [hiccup.form :refer :all]
            [rezipeas.sql :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(db-setup)

(defn new-recipe []
  (html [:header [:h1 "Neues Rezept"]]
         [:main
          [:form {:method "post" :action "/recipe/new"}
           (text-field :name)
           (text-area :intro)
           (text-field :ingredients)
           (text-area :description)
           (text-area :tip)
           (submit-button "Fertig")]]))

(defn save-new-recipe [req]
  (do
    (let [params (:form-params req)
        name (get params "name")
        intro (get params "intro")
        ingredients (get params "ingredients")
        description (get params "description")
        tip (get params "tip")]
      (insert-recipe db {:name name, :intro intro, :description description, :tip tip}))
    (redirect "/recipe/all")))

(defn show-all-recipies []
  """Returns <ul> of links to all recipies."""
  (let [recipies (get-recs db)]
    (html
     [:ul
      (reduce (fn [st rec] (concat st "<li>" (get rec :name) "</li>")) "" recipies)])))
      
  
(defroutes app-routes
  (GET "/" [] (html [:h1 "Rezept des Tages"]))
  (GET "/recipe/all" [] (show-all-recipies))
  (GET "/recipe/new" [] (new-recipe))
  (GET "/recipe/edit" [] (html [:h1 "Rezept bearbeiten"]))
  (GET "/recipe/delete" [] (html [:h1 "Rezept löschen"]))
  (POST "/recipe/new" req (save-new-recipe req))
  (route/not-found "Not Found"))

(def app
  ;; enable anti-forgery later
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
