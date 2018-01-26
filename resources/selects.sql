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

-- :name get-tag-by-id
-- :command :query
-- :doc Get tag responding to id
SELECT * FROM tags WHERE id = :id;

-- :name get-ingredients
-- :command :query
-- :doc Get list of all ingredients
SELECT * FROM ingredients ORDER BY name;

-- :name get-ing-id
-- :command :query
-- :doc Get the id for a given ingredient
SELECT id FROM ingredients WHERE name = :name;

-- :name get-ing-by-id
-- :command :query
-- :doc Get ingredient with given name
SELECT * FROM ingredients WHERE id = :id;

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

-- :name get-recipies-by-tags-n-term
-- :command :query
-- :doc Get list of all recipies which match all given tags in :ids, :num-ids must be length of :ids. Also does full-text search for :term.
SELECT * FROM recipies
WHERE
:num_ids = ifnull(
      (SELECT COUNT(rec_id) FROM tagrec
      WHERE tag_id in (:v*:ids)
      AND rec_id = recipies.id
      GROUP BY rec_id), 0)
AND
(recipies.id in (
    SELECT recing.rec_id
    FROM ingredients JOIN recing
    ON recing.ing_id = ingredients.id
    WHERE instr(ingredients.name, :term) > 0)
 OR instr(recipies.name, :term) > 0
 OR instr(recipies.intro, :term) > 0
 OR instr(recipies.description, :term) > 0
 OR instr(recipies.tip, :term) > 0)
ORDER BY recipies.name;
