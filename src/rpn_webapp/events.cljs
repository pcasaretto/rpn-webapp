(ns rpn-webapp.events
  (:require
   [re-frame.core :as re-frame]
   [rpn-webapp.db :as db]))
   

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::input-changed
 (fn [db [_ new-value]]
    (assoc db :input new-value)))

(re-frame/reg-event-db
 ::input-submit
 (fn [db _]
   (let [ input-value (js/parseFloat (:input db))]
    (-> db
        (assoc :stack (conj (:stack db) input-value))
        (assoc :input "")))))

(re-frame/reg-event-db
 ::sum-submit
 (fn [db _]
   (let

       [stack (:stack db)
        first (peek stack)
        stack (pop stack)
        second (peek stack)
        stack (pop stack)
        value (+ first second)
        stack (conj stack value)]
    (-> db
        (assoc :stack stack)))))
