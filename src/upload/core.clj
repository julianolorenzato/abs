(ns upload.core
  (:require [broker.core :as broker]
            [clojure.java.shell :as jsh])
  (:gen-class))

(defn encode-hls-video
  [video-title segment-duration]
  (println "Starting to encode [" video-title "] to HLS")

  ;; (println (jsh/sh "ffmpeg"
  ;;                  "-i" (str "/videos/" video-title "/raw") ; input
  ;;                  "-profile:v" "baseline",
  ;;                  "-level" "3.0"
  ;;                  "-start_number" "0"
  ;;                  "-hls_time" (str segment-duration)
  ;;                  "-hls_list_size" "0"
  ;;                  "-f" "hls"
  ;;                  (str "/videos/" video-title "/hls/playlist.m3u8")))

  ;; (println (jsh/sh "echo" "ffmpeg"
  ;;                  "-i" (str "/videos/" video-title "/raw")
  ;;                  "-map" "0:v:0" "-map" "0:a:0" "-map" "0:v:0" "-map" "0:a:0" "-map" "0:v:0" "-map" "0:a:0"
  ;;                  "-c:v" "libx264" "-crf" "22" "-c:a" "aac" "-ar" "44100"
  ;;                  "-filter:v:0" "scale=w=480:h=360"  "-maxrate:v:0" "600k" "-b:a:0" "500k"
  ;;                  "-filter:v:1" "scale=w=640:h=480"  "-maxrate:v:1" "1500k" "-b:a:1" "1000k"
  ;;                  "-filter:v:2" "scale=w=1280:h=720" "-maxrate:v:2" "3000k" "-b:a:2" "2000k"
  ;;                  "-var_stream_map" "\"v:0,a:0,name:360p v:1,a:1,name:480p v:2,a:2,name:720p\""
  ;;                  "-preset" "fast" "-hls_list_size" "0" "-threads" "0" "-f" "hls"
  ;;                  "-hls_time" "10" "-hls_flags" "independent_segments"
  ;;                  "-master_pl_name" "\"playlist.m3u8\""
  ;;                  "-y" (str "/videos/" video-title "/hls/playlist-%v.m3u8")))
  
  (println (jsh/sh "ffmpeg"
                   "-i" (str "/videos/" video-title "/raw")
                   "-map" "0:v:0" "-map" "0:a:0" "-map" "0:v:0" "-map" "0:a:0" "-map" "0:v:0" "-map" "0:a:0"
                   "-c:v" "libx264" "-crf" "22" "-c:a" "aac" "-ar" "44100"
                   "-filter:v:0" "scale=w=480:h=360"  "-maxrate:v:0" "600k" "-b:a:0" "500k"
                   "-filter:v:1" "scale=w=640:h=480"  "-maxrate:v:1" "1500k" "-b:a:1" "1000k"
                   "-filter:v:2" "scale=w=1280:h=720" "-maxrate:v:2" "3000k" "-b:a:2" "2000k"
                   "-var_stream_map" "\"v:0,a:0,name:360p" "v:1,a:1,name:480p" "v:2,a:2,name:720p\""
                   "-preset" "fast" "-hls_list_size" "0" "-threads" "0" "-f" "hls"
                   "-hls_time" "10" "-hls_flags" "independent_segments"
                   "-master_pl_name" "\"playlist.m3u8\""
                   "-y" (str "/videos/" video-title "/hls/playlist-%v.m3u8")))

  (println "[" video-title "] encoded to HLS successfully!"))


(defn handle-new-msg
  "Verify if have a new published message in uploads channel and process it"
  [msg]
  (let [msg-type (get msg 0) msg-payload (get msg 2)]
    (when (= msg-type "message") (future (encode-hls-video msg-payload 5)))))


(defn -main
  "The upload's entrypoint"
  [& _args]
  (println "checking the connection!" (broker/ping))
  (broker/make-listener handle-new-msg))