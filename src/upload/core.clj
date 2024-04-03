(ns upload.core
  (:require [broker.core :as broker]
            [clojure.java.shell :as jsh])
  (:import [java.io File])
  (:gen-class))

(defn encode-hls-video
  [video-title segment-duration]
  (let [raw-path (str "resources/raw/" video-title)
        processed-directory (str "resources/public/hls/" video-title)]

    (if (.mkdir (File. (str "resources/public/hls/" video-title)))
      (println "directory [resources/public/hls/" video-title "] created successfully!")
      (println "error creating directory!"))

    (println "Starting to encode [" video-title "] to HLS")

    (jsh/sh "ffmpeg"
            "-i" (str "resources/raw/" video-title) ; input
            "-profile:v" "baseline",
            "-level" "3.0"
            "-start_number" "0"
            "-hls_time" (str segment-duration)
            "-hls_list_size" "0"
            "-f" "hls"
            (str "resources/public/hls/" video-title "/playlist.m3u8"))

    (println "[" video-title "] encoded to HLS successfully!")))


(defn handle-new-msg
  "Verify if have a new published message in uploads channel and process it"
  [msg]
  (let [msg-type (get msg 0) msg-payload (get msg 2)]
    (when (= msg-type "message") (future (encode-hls-video msg-payload 5)))))


(defn -main
  "The upload's entrypoint"
  [& args]
  (println "checking the connection!" (broker/ping))
  (broker/make-listener handle-new-msg)
  (println "upload's args: " args))