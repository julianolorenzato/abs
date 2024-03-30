(ns delivery.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [broker.core :as broker]
            [clojure.java.io :as jio]))

(defn- save-file [file filename]
  (spit (str "resources/public/raw" filename) file))

(defn handler [{{video "video"} :params}]
  (when (not (nil? video))
    (println video (type (video :tempfile)))

    (let [file (video :tempfile)]
      ;; (save-file file-content uuid)
      (jio/copy file (jio/file "resources/public/raw" (.getName file)))
      ;; (broker/publish "uploads" uuid)
      ))
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Handler"})

(def app
  (->
   handler
   (wrap-params)
   (wrap-multipart-params)
   (wrap-resource "")
   (wrap-content-type)
   (wrap-not-modified)))

(defn -main
  "The delivery's entrypoint"
  [& _args]
  (run-jetty app {:port 8080}))