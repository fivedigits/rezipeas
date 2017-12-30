-- :name insert-recipe :! :n
-- :doc Insert a recipe into table recipies
INSERT INTO recipies (name, intro, description, tip)
       VALUES (:name, :intro, :description, :tip);

-- :name insert-tag :! :n
-- :doc Insert a new tag into table tags
INSERT INTO tags (name)
       VALUES (:tag);

-- :name insert-ingredient :! :n
-- :doc Inserts a new ingredients
INSERT INTO ingredients (name)
       VALUES (:name);

-- :name insert-tag-rec :! :n
-- :doc Inserts a tag - recipe tuple to tagrec table
INSERT INTO tagrec (tag_id, rec_id)
       VALUES (:tag_id, :rec_id);

-- :name insert-rec-ing :! :n
-- :doc Inserts a recipe - ingredient coupling
INSERT INTO recing (rec_id, ing_id, unit, quantity)
       VALUES (:rec_id, :ing_id, :unit, :quantity);
