(ns username
  (:require [mount.core :as mount]
            [mailadmin.figwheel :refer [start-fw stop-fw cljs]]
            mailadmin.core))

(defn start []
  (mount/start-without #'mailadmin.core/repl-server))

(defn stop []
  (mount/stop-except #'mailadmin.core/repl-server))

(defn restart []
  (stop)
  (start))


