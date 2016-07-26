(ns mailadmin.routes.users
  (:require [mailadmin.db.core :as db]
            [mailadmin.routes.home :refer [response-handler]]
            [mailadmin.validation :refer [validate-user]]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.coercions :refer [as-int]]
            [ring.util.http-response :as response]))


(defn create-user! [params]
  (db/create-user! params)
  (str "user " (:username params) " created successfully"))

(defn handle-create-user! [{:keys [:params]}]
  (if-let [errors (validate-user params)]
    (response/internal-server-error {:error errors})
    (create-user! params)))


(defn update-user! [params]
  (db/update-user! params)
  (str "user " (:username params) " created successfully"))

(defn handle-update-user! [_ {:keys [:params]}]
  (if-let [errors (validate-user params)]
    (response/internal-server-error {:error errors})
    (update-user! params)))


(response-handler handle-get-users [] (db/get-all-users))
(response-handler handle-get-user [id] (db/get-user {:id id}))
(response-handler handle-find-user [username] (db/find-user {:username username}))
(response-handler handle-create-user [request] (handle-create-user! request))
(response-handler handle-update-user [id request] (handle-update-user! id request))
(response-handler handle-delete-user [id] (db/delete-user! {:id id}))


(defroutes user-routes
           (GET "/users" [] (handle-get-users))
           (GET "/users/:id" [id] (handle-get-user id))
           (GET "/users/?username=:username" [username] (handle-find-user username))
           (POST "/users" request (handle-create-user request))
           (PUT "/users/:id" [id :as request] (handle-update-user id request))
           (DELETE "/users/:id" [id] (handle-delete-user id)))
