-- :name delete-tagrec :! :n
-- :doc Insert a recipe into table recipies
DELETE
FROM tagrec
WHERE rec_id = :rec_id;

-- :name delete-recing :! :n
-- :doc Insert a recipe into table recipies
DELETE
FROM recing
WHERE rec_id = :rec_id;
