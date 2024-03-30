(ns delivery.core)

(defn -main
  "The delivery's entrypoint"
  [& args]
  (println (.toUpperCase "delivery's args: ") args))