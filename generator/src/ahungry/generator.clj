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

(defn entry->md [type {:keys [name home dist desc tags]}]
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
    <img src=\"https://img.shields.io/badge/type-%s-teal?style=plastic\" alt=\"dist\" />
    %s
  </td>
</tr>
 "
          name home name desc dist type (tags->md tags)))

(defn yaml->md [type yaml]
  (clojure.string/join (map (partial entry->md type) yaml)))

(defn generate []
  (let [yaml (slurp-yaml)
        preamble (slurp "../PREAMBLE.md")]
    (spit "../README.md"
          (format "
%s

<table>
%s
%s
%s
%s
%s
%s
%s
%s
%s
%s
%s
%s
%s
%s
%s
%s
</table>

"
                  preamble

                  (yaml->md :sundry1 (:sundry1 yaml))
                  (yaml->md :quests1 (:quests1 yaml))
                  (yaml->md :characters1 (:characters1 yaml))
                  (yaml->md :gameplay1 (:gameplay1 yaml))

                  (yaml->md :sundry2 (:sundry2 yaml))
                  (yaml->md :ui2 (:ui2 yaml))
                  (yaml->md :items2 (:items2 yaml))
                  (yaml->md :quests2 (:quests2 yaml))
                  (yaml->md :characters2 (:characters2 yaml))
                  (yaml->md :pos2 (:pos2 yaml))
                  (yaml->md :voice2 (:voice2 yaml))
                  (yaml->md :gameplay2 (:gameplay2 yaml))
                  (yaml->md :sys2 (:sys2 yaml))
                  (yaml->md :tactics2 (:tactics2 yaml))
                  (yaml->md :tweaks2 (:tweaks2 yaml))
                  (yaml->md :end2 (:end2 yaml))))))

(defn greet
  "Callable entry point to the application."
  [data]
  (println (str "Hello, " (or (:name data) "World") "!")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (greet {:name (first args)}))

(defn testit []
  (let [yaml (slurp-yaml)]
    (-> (flatten [(:sundry1 yaml)
                  (:quests1 yaml)
                  (:characters1 yaml)
                  (:gameplay1 yaml)
                  (:sundry2 yaml)
                  (:ui2 yaml)
                  (:items2 yaml)
                  (:quests2 yaml)
                  (:characters2 yaml)
                  (:pos2 yaml)
                  (:voice2 yaml)
                  (:gameplay2 yaml)
                  (:sys2 yaml)
                  (:tactics2 yaml)
                  (:tweaks2 yaml)
                  (:end2 yaml)])
        (crawl/take-screenshots))))
