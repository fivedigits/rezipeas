(ns rezipeas.sanitize
  (:require [clojure.string :refer [capitalize, trim]]
            [clojure.edn :as edn]))

(defn wrap-and-sanitize [fun object]
  """Wraps single objects in a list and returns the map of fun."""
  (map
   fun
   (if (seq? object)
    object
    (list object))))

(defn sanitize-ingredients [ingredients]
  """Wraps single ingredients in list trims and capitalizes."""
  (wrap-and-sanitize (comp capitalize trim) ingredients))

(defn sanitize-tags [tags]
  """Wraps single tags in list and trims whitespace."""
  (wrap-and-sanitize trim tags))

(defn sanitize-units [units]
  """Wraps single units in list and trims whitespace."""
  (wrap-and-sanitize trim units))

(defn sanitize-quantities [quantities]
  """Wraps single quantities in list and reads as number."""
  (wrap-and-sanitize edn/read-string quantities))

(defn sanitize-portions [portions]
  """Converts to number."""
  (edn/read-string portions))

