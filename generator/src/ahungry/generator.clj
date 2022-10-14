(ns ahungry.generator
  (:require
   [clojure.string]
   [clj-yaml.core :as yaml])
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
%s%s
" name home dist (tags->md tags) desc))

(defn yaml->md [yaml]
  (clojure.string/join (map entry->md yaml)))

(defn generate []
  (let [yaml (slurp-yaml)
        preamble (slurp "../PREAMBLE.md")]
    (spit "/tmp/x.md"
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
### UI Enhancement
### Item Packs
### Quests/Story
### Characters
### Position Dependent Quest/Megamod Portions
### Voice Packs/Portraits
### Gameplay
### System Revisions
### Tactics
### Tweaks
### End of Order
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
