(ns mailadmin.handlers
  (:require [re-frame.core :refer [register-handler dispatch path trim-v after debug]]
            [mailadmin.ajax :as a]))

;; debug
(defn log-ex
  [handler]
  (fn log-ex-handler
    [db v]
    (try
      (handler db v)                                        ;; call the handler with a wrapping try
      (catch :default e                                     ;; oops
        (do
          (.error js/console e.stack)                       ;; print a sane stack trace
          (throw e))
        ))))
(def standard-middlewares [log-ex debug])

(defn clear-indicators [] (dispatch [:set-error nil]) (dispatch [:set-status nil]))

(register-handler :fetch-domains standard-middlewares (fn [db _] (a/fetch-domains) db))
(register-handler :fetch-forwardings standard-middlewares (fn [db _] (a/fetch-forwardings) db))
(register-handler :fetch-users standard-middlewares (fn [db _] (a/reload-users) db))
(register-handler :set-status standard-middlewares (fn [db [_ status]] (-> db (assoc :status status))))
(register-handler :set-error standard-middlewares (fn [db [_ error]] (-> db (assoc :error error))))
(register-handler :bad-response standard-middlewares (fn [db [_ response]] (-> db (assoc :error (get-in response [:response :error])))))

(register-handler
  :initialize
  standard-middlewares
  (fn
    [db _]
    (clear-indicators)
    (-> db
        (assoc :users-loaded? false)
        (assoc :domains-loaded? false)
        (assoc :forwardings-loaded? false))
    (a/fetch-domains)
    (a/fetch-forwardings)
    (a/reload-users)
    db))

(register-handler
  :process-domains-response
  standard-middlewares
  (fn
    [db [_ response]]
    (-> db
        (assoc :domains-loaded? true)
        (assoc :domains response))))

(register-handler
  :process-forwardings-response
  standard-middlewares
  (fn
    [db [_ response]]
    (-> db
        (assoc :forwardings-loaded? true)
        (assoc :forwardings response))))

(register-handler
  :process-users-response
  standard-middlewares
  (fn
    [db [_ response]]
    (-> db
        (assoc :users-loaded? true)
        (assoc :users response))))

(register-handler
  :add-domain
  standard-middlewares
  (fn
    [db [_ form-data]]
    (clear-indicators)
    (a/create-domain! form-data)
    db))

(register-handler
  :update-domain
  standard-middlewares
  (fn
    [db [_ form-data]]
    (clear-indicators)
    (a/update-domain! form-data)
    db))

(register-handler
  :delete-domain
  standard-middlewares
  (fn
    [db [_ form-data]]
    (clear-indicators)
    (a/delete-domain! form-data)
    db))