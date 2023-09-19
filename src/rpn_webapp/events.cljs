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

(defn swap
  [stack]
  (->> (take 2 stack)
       reverse
       ( concat (drop 2 stack))))

(defn singular-stack-operation
  [operation stack]
  (->> (take 1 stack)          ; Get the top two values from the stack
       (apply operation)       ; Apply the operation on them
       (conj (drop 1 stack)))) ; Push the result back onto the stack

(defn pair-stack-operation
  [operation stack]
  (->> (take 2 stack)          ; Get the top two values from the stack
       reverse                 ; Reverse them to get the right order
       (apply operation)       ; Apply the operation on them
       (conj (drop 2 stack)))) ; Push the result back onto the stack

(def operators
  {:subtract (partial pair-stack-operation -)
   :sum (partial pair-stack-operation +)
   :multiply (partial pair-stack-operation *)
   :divide (partial pair-stack-operation /)
   :power (partial pair-stack-operation (fn [x y] (js/Math.pow x y)))
   :inverse (partial singular-stack-operation (fn [x] (/ 1 x)))
   :negate (partial singular-stack-operation (fn [x] (- x)))
   :square-root (partial singular-stack-operation (fn [x] (js/Math.sqrt x)))
   :factorial (partial singular-stack-operation (fn [x] (apply * (range 1 (inc x)))))
   :summation (fn [stack] (conj '() (reduce + stack)))
   :pop (fn [stack] (drop 1 stack))
   :swap swap})


(re-frame/reg-event-db
 ::operation-submit
 (fn [db [_ operation-name]]
   (let [operation (get operators operation-name)]
     (cond
       (empty? (:stack db)) db ; if the stack is empty, just return the original db without modification
       (nil? operation)     db ; if the operation is not valid, just return the original db without modification
       :else                (update db :stack operation)))))
