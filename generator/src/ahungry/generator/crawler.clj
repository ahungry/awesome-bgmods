(ns ahungry.generator.crawler
  (:import (com.thoughtworks.selenium DefaultSelenium)
           ;; (org.openqa.selenium.remote.server SeleniumServer)
           (org.openqa.selenium Dimension OutputType TakesScreenshot WebDriver)
           (org.openqa.selenium.firefox FirefoxBinary FirefoxDriver FirefoxOptions)
           java.util.Date
           (java.io FileWriter)
           (java.text SimpleDateFormat))
  (:require [clojure.zip :as zip]
            [clojure.xml :as xml]
            [mikera.image.core :as mikera]))

(defn spit-bytes
  "Takes in bytes[] and spits to a file."
  [filename bytes]
  (with-open
    [out-stream (clojure.java.io/output-stream filename :encoding "ASCII")
     channel (java.nio.channels.Channels/newChannel out-stream)]
    (.write channel (java.nio.ByteBuffer/wrap bytes))))

(defmacro with-selenium [& body]
  `(let [p# (. (Runtime/getRuntime) exec "/usr/bin/Xvfb :99")
        firefox# (FirefoxBinary.)]
    (.addCommandLineOptions firefox# (into-array ["--display=:99" "--window-size=800,800"]))
    (let [driver-opts# (FirefoxOptions.)]
      (doto driver-opts#
        (.setBinary firefox#)
        (.setAcceptInsecureCerts true)
        (.setHeadless true))
      (let [driver# (FirefoxDriver. driver-opts#)]
        (def driver driver#)
        (-> driver# .manage .window (.setSize (Dimension. 800 600)))
        (def res ~@body)
        (.quit driver#)
        res))))

(defn take-screenshot [driver name url]
  (try
    (.get driver url)
    (let [img (.getScreenshotAs driver OutputType/BYTES)]
      (spit-bytes (str "/tmp/" name "-wip.png") img)
      (-> (mikera/load-image (str "/tmp/" name "-wip.png"))
          (mikera/resize 200)
          (mikera/save (str "../ghpages/images/" name ".png"))))
    (catch Exception e (prn (str "failure on: " url)))))

(defn take-screenshots [urls]
  (with-selenium
    (doall (map (fn [{:keys [name home]} ] (take-screenshot driver name home)) urls))))

(defn testit []
  (take-screenshots
   [
    {:name "one" :home "http://example.com"}
    {:name "two" :home "http://ahungry.com"}
    ;; {:name "three" :home "http://localhost:12345"}
    ]))
