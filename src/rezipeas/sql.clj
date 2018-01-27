(ns rezipeas.sql
  (:require [hugsql.core :as hug]
            [clojure.java.io :refer [make-parents]])
  (:gen-class))

;;(def rootpath (str (. System getProperty "user.dir") (java.io.File/separator)))

(def rootpath "/var/www/rezipeas/")

(make-parents "/var/www/rezipeas/img/dummy.txt")

(def dbname "rezipeas.db")

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
