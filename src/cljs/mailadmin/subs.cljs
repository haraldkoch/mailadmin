(ns mailadmin.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))


(register-sub :status
              (fn [db _] (reaction (:status @db))))

(register-sub :error
              (fn [db _] (reaction (:error @db))))

(register-sub
  :domains
  (fn [db _]
    (let [domains (reaction (:domains @db))]
      (reaction (sort-by :domain @domains)))))

(register-sub :domains-loaded?
              (fn [db _] (reaction (:domains-loaded? @db))))

(register-sub
  :forwardings
  (fn [db _]
    (let [forwardings (reaction (:forwardings @db))]
      (reaction (sort-by :forwarding @forwardings)))))

(register-sub :forwardings-loaded?
              (fn [db _] (reaction (:forwardings-loaded? @db))))

(register-sub
  :users
  (fn [db _]
    (let [users (reaction (:users @db))]
      (reaction (sort-by :user @users)))))

(register-sub :users-loaded?
              (fn [db _] (reaction (:users-loaded? @db))))
