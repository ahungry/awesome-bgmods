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

;; http://al3xandr3.github.io/clojure-selenium-crawler.html
;; https://stackoverflow.com/questions/1504034/take-a-screenshot-of-a-web-page-in-java
;; https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/remote/RemoteWebDriver.html#getScreenshotAs(org.openqa.selenium.OutputType)
;; https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/OutputType.html

(defn spit-bytes
  "Takes in bytes[] and spits to a file."
  [filename bytes]
  (with-open
    [out-stream (clojure.java.io/output-stream filename :encoding "ASCII")
     channel (java.nio.channels.Channels/newChannel out-stream)]
    (.write channel (java.nio.ByteBuffer/wrap bytes))))

(defn buffered-image->bytes
  "Converts java.awt.image.BufferedImage into a bytes[] array."
  [img]
  (let [baos (java.io.ByteArrayOutputStream.)]
    (javax.imageio.ImageIO/write img "jpg" baos)
    (.toByteArray baos)))

(defn take-screenshot []
  (let [p (. (Runtime/getRuntime) exec "/usr/bin/Xvfb :99")
        firefox (FirefoxBinary.)]
    (.addCommandLineOptions firefox (into-array ["--display=:99" "--window-size=800,800"]))
    (let [driver-opts (FirefoxOptions.)]
      (doto driver-opts
        (.setBinary firefox)
        (.setAcceptInsecureCerts true)
        (.setHeadless true))
      (let [driver (FirefoxDriver. driver-opts)]
        (-> driver .manage .window (.setSize (Dimension. 800 600)))
        (.get driver "http://ahungry.com")
        (let [img (.getScreenshotAs driver OutputType/BYTES)]
          (spit-bytes "/tmp/wip.png" img)
          (-> (mikera/load-image "/tmp/wip.png")
              (mikera/resize 200)
              (mikera/save "/tmp/phew.png")
               ;; buffered-image->bytes
               ;; (spit-bytes "/tmp/success.png")
               ))))))

;; (defmacro with-selenium
;;   [browser & body]
;;   `(let [server# (new SeleniumServer)]
;;     (.start server#)
;;     (let [~browser
;;          (new DefaultSelenium "localhost", 4444, "*firefox", "http://www.google.com/")]
;;       (.start ~browser)
;;       (.setTimeout ~browser "100000")
;;       ~@body
;;       (.stop ~browser))
;;       (.stop server#)))

;; (def *js-eval* "this.browserbot.getCurrentWindow().document.title;")
;; (defn check-a-page [a-browser a-url]
;;   (try
;;   (.open a-browser a-url)
;;     (Thread/sleep 3000) ; make a little timeout, to avoid overloading server
;;     (println (str a-url "," (.getEval a-browser *js-eval*)))
;;     (catch Exception e
;;     (println (str a-url "," e)))))

;; (defn check-pages [url-list]
;;   (with-selenium browser
;;     (binding [*out* (FileWriter.
;;          (str "output/log_" (.format (SimpleDateFormat. "yyyy-MM-dd") (Date.)) ".csv"))]
;;       (doseq [a-url url-list]
;;         (check-a-page browser a-url)))))

;; (defn xml-to-zip [url]
;;   "read xml url into a tree"
;;   (zip/xml-zip (xml/parse url)))

;; (defn pick-a-sample [a-percentage a-list]
;;   "picks a subset (a-)percentage of the total"
;;     (filter #(if (> (rand) (- 1 (/ a-percentage 100))) %) a-list))

;; (defn process-sitemap [sitemap-url]
;;   (let [u-list (xml-> (xml-to-zip sitemap-url) :url :loc text)]
;;     (check-pages (pick-a-sample 1 u-list))))

;; (def *sitemap* "http://www.google.com/sitemap.xml")

;use: (process-sitemap *sitemap*)
