-- :name create-recipe-table
-- :command :execute
-- :doc Create table for recipies
CREATE TABLE IF NOT EXISTS recipies (id INTEGER PRIMARY KEY,
	name TEXT NOT NULL,
	intro TEXT,
	description TEXT NOT NULL,
	tip TEXT,
	views INTEGER DEFAULT 0,
	portions INTEGER DEFAULT 1,
	image_url TEXT,
	CONSTRAINT unique_name UNIQUE (name));

-- :name create-tag-table
-- :command :execute
-- :doc Create table for tags
CREATE TABLE IF NOT EXISTS tags (id INTEGER PRIMARY KEY,
       name TEXT NOT NULL,
       CONSTRAINT unique_name UNIQUE (name));

-- :name create-ingredient-table
-- :command :execute
-- :doc Create table for ingredients
CREATE TABLE IF NOT EXISTS ingredients (id INTEGER PRIMARY KEY,
       name TEXT NOT NULL,
       CONSTRAINT unique_name UNIQUE (name));

-- :name create-rec-ingredients-table
-- :command :execute
-- :doc Create table for recipe ingredient relationship
CREATE TABLE IF NOT EXISTS recing (rec_id INTEGER NOT NULL,
       ing_id INTEGER NOT NULL,
       unit TEXT,
       quantity REAL);

-- :name create-tag-rec-table
-- :command :execute
-- :doc Create table for tag and recipe relation
CREATE TABLE IF NOT EXISTS tagrec (tag_id INTEGER NOT NULL,
       rec_id INTEGER NOT NULL,
       CONSTRAINT unique_rel UNIQUE (tag_id, rec_id));



