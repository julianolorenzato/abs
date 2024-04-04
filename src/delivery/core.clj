(ns delivery.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.util.response :refer [redirect]]
            [broker.core :as broker]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [clojure.java.io :as jio])
  (:import [java.io File])
  (:gen-class))

(defn- create-dir-structure [title]
  (if (and
       (.mkdir (File. (str "/videos/" title)))
       (.mkdir (File. (str "/videos/" title "/hls")))
       (.mkdir (File. (str "/videos/" title "/dash"))))
    (println "Directory structure built successfully!")
    (throw (Exception. "Something goes wrong during directory structure building"))))

(defn- save-raw-file [file title]
  (jio/copy file (jio/file (str "/videos/" title) "raw")))

(defn- handle-upload [video title]
  (when (not (nil? video))
    (let [file (video :tempfile)]
      (create-dir-structure title)
      (save-raw-file file title)
      (broker/publish "uploads" title))))


(defroutes app-routes
  (route/files "/streaming" {:root "/videos"})
  (GET "/" [] (slurp (jio/resource "public/index.html")))
  (POST "/upload" {{video "video" title "title"} :params}
    (handle-upload video title)
    (redirect "/"))
  (route/not-found "Page not found"))

(defn -main
  "The delivery's entrypoint"
  [& _args]
  (run-jetty (wrap-multipart-params app-routes) {:port 8080}))