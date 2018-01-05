(ns rezipeas.pages
  (:require [net.cgrand.enlive-html :as enlive]
            [rezipeas.sanitize :refer [prepare-quantity, prepare-unit]]
            [rezipeas.sql :refer :all]))

(enlive/defsnippet nav-bar "templates/recipe.html"
  [:header]
  [])

(enlive/defsnippet tag-checkbox "templates/search.html"
  [:.stupid-checkbox-container]
  [tag]
  [:.tag-checkbox] (enlive/set-attr :value (:id tag))
  [:.tag-label] (enlive/content (:name tag)))

(enlive/deftemplate search-mask "templates/search.html"
  [tags]
  [:header] (enlive/content (nav-bar))
  [:#tags] (enlive/content
            (map tag-checkbox tags)))

(enlive/defsnippet tag "templates/recipe.html"
  [:.tag]
  [tag]
  [:span] (enlive/content (:name tag)))

(enlive/defsnippet ingredient "templates/recipe.html"
  [:.ingredient]
  [portions ing]
  [:.ingredient [:.quantity]] (enlive/content (prepare-quantity (:quantity ing) (:unit ing) portions))
  [:.ingredient [:.unit]] (enlive/content (prepare-unit (:unit ing)))
  [:.ingredient [:.name]] (enlive/content (:name ing)))

(enlive/deftemplate recipe-view "templates/recipe.html"
  [recipe tags ingredients]
  [:title] (enlive/content (:name recipe))
  [:#title] (enlive/content (:name recipe))
  [:#picture] (enlive/set-attr :src (if (:image_url recipe) (str "/img/" (:image_url recipe)) "/assets/default.jpg"))
  [:#intro] (enlive/content (:intro recipe))
  [:#tags] (enlive/content
            (map tag tags))
  [:#portions-display] (enlive/content (str (:portions recipe)))
  [:#ingredients] (enlive/content
                   (map (partial ingredient (:portions recipe)) ingredients))
  [:#description] (enlive/content (:description recipe))
  [:#tip] (enlive/content (:tip recipe)))

(enlive/deftemplate new-recipe "templates/new_recipe.html"
  []
  [:header] (enlive/content (nav-bar)))

(enlive/defsnippet list-item "templates/list.html"
  [:.list-item]
  [name hrefpre id]
  [:a] (enlive/do->
        (enlive/content name)
        (enlive/set-attr :href (str hrefpre id))))

(enlive/deftemplate list-view "templates/list.html"
  [title hrefpre items]
  [:title] (enlive/content title)
  [:header] (enlive/content (nav-bar))
  [:#title] (enlive/content title)
  [:#list] (enlive/content
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

(defn show-search-result [term tags]
  """Returns the page displaying all recipies which match all given tags or a search mask, if no args where given."""
  (if (and (nil? term) (empty? tags))
    (search-mask (get-tags db))
    (let [recipies (get-recipies-by-tags-n-term db {:num_ids (count tags) :ids tags :term term})]
      (list-view "Suchergebnisse" "/recipies/" recipies))))
  
