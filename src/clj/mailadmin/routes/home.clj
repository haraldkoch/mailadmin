(ns mailadmin.routes.home
  (:require [mailadmin.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(defmacro response-handler [fn-name args & body]
  `(defn ~fn-name ~args
     (try
       (response/ok (do ~@body))
       (catch Exception e#
         (log/error e# "error handling request")
         (response/internal-server-error {:error (.getMessage e#)})))))

(defn home-page []
  (layout/render "home.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/docs" [] (response/ok (-> "docs/docs.md" io/resource slurp))))

