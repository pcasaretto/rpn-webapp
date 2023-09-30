(ns rpn-webapp.events
  (:require
   [re-frame.core :as re-frame]
   [rpn-webapp.db :as db]
   [stack.operations]))


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
   (let [input-value (js/parseFloat (:input db))]
     (if (js/isNaN input-value)
       db ; if the input is not a valid number, just return the original db without modification
       (-> db
           (update :stack #(conj % input-value))
           (assoc :input ""))))))

(re-frame/reg-event-db
 ::digit-clicked
 (fn [db [_ digit]]
   (-> db
       (update :input str digit))))

(re-frame/reg-event-db
 ::operation-submit
 (fn [db [_ operation-name]]
   (let [operation (get stack.operations/operators operation-name)]
     (cond
       (empty? (:stack db)) db ; if the stack is empty, just return the original db without modification
       (nil? operation)     db ; if the operation is not valid, just return the original db without modification
       :else                (update db :stack operation)))))
