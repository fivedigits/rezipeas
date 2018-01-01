-- :name get-rec-by-name
-- :command :query
-- :doc Retrieve a recipe by name
SELECT * FROM recipies WHERE instr(name,:name)>0;

-- :name get-rec-id
-- :command :query
-- :doc Retrieve id for a given recipe
SELECT id FROM recipies WHERE name = :name;

-- :name get-rec-by-id
-- :command :query
-- :doc Retrieve recipe by given id
SELECT * FROM recipies WHERE id = :id;

-- :name get-recs
-- :command :query
-- :doc Get all recipies, ordered by name
SELECT * FROM recipies ORDER BY name;

-- :name get-tags
-- :command :query
-- :doc Get list of all tags
SELECT * FROM tags ORDER BY name;

-- :name get-tag-id
-- :command :query
-- :doc Get id of tag given by name
SELECT id FROM tags WHERE name = :name;

-- :name get-ingredients
-- :command :query
-- :doc Get list of all ingredients
SELECT * FROM ingredients ORDER BY name;

-- :name get-ing-id
-- :command :query
-- :doc Get the id for a given ingredient
SELECT id FROM ingredients WHERE name = :name;

-- :name get-rec-ingredients
-- :command :query
-- :doc Get list of all ingredients for a particular recipe
SELECT DISTINCT ingredients.id, ingredients.name, recing.quantity, recing.unit
FROM ingredients JOIN recing
ON ingredients.id = recing.ing_id
WHERE recing.rec_id = :rec_id;

-- :name get-tags-for-rec
-- :command :query
-- :doc Get list of all tag for given recipe id
SELECT DISTINCT tags.name
FROM tags JOIN tagrec
ON tags.id = tagrec.tag_id
WHERE tagrec.rec_id = :rec_id;

-- write some query to get recipe by full-text search and tags
