(ns rezipeas.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [hiccup.core :refer :all]
            [hiccup.form :refer :all]
            [rezipeas.sql :refer :all]
            [ring.util.response :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(db-setup)

(defn new-recipe []
  (html [:header [:h1 "Neues Rezept"]]
        [:script "function newIngredient() {
                  var ings = document.getElementsByClassName(\"ingredient\");
                  var ing = ings[0];
                  var newing = ing.cloneNode(true);
                  var i;
                  for (i = 0; i < 3; i++) {
                      newing.childNodes[i].value = \"\";
                  }
                  var ingform = document.getElementById(\"ingredients\");
                  ingform.appendChild(newing);
                  }"]
         [:main
          [:form {:method "post" :action "/recipe/new"}
           [:input {:type "text" :placeholder "Name des Rezepts" :name "name"}]
           [:textarea {:placeholder "Einleitende Worte" :name "intro"}]
           [:div#ingredients
            [:button {:type "button" :onclick "newIngredient()"} "+ Zutat"]
            [:div.ingredient
             [:input {:type "number" :step "0.01" :placeholder "0.0" :name "quantity"}]
             [:input {:type "text" :placeholder "Einheit" :name "unit"}]
             [:input {:type "text" :placeholder "Zutat" :name "ingredient"}]]]
           [:textarea {:placeholder "Rezept" :name "description"}]
           [:textarea {:placeholder "Tip" :name "tip"}]
           (submit-button "Fertig")]]))

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

(defn show-all-recipies []
  """Returns <ul> of links to all recipies."""
  (let [recipies (get-recs db)]
    (html
     [:ul
      (reduce (fn [st rec] (concat st "<li>" (get rec :name) "</li>")) "" recipies)])))

(defn show-all-ingredients []
  """Returns <ul> of all ingredients."""
  (let [ingredients (get-ingredients db)]
    (html
     [:ul
      (reduce (fn [st ing] (concat st "<li>" (get ing :name) "</li>")) "" ingredients)])))
      
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
