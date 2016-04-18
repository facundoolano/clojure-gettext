(ns gettext.scan)

(defn- is-clj
  [file]
  (#{"clj" "cljc" "cljs"} (last (clojure.string/split (.getName file) #"\."))))

(defn- get-files
  [dir]
  (filter is-clj (file-seq (clojure.java.io/file dir))))

(defn- extract-text
  [expressions]
  (let [extract (fn [expr]
                  (cond
                    (and (seq? expr) (#{'_ 'gettext} (first expr))) (second expr)
                    (and (seq? expr) (#{'p_ 'pgettext} (first expr))) (nth expr 2)))]
    (filter not-empty (map extract (tree-seq coll? identity expressions)))))

(defn- zipzip [s] (zipmap s s))

(defn scan-files
  "Walk the given directory and for every clj file extract the strings that
  appear enclosed by _, p_, gettext or pgettext."
  ([] (scan-files (.getAbsolutePath (java.io.File. ""))))
  ([dir]
   (->>
     (get-files dir)
     (mapcat (comp extract-text read-string #(str "(" % ")") slurp))
     zipzip
     (into (sorted-map)))))
