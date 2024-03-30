(ns upload.core
  (:require [broker.core :as broker]
            [clojure.java.io :as jio])
  (:import [net.bramp.ffmpeg FFmpeg]
           [net.bramp.ffmpeg.builder FFmpegBuilder]))

(defn run-ffmpeg
  []
  (->
   (FFmpegBuilder.)
   (.setFormat "input.mp4")
   (.overrideOutputFiles true)
   (.addOutput "output.mp4")))


(defn process-media [path] (println "media processed: " path))

(defn handle-new-msg
  "Verify if have a new published message in uploads channel and process it"
  [msg]
  (let [msg-type (get msg 0) msg-payload (get msg 2)]
    (when (= msg-type "message") (process-media msg-payload))))


(defn -main
  "The upload's entrypoint"
  [& args]
  (println "checking the connection!" (broker/ping))
  (broker/make-listener handle-new-msg)
  ;; (consumer/listener)
  ;; (println (build-ffmpeg "/usr/bin/ffmpeg"))
  (println "upload's args: " args))