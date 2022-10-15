(ns ahungry.generator
  (:require
   [clojure.string]
   [clj-yaml.core :as yaml]
   [ahungry.generator.crawler :as crawl]
   )
  (:gen-class))

(defn slurp-yaml []
  (-> (slurp "../mods.yaml")
      yaml/parse-string))

(defn get-color [tag]
  (let [red (mod (reduce + (map #(* % 3 ) (map int tag))) 255)
        green (mod (reduce + (map int tag)) 255)
        blue (mod (reduce + (map #(* % 2) (map int tag))) 255)]
    (format "%x%x%x" red green blue)))

(defn tags->md [tags]
  (clojure.string/join
   (map (fn [tag]
          (let [color (get-color tag)]
            (format
             "<img src=\"https://img.shields.io/badge/tag-%s-%s?style=plastic\" alt=\"%s\"/>"
             tag color tag)))
        tags)))

(defn entry->md [{:keys [name home dist desc tags]}]
  (format "
<tr class=\"mod-entry\">
  <td>
    <img src=\"https://ahungry.github.io/awesome-bgmods/ghpages/images/%s.png\" alt=\"pic\" />
  </td>
  <td>
    <a href=\"%s\">%s</a>
  </td>
  <td>
    %s
  </td>
  <td>
    <img src=\"https://img.shields.io/badge/dist-%s-purple?style=plastic\" alt=\"dist\" />
    %s
  </td>
</tr>
 "
          name home name desc dist (tags->md tags)))

(defn yaml->md [yaml]
  (clojure.string/join (map entry->md yaml)))

(defn sort-yaml [yaml]
  (sort-by :name #(compare (.toLowerCase (str %1)) (.toLowerCase (str %2))) yaml))

(defn generate []
  (let [yaml (slurp-yaml)
        preamble (slurp "../PREAMBLE.md")]
    (spit "../README.md"
          (format "
%s

<table>
%s
</table>

"
                  preamble
                  (yaml->md (sort-yaml (:mods yaml)))))))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn testit []
  (let [yaml (slurp-yaml)]
    (-> (:mods yaml)
        (crawl/take-screenshots))))

;; TODO: Swap based on cmd in makefile
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)})
  (generate)
  ;; (testit)
  )
