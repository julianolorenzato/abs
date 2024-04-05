(ns converter.core
  (:require [broker.core :as broker]
            [converter.hls :refer [encode-hls-video]])
  (:gen-class))


(defn handle-new-msg
  "Verify if have a new published message in uploads channel and process it"
  [msg]
  (let [msg-type (get msg 0) msg-payload (get msg 2)]
    (when (= msg-type "message") (future (encode-hls-video msg-payload)))))


(defn -main
  "The converter's entrypoint"
  [& _args]
  (println "checking the connection!" (broker/ping))
  (broker/make-listener handle-new-msg))