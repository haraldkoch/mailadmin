(ns mailadmin.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [mailadmin.layout :refer [error-page]]
            [mailadmin.routes.home :refer [home-routes]]
            [mailadmin.routes.domains :refer [domain-routes]]
            [mailadmin.routes.forwardings :refer [forwarding-routes]]
            [compojure.route :as route]
            [mailadmin.env :refer [defaults]]
            [mount.core :as mount]
            [mailadmin.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (-> #'home-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'domain-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (-> #'forwarding-routes
        (wrap-routes middleware/wrap-csrf)
        (wrap-routes middleware/wrap-formats))
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
