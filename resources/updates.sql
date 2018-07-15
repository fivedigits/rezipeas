-- :name change-recipe :! :n
-- :doc Insert a recipe into table recipies
UPDATE recipies
SET name = :name,
    intro = :intro,
    description = :description,
    tip = :tip,
    portions = :portions
WHERE id = :id;

-- :name change-image-url :! :n
-- :doc change the image url for a recipe
UPDATE recipies
SET image_url = :image_url
WHERE id = :id;

-- :name rename-tag-with-id :! :n
-- :doc Renames tag given by id.
UPDATE OR IGNORE tags
SET name = :name
WHERE id = :id;

-- :name merge-tag-into :! :n
-- :doc Replaces all occurences of tag with :old_id by tag with :new_id.
UPDATE tagrec
SET tag_id = :new-id
WHERE tag_id = :old-id;

-- :name rename-ing-with-id :! :n
-- :doc Rename ingredient with given id and name.
UPDATE OR IGNORE ingredients
SET name = :name
WHERE id = :id;

-- :name merge-ing-into :! :n
-- :doc Replaces all occurences of ing with :old_id by ing with :new_id
UPDATE recing
SET ing_id = :new_id
WHERE ing_id = :old_id;

