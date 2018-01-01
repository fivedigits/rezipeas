(ns rezipeas.pages
  (:require [net.cgrand.enlive-html :as enlive]
            [rezipeas.sql :refer :all]))

(enlive/deftemplate new-recipe "templates/new_recipe.html"
  [])

(enlive/defsnippet tag "templates/recipe.html"
  [:#tags]
  [tag]
  [:span] (enlive/content (:name tag)))

(enlive/defsnippet ingredient "templates/recipe.html"
  [:#ingredients]
  [ing]
  [:.ingredient [:.quantity]] (enlive/content (str (:quantity ing)))
  [:.ingredient [:.unit]] (enlive/content (:unit ing))
  [:.ingredient [:.name]] (enlive/content (:name ing)))

(enlive/deftemplate recipe-view "templates/recipe.html"
  [recipe tags ingredients]
  [:title] (enlive/content (:name recipe))
  [:header [:h1]] (enlive/content (:name recipe))
  [:#intro] (enlive/content (:intro recipe))
  [:#tags] (enlive/content
            (map tag tags))
  [:#ingredients] (enlive/content
                   (map ingredient ingredients))
  [:#description] (enlive/content (:description recipe))
  [:#tip] (enlive/content (:tip recipe)))
   

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

(defn show-recipe [id]
  """Returns the page displaying the recipe with given id."""
  (let [recipe (first (get-rec-by-id db {:id id}))
        rec_id (:id recipe)
        tags (get-tags-for-rec db {:rec_id rec_id})
        ingredients (get-rec-ingredients db {:rec_id rec_id})]
    (recipe-view recipe tags ingredients)))

  
