(ns rezipeas.pages
  (:require [hiccup.core :refer :all]
            [hiccup.form :refer :all]
            [net.cgrand.enlive-html :as enlive]
            [rezipeas.sql :refer :all]))

(enlive/deftemplate random-menu "templates/random_menu.html"
  [menu]
  [:body] (enlive/content (:body menu)))

(enlive/deftemplate new-recipe "templates/new_recipe.html"
  [])

(enlive/defsnippet list-item "templates/list_item.html"
  [:li (enlive/nth-of-type 1)]
  [text href]
  [:a] (enlive/do->
        (enlive/content text)
        (enlive/set-attr :href href)))

(enlive/deftemplate list-view "templates/list.html"
  [title href items]
  [:title (enlive/content title)]
  [:.title (enlive/content title)]
  [:ul (enlive/nth-of-type 1)] (enlive/content
                                (map
                                 #(list-item % href)
                                 items)))

(defn random-menu-page []
  (random-menu {:body "hi there"}))

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

(defn new-recipe-page []
  """Returns the page containing the form for new recipies."""
  (new-recipe))

  
