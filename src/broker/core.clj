(ns broker.core
  (:require [taoensso.carmine :as car :refer [wcar]]))

(def ^:private wcar-opts {:pool {} :spec {:uri "redis://localhost:6379"}})

(defn publish [channel msg] (wcar wcar-opts (car/publish channel msg)))

(defn ping [] (wcar wcar-opts (car/ping)))

(defn make-listener [upload-handler]
  (car/with-new-pubsub-listener (:spec wcar-opts)
    {"uploads" upload-handler}
    (car/subscribe "uploads")))