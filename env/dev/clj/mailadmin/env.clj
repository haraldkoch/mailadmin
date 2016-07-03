(ns mailadmin.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [mailadmin.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[mailadmin started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[mailadmin has shut down successfully]=-"))
   :middleware wrap-dev})
