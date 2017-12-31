(ns rezipeas.pages
  (:require [hiccup.core :refer :all]
            [hiccup.form :refer :all]
            [rezipeas.sql :refer :all]))

(defn show-all-recipies []
  """Returns <ul> of links to all recipies."""
  (let [recipies (get-recs db)]
    (html
     [:ul
      (reduce
       (fn [st rec] (concat st "<li>" (get rec :name) "</li>"))
       ""
       recipies)])))

(defn show-all-ingredients []
  """Returns <ul> of all ingredients."""
  (let [ingredients (get-ingredients db)]
    (html
     [:ul
      (reduce
       (fn [st ing] (concat st "<li>" (get ing :name) "</li>"))
       ""
       ingredients)])))

(defn new-recipe []
  """Returns the page containing the form for new recipies."""
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

