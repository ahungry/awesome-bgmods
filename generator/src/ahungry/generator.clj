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

(defn tags->md [tags]
  (clojure.string/join
   (map (fn [tag]
          (format "![tag](https://img.shields.io/badge/tag-%s-purple?style=plastic)\n" tag))
        tags)))

(defn entry->md [{:keys [name home dist desc tags]}]
  (format "
- [%s](%s)

![dist](https://img.shields.io/badge/dist-%s-purple?style=plastic)
%s

%s

<img src=\"https://ahungry.github.io/awesome-bgmods/ghpages/images/%s.png\" alt=\"pic\" />
 "
          name home dist (tags->md tags) desc name))

(defn yaml->md [yaml]
  (clojure.string/join (map entry->md yaml)))

(defn generate []
  (let [yaml (slurp-yaml)
        preamble (slurp "../PREAMBLE.md")]
    (spit "../README.md"
          (format "
%s

### Sundry/Miscellaneous
%s

### Quests/Story
%s

### Characters
%s

### Gameplay
%s

## Post-EET

Install/setup after EET

### Sundry/Miscellaneous
%s

### UI Enhancement
%s

### Item Packs
%s

### Quests/Story
%s

### Characters
%s

### Position Dependent Quest/Megamod Portions

This includes a few items that should be installed after
quests/characters, but still not quite end of order.

%s

### Voice Packs/Portraits
%s

### Gameplay
%s

### System Revisions
%s

### Tactics
%s

### Tweaks
%s

### End of Order
%s

"
                  preamble

                  (yaml->md (:sundry1 yaml))
                  (yaml->md (:quests1 yaml))
                  (yaml->md (:characters1 yaml))
                  (yaml->md (:gameplay1 yaml))

                  (yaml->md (:sundry2 yaml))
                  (yaml->md (:ui2 yaml))
                  (yaml->md (:items2 yaml))
                  (yaml->md (:quests2 yaml))
                  (yaml->md (:characters2 yaml))
                  (yaml->md (:pos2 yaml))
                  (yaml->md (:voice2 yaml))
                  (yaml->md (:gameplay2 yaml))
                  (yaml->md (:sys2 yaml))
                  (yaml->md (:tactics2 yaml))
                  (yaml->md (:tweaks2 yaml))
                  (yaml->md (:end2 yaml))

                  ))))

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
