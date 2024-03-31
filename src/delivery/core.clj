(ns delivery.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [ring.util.response :refer [redirect]]
            [broker.core :as broker]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            [clojure.java.io :as jio]))


(defn save-file [{{video "video"} :params}]
  (when (not (nil? video))
    (println video (type (video :tempfile)))

    (let [file (video :tempfile)]
      (jio/copy file (jio/file "resources/raw" (.getName file)))))
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Handler"})


(defroutes app-routes
  (GET "/" [] "<h1>Hello wordl</h1>")
  (POST "/upload" request
    ;; (println params)
    (save-file request)
    (redirect "/index.html"))
  (route/resources "/")
  (route/not-found "Page not found"))

(defn -main
  "The delivery's entrypoint"
  [& _args]
  (run-jetty (wrap-multipart-params app-routes) {:port 8080}))