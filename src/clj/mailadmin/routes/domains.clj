(ns mailadmin.routes.domains
  (:require [mailadmin.db.core :as db]
            [mailadmin.routes.home :refer [response-handler]]
            [mailadmin.validation :refer [validate-user validate-domain]]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.coercions :refer [as-int]]
            [ring.util.http-response :as response]
            [clojure.tools.logging :as log]))


(defn create-domain! [params]
  (db/create-domain! (params))
  (str "domain " (:domain params) " created successfully"))

(defn handle-create-domain! [{:keys [:params]}]
  (if-let [errors (validate-domain params)]
    (response/internal-server-error {:error errors})
    (create-domain! params)))


(defn update-domain! [params]
  (db/update-domain! (params))
  (str "domain " (:domain params) " created successfully"))

(defn handle-update-domain! [_ {:keys [:params]}]
  (if-let [errors (validate-domain params)]
    (response/internal-server-error {:error errors})
    (update-domain! params)))


(response-handler handle-get-domains [] (db/get-all-domains))
(response-handler handle-get-domain [id] (db/get-domain {:id id}))
(response-handler handle-find-domain [domain] (db/find-domain {:domain domain}))
(response-handler handle-create-domain [request] (handle-create-domain! request))
(response-handler handle-update-domain [id request] (handle-update-domain! id request))
(response-handler handle-delete-domain [id] (db/delete-domain! {:id id}))


(defroutes domain-routes
           (GET "/domains" [] (handle-get-domains))
           (GET "/domains/:id" [id :<< as-int] (handle-get-domain id))
           (GET "/domains/?domain=:domain" [domain] (handle-find-domain domain))
           (PUT "/domains" request (handle-create-domain request))
           (POST "/domains/:id" [id :<< as-int :as request] (handle-update-domain id request))
           (DELETE "/domains/:id" [id :<< as-int] (handle-delete-domain id)))
