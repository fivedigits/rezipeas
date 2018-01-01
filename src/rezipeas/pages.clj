(ns rezipeas.pages
  (:require [net.cgrand.enlive-html :as enlive]
            [rezipeas.sql :refer :all]))

(enlive/deftemplate new-recipe "templates/new_recipe.html"
  [])

(enlive/defsnippet list-item "templates/list.html"
  [:#list]
  [name hrefpre id]
  [:li [:a]] (enlive/do->
        (enlive/content name)
        (enlive/set-attr :href (str hrefpre id))))

(enlive/deftemplate list-view "templates/list.html"
  [title hrefpre items]
  [:title] (enlive/content title)
  [:.title] (enlive/content title)
  [:ul#list] (enlive/content
              (map
               #(list-item (:name %) hrefpre (:id %))
               items)))

(defn show-all-recipies []
  """Returns <ul> of links to all recipies."""
  (let [recipies (get-recs db)]
    (list-view "Alle Rezepte" "/recipies/" recipies)))

(defn show-all-ingredients []
  """Returns <ul> of all ingredients."""
  (let [ingredients (get-ingredients db)]
    (list-view "Alle Zutaten" "/ingredients/" ingredients)))

(defn new-recipe-page []
  """Returns the page containing the form for new recipies."""
  (new-recipe))

  
