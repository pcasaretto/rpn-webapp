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
         (update :stack #(conj % input-value))
         (assoc :input "")))))

(defn singular-stack-operation
  [operation stack]
  (->> (take 2 stack)       ; Get the top two values from the stack
       (reverse)            ; Reverse them to get the right order
       (apply operation)    ; Apply the operation on them
       (conj (drop 2 stack)))) ; Push the result back onto the stack

(re-frame/reg-event-db
 ::digit-clicked
 (fn [db [_ digit]]
   (-> db
       (update :input str digit))))

(re-frame/reg-event-db
 ::operation-submit
 (fn [db [_ operation-name]]
   (cond
     (= :subtract operation-name)
     (update db :stack (partial singular-stack-operation -))
     (= :sum operation-name)
     (update db :stack (partial singular-stack-operation +))
     (= :multiply operation-name)
     (update db :stack (partial singular-stack-operation *))
     (= :divide operation-name)
     (update db :stack (partial singular-stack-operation /)))))
