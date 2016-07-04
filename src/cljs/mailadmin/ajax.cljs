(ns mailadmin.ajax
  (:require [ajax.core :as ajax]
            [re-frame.core :refer [dispatch]]))

(defn default-headers [request]
  (-> request
      (update :uri #(str js/context %))
      (update
        :headers
        #(merge
          %
          {"Accept"       "application/transit+json"
           "x-csrf-token" js/csrfToken}))))

(defn load-interceptors! []
  (swap! ajax/default-interceptors
         conj
         (ajax/to-interceptor {:name    "default headers"
                               :request default-headers})))

;; AJAX utility wrappers
(defn fetch-domains []
  (ajax.core/GET
    "/domains"
    {:handler       #(dispatch [:process-domains-response %1])
     :error-handler #(dispatch [:bad-response %1])}))

(defn fetch-forwardings []
  (ajax.core/GET
    "/forwardings"
    {:handler       #(dispatch [:process-forwardings-response %1])
     :error-handler #(dispatch [:bad-response %1])}))

(defn reload-users []
  (ajax.core/GET
    "/users"
    {:handler       #(dispatch [:process-users-response %1])
     :error-handler #(dispatch [:bad-response %1])}))

(defn create-domain! [data]
  (js/console.log "create domain " data)
  (ajax.core/PUT
    "/domains"
    {:params        data
     :handler       #(do
                      (dispatch [:set-status %])
                      (dispatch [:fetch-domains]))
     :error-handler #(dispatch [:bad-response %1])}))

(defn update-domain! [data]
  (js/console.log "update domain " data)
  (ajax.core/POST
    (str "/domains/" (:id data))
    {:params        data
     :handler       #(do
                      (dispatch [:set-status %])
                      (dispatch [:fetch-domains]))
     :error-handler #(dispatch [:bad-response %1])}))

(defn delete-domain! [data]
  (js/console.log "delete domain " data)
  (ajax.core/DELETE
    (str "/domains/" (:id data))
    {:handler       #(do
                      (dispatch [:set-status %])
                      (dispatch [:fetch-domains]))
     :error-handler #(dispatch [:bad-response %1])}))

