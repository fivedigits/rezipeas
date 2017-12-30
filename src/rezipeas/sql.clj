(ns rezipeas.sql
  (:require [hugsql.core :as hug])
  (:gen-class))

(def db {:dbtype "sqlite"
         :dbname "test.db"})

(hug/def-db-fns "db-setup.sql")
(hug/def-db-fns "selects.sql")
(hug/def-db-fns "inserts.sql")

(defn db-setup []
  """Creates all tables, if not existent."""
  (do
    (create-recipe-table db)
    (create-ingredient-table db)
    (create-tag-table db)
    (create-rec-ingredients-table db)
    (create-tag-rec-table db)))
