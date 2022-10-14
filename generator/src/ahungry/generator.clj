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
          (format
           "<img src=\"https://img.shields.io/badge/tag-%s-purple?style=plastic\" alt=\"%s\"/>"
           tag tag))
        tags)))

(defn entry->md [{:keys [name home dist desc tags]}]
  (format "
<div class=\"mod-entry\">
  <p><a href=\"%s\">%s</a></p>
  <p class=\"tags\">
    <img src=\"https://img.shields.io/badge/dist-%s-purple?style=plastic\" alt=\"dist\" />
    %s
  </p>
<p class=\"desc\">%s</p>
<img src=\"https://ahungry.github.io/awesome-bgmods/ghpages/images/%s.png\" alt=\"pic\" />
</div>
 "
          home name dist (tags->md tags) desc name))

(defn yaml->md [yaml]
  (clojure.string/join (map entry->md yaml)))

(defn generate []
  (let [yaml (slurp-yaml)
        preamble (slurp "../PREAMBLE.md")]
    (spit "../README.md"
          (format "
%s

### Sundry/Miscellaneous
<div class=\"container\">
%s
</div>

### Quests/Story
<div class=\"container\">
%s
</div>

### Characters
<div class=\"container\">
%s
</div>

### Gameplay
<div class=\"container\">
%s
</div>

## Post-EET

Install/setup after EET

### Sundry/Miscellaneous
<div class=\"container\">
%s
</div>

### UI Enhancement
<div class=\"container\">
%s
</div>

### Item Packs
<div class=\"container\">
%s
</div>

### Quests/Story
<div class=\"container\">
%s
</div>

### Characters
<div class=\"container\">
%s
</div>

### Position Dependent Quest/Megamod Portions

This includes a few items that should be installed after
quests/characters, but still not quite end of order.

<div class=\"container\">
%s
</div>

### Voice Packs/Portraits
<div class=\"container\">
%s
</div>

### Gameplay
<div class=\"container\">
%s
</div>

### System Revisions
<div class=\"container\">
%s
</div>

### Tactics
<div class=\"container\">
%s
</div>

### Tweaks
<div class=\"container\">
%s
</div>

### End of Order
<div class=\"container\">
%s
</div>

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
