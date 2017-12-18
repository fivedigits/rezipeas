(ns rezipeas.pages
  (:require [net.cgrand.enlive-html :as enlive]
            [rezipeas.sanitize :refer [prepare-quantity, prepare-unit]]
            [rezipeas.sql :refer :all]))

(enlive/defsnippet nav-bar "templates/recipe.html"
  [:#nav-bar]
  [])

(enlive/defsnippet hidden-recipe-btn "templates/recipe.html"
  [:#first-nav]
  [recipe url icon]
  [:#first-nav] (enlive/set-attr :href (str url (:id recipe)))
  [:#first-nav] (enlive/add-class "hideable")
  [:#first-nav :> :img] (enlive/set-attr :src (str "/assets/" icon)))

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
  [:#nav-bar] (enlive/append (hidden-recipe-btn recipe "/recipies/edit/" "gear.png"))
  [:#nav-bar] (enlive/append (hidden-recipe-btn recipe "/recipies/delete/" "rubbish.png"))
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

(enlive/defsnippet tag-input "templates/new_recipe.html"
  [:.tag]
  [tag]
  [:.tag] (enlive/set-attr :value (:name tag)))

(enlive/defsnippet ing-input "templates/new_recipe.html"
  [:.ingredient]
  [ingredient]
  [:.quantity] (enlive/set-attr :value (:quantity ingredient))
  [:.unit] (enlive/set-attr :value (:unit ingredient))
  [:.ingname] (enlive/set-attr :value (:name ingredient)))

(enlive/deftemplate edit-recipe "templates/new_recipe.html"
  [recipe tags ingredients]
  [:title] (enlive/content "Rezept bearbeiten")
  [:#title] (enlive/content (str "Rezept bearbeiten: " (:name recipe)))
  [:header] (enlive/content (nav-bar))
  [:#nav-bar] (enlive/append (hidden-recipe-btn recipe "/recipies/delete/" "rubbish.png"))
  [:#input-form] (enlive/set-attr :action (str "/recipies/edit/" (:id recipe)))
  [:#name-form] (enlive/set-attr :value (:name recipe))
  [:#intro-form] (enlive/content (:intro recipe))
  [:#tags] (enlive/content
            (map tag-input tags))
  [:#portions] (enlive/set-attr :value (:portions recipe))
  [:#ingredients] (enlive/content
                   (map ing-input ingredients))
  [:#desc-form] (enlive/content (:description recipe))
  [:#tip-form] (enlive/content (:tip recipe)))


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

(enlive/deftemplate delete-view "templates/delete_item.html"
  [base_url item]
  [:title] (enlive/content (str (:name item) " löschen?"))
  [:header] (enlive/content (nav-bar))
  [:#delete-prompt] (enlive/content (str (:name item) " wirklich löschen?"))
  [:#delete-form] (enlive/set-attr :action (str base_url (:id item))))

(defn show-delete-recipe [id]
  """Returns page with delete prompt for recipies."""
  (let [recipe (first (get-rec-by-id db {:id id}))]
    (delete-view "/recipies/delete/" recipe)))

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

(defn edit-recipe-page[id]
  """Returns the pre-filled edit page for recipe with given id."""
  (let [recipe (first (get-rec-by-id db {:id id}))
        rec_id (:id recipe)
        tags (get-tags-for-rec db {:rec_id rec_id})
        ingredients (get-rec-ingredients db {:rec_id rec_id})]
    (edit-recipe recipe tags ingredients)))

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
  
