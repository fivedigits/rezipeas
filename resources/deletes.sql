-- :name delete-tagrec :! :n
-- :doc Delete all tag-rec relations by rec_id.
DELETE
FROM tagrec
WHERE rec_id = :rec_id;

-- :name delete-recing :! :n
-- :doc Delete all rec-ing relations by rec_id.
DELETE
FROM recing
WHERE rec_id = :rec_id;

-- :name delete-rec-by-id :! :n
-- :doc Delete a recipe with given id.
DELETE
FROM recipies
WHERE id = :id;

-- :name delete-orphan-tags :! :n
-- :doc Deletes all tags which have no recipies attached.
DELETE
FROM tags
WHERE tags.id NOT IN (SELECT tag_id FROM tagrec);

-- :name delete-orphan-ings :! :n
-- :doc Deletes all ingredients which have no recipies attached.
DELETE
FROM ingredients
WHERE ingredients.id NOT IN (SELECT ing_id FROM recing);

-- :name delete-tag-by-id :! :n
-- :doc Deletes tag with given id from tags.
DELETE
FROM tags
WHERE id = :id;
