(ns rpn-webapp.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::input
 (fn [db]
   (:input db)))

(re-frame/reg-sub
 ::stack
 (fn [db]
   (:stack db)))
