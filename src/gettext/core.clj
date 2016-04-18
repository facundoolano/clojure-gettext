(ns gettext.core
  (:require [cfg.current :refer [project]]
            [clojure.test :refer [function?]]))

; Load text source from project.clj
(if (:gettext-source project)
  (require [(symbol (namespace (:gettext-source project)))]))
(def ^:dynamic *text-source* (eval (:gettext-source project)))

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

