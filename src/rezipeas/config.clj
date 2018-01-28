(ns rezipeas.config
  (:require [clojure.edn :as edn]
            [clojure.java.io :refer [make-parents]]))

(def rootpath (if (= "root" (. System getProperty "user.name"))
                "/var/www/rezipeas/"
                (str (. System getProperty "user.dir") (java.io.File/separator) "rezipeas" (java.io.File/separator))))

(make-parents (str rootpath "/img/dummy.txt"))

(def dbname "rezipeas.db")
