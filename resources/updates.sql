-- :name change-recipe :! :n
-- :doc Insert a recipe into table recipies
UPDATE recipies
SET name = :name,
    intro = :intro,
    description = :description,
    tip = :tip,
    portions = :portions,
    image_url = :image_url
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

