(ns mailadmin.db.core
  (:require
    [clojure.java.jdbc :as jdbc]
    [conman.core :as conman]
    [mailadmin.config :refer [env]]
    [mount.core :refer [defstate]])
  (:import [java.sql
            BatchUpdateException
            PreparedStatement]))

(defstate ^:dynamic *db*
          :start (conman/connect!
                   {:jdbc-url (env :database-url)})
          :stop (conman/disconnect! *db*))

(conman/bind-connection *db* "sql/queries.sql")

(defn to-date [sql-date]
  (-> sql-date (.getTime) (java.util.Date.)))

(extend-protocol jdbc/IResultSetReadColumn
  java.sql.Date
  (result-set-read-column [v _ _] (to-date v))

  java.sql.Timestamp
  (result-set-read-column [v _ _] (to-date v)))

(extend-type java.util.Date
  jdbc/ISQLParameter
  (set-parameter [v ^PreparedStatement stmt idx]
    (.setTimestamp stmt idx (java.sql.Timestamp. (.getTime v)))))

(declare create-domain! delete-domain! find-domain get-all-domains get-domain update-domain!
         create-forwarding! delete-forwarding! find-forwarding get-all-forwardings get-forwarding update-forwarding!
         create-user! delete-user! find-user get-all-users get-user update-user!)
