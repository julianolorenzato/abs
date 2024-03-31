(ns delivery.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.util.response :refer [redirect]]
            [broker.core :as broker]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [clojure.java.io :as jio]))


(defn handle-upload [video title]
  (when (not (nil? video))
    (println title video)
    (let [file (video :tempfile)]
      (jio/copy file (jio/file "resources/raw" title))
      (broker/publish "uploads" title))))


(defroutes app-routes
  (GET "/" [] "<h1>Hello wordl</h1>")
  (POST "/upload" {{video "video" title "title"} :params}
    (handle-upload video title)
    (redirect "/index.html"))
  (route/resources "/")
  (route/not-found "Page not found"))

(defn -main
  "The delivery's entrypoint"
  [& _args]
  (run-jetty (wrap-multipart-params app-routes) {:port 8080}))