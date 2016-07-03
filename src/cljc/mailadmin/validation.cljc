(ns mailadmin.validation
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]))

(def domain-match #"^(?:[A-Za-z][A-Za-z0-9-]+\.)+[A-Za-z][A-Za-z0-9-]+$")
(def email-match  #"^[\w._-]+@(?:[A-Za-z][\w-]+\.)+[A-Za-z][\w-]+")
(defn validate-domain [params]
  (first
    (b/validate
      params
      :domain [v/required v/string [v/matches domain-match]])))

(defn validate-forwarding [params]
  (first
    (b/validate
      params
      :source [v/required v/string [v/matches email-match]]
      :destination [v/required v/string [v/matches email-match]])))

(defn validate-user [params]
  (first
    (b/validate
      params
      :username [v/required [v/matches #"^[A-Za-z][A-Za-z0-9]*$"]]
      :first_name v/required
      :last_name v/required)))