(ns rezipeas.sql
  (:require [hugsql.core :as hug]
            [rezipeas.config :refer :all])
  (:gen-class))

(def db {:dbtype "sqlite"
         :dbname (str rootpath dbname)})

(hug/def-db-fns "db-setup.sql")
(hug/def-db-fns "selects.sql")
(hug/def-db-fns "inserts.sql")
(hug/def-db-fns "updates.sql")
(hug/def-db-fns "deletes.sql")

(defn db-setup []
  """Creates all tables, if not existent."""
  (do
    (create-recipe-table db)
    (create-ingredient-table db)
    (create-tag-table db)
    (create-rec-ingredients-table db)
    (create-tag-rec-table db)))
