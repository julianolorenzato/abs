(ns build
  (:require [clojure.tools.build.api :as b]))

(def build-folder "target")
(def jar-content-folder (str build-folder "/classes"))

(def basis (b/create-basis {:project "deps.edn"}))
(def version "0.0.1")
(def app-name "Vizmo Port to Clojure")
(def uber-file-name (format
                     "%s/%s-%s-standalone.jar"
                     build-folder
                     app-name
                     version))

(defn clean [_]
  (b/delete {:path build-folder})
  (println (format "Build folder \"%s\" removed" build-folder)))

(defn uber [{main-ns :main}]
  (clean nil)

  (b/copy-dir {:src-dirs ["resources"]
               :target-dir jar-content-folder})

  (b/compile-clj {:basis basis
                  :src-dirs ["src"]
                  :class-dir jar-content-folder})

  (b/uber {:class-dir jar-content-folder
           :uber-file uber-file-name
           :basis basis
           :main main-ns}))