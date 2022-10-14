(ns user
  (:require
   [clojure.main]
   ;; [clojure.tools.logging :as log]
   [ahungry.generator :as g]))

(prn "Greetings from clj")

(defn go []
  (apply require clojure.main/repl-requires))

(go)

(g/greet {})
