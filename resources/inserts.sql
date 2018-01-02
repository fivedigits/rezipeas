-- :name insert-recipe :! :n
-- :doc Insert a recipe into table recipies
INSERT OR IGNORE INTO recipies (name, intro, description, tip, portions, image_url)
       VALUES (:name, :intro, :description, :tip, :portions, :image_url);

-- :name insert-tag :! :n
-- :doc Insert a new tag into table tags
INSERT OR IGNORE INTO tags (name)
       VALUES (:name);

-- :name insert-ingredient :! :n
-- :doc Inserts a new ingredients
INSERT OR IGNORE INTO ingredients (name)
       VALUES (:name);

-- :name insert-tag-rec :! :n
-- :doc Inserts a tag - recipe tuple to tagrec table
INSERT OR IGNORE INTO tagrec (tag_id, rec_id)
       VALUES (:tag_id, :rec_id);

-- :name insert-rec-ing :! :n
-- :doc Inserts a recipe - ingredient coupling
INSERT OR IGNORE INTO recing (rec_id, ing_id, unit, quantity)
       VALUES (:rec_id, :ing_id, :unit, :quantity);
