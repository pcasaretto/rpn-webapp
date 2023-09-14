(ns rpn-webapp.events-test
  (:require [rpn-webapp.events :as sut]
            [rpn-webapp.subs :as subs]
            [re-frame.core :as rf]
            [day8.re-frame.test :as rf-test]
            [cljs.test :as t :include-macros true]))

(t/deftest init-test
  (rf-test/run-test-sync
    (rf/dispatch [::sut/initialize-db])
    (let [stack (rf/subscribe [::subs/stack])]
        (t/is (= [] @stack)))))

(t/deftest digits-test
  (rf-test/run-test-sync
    (rf/dispatch [::sut/digit-clicked 1])
    (rf/dispatch [::sut/digit-clicked 3])
    (let [input (rf/subscribe [::subs/input])]
        (t/is (= "13" @input)))))

(t/deftest sum-test
  (rf-test/run-test-sync
    (rf/dispatch [::sut/digit-clicked 1])
    (rf/dispatch [::sut/digit-clicked 3])
    (rf/dispatch [::sut/input-submit])
    (rf/dispatch [::sut/digit-clicked 2])
    (rf/dispatch [::sut/digit-clicked 4])
    (rf/dispatch [::sut/input-submit])
    (rf/dispatch [::sut/operation-submit :sum])
    (let [stack (rf/subscribe [::subs/stack])]
        (t/is (= [37] @stack)))))

(t/deftest subtract-test
  (rf-test/run-test-sync
    (rf/dispatch [::sut/digit-clicked 1])
    (rf/dispatch [::sut/digit-clicked 3])
    (rf/dispatch [::sut/input-submit])
    (rf/dispatch [::sut/digit-clicked 2])
    (rf/dispatch [::sut/digit-clicked 4])
    (rf/dispatch [::sut/input-submit])
    (rf/dispatch [::sut/operation-submit :subtract])
    (let [stack (rf/subscribe [::subs/stack])]
        (t/is (= [-11] @stack)))))