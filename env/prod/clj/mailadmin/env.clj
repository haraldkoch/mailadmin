(ns mailadmin.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[mailadmin started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[mailadmin has shut down successfully]=-"))
   :middleware identity})
