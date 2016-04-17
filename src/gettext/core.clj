(ns gettext.core
  (:require [clojure.test :refer [function?]]))

; Load text source from project.clj
(def project-map (->> "project.clj" slurp read-string (drop 3) (partition 2) (map vec) (into {})))
(if (:gettext-source project-map)
  (require [(symbol (namespace (:gettext-source project-map)))]))
(def ^:dynamic *text-source* (eval (:gettext-source project-map)))

(defn gettext
  "Look up the given key in the current text source dictionary.
  If not found return the key itself."
  [text-key & replacements]
  (let [text-value (get *text-source* text-key text-key)
        text-value (if (function? text-value) (text-value nil) text-value)]
    (apply format text-value replacements)))

(defn pgettext
  [ctx text-key & replacements]
  (let [text-value (get *text-source* text-key text-key)
        text-value (if (function? text-value) (text-value ctx) text-value)]
    (apply format text-value replacements)))

; handy aliases
(def _ gettext)
(def p_ pgettext)

