(ns mailadmin.routes.forwardings
  (:require [mailadmin.db.core :as db]
            [mailadmin.routes.home :refer [response-handler]]
            [mailadmin.validation :refer [validate-user validate-forwarding]]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.coercions :refer [as-int]]
            [ring.util.http-response :as response]
            [clojure.tools.logging :as log]))


(defn create-forwarding! [params]
  (db/create-forwarding! (params))
  (str "forwarding for " (:source params) " created successfully"))

(defn handle-create-forwarding! [{:keys [:params]}]
  (if-let [errors (validate-forwarding params)]
    (response/internal-server-error {:error errors})
    (create-forwarding! params)))


(defn update-forwarding! [params]
  (db/update-forwarding! (params))
  (str "forwarding for " (:source params) " updated successfully"))

(defn handle-update-forwarding! [_ {:keys [:params]}]
  (if-let [errors (validate-forwarding params)]
    (response/internal-server-error {:error errors})
    (update-forwarding! params)))


(response-handler handle-get-forwardings [] (db/get-all-forwardings))
(response-handler handle-get-forwarding [id] (db/get-forwarding {:id id}))
(response-handler handle-create-forwarding [request] (handle-create-forwarding! request))
(response-handler handle-update-forwarding [id request] (handle-update-forwarding! id request))
(response-handler handle-delete-forwarding [id] (db/delete-forwarding! {:id id}))


(defroutes forwarding-routes
           (GET "/forwardings" [] (handle-get-forwardings))
           (GET "/forwardings/:id" [id :<< as-int] (handle-get-forwarding id))
           (PUT "/forwardings" request (handle-create-forwarding request))
           (POST "/forwardings/:id" [id :<< as-int :as request] (handle-update-forwarding id request))
           (DELETE "/forwardings/:id" [id :<< as-int] (handle-delete-forwarding id)))
