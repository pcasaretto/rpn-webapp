(ns rpn-webapp.views
  (:require
   [re-frame.core :as re-frame]
   [cljs-styled-components.reagent :refer [defstyled defkeyframes theme-provider clj-props set-default-theme!]]
   [rpn-webapp.subs :as subs]
   [rpn-webapp.events :as events]))

(defstyled Center
  :div
  {
   :box-sizing "content-box"
   :margin-inline "auto"
   :max-inline-size "var(--measure, 60ch)"})

(defstyled Stack
  :div
  {
   :display "flex"
   :flex-direction "column"
   :justify-content "flex-start"
   "> *" {:margin-block 0}
   "> * + *" {:margin-block-start "1.5rem"}})

(defstyled Cluster
  :div
  {
   :display "flex"
   :flex-wrap "wrap"
   :gap "var(--space, 1rem)"
   :justify-content "flex-start"
   :align-items "center"})

(defstyled Keyboard
  :div
  {
   :display "flex"
   :flex-direction "column"
   :align-content "stretch"
   :align-items "stretch"
   "> *" {:margin-block 0}
   "> * + *" {:margin-block-start "1.5rem"}})

(defstyled KeyboardLine
  :div
  {
   :display "flex"
   :align-content "stretch"
   :align-items "stretch"})


(defn main-panel []
  (let
      [
       name (re-frame/subscribe [::subs/name])
       stack (re-frame/subscribe [::subs/stack])
       input (re-frame/subscribe [::subs/input])]
      [Center
       [Cluster
         [Stack
           [:ol
            (map-indexed (fn [idx value] [:li {:key idx} value]) @stack)]
           [Cluster
             [:div.required.field
              [:input {:type      "text"
                       :value     @input
                       :disabled  true
                       :on-change #(re-frame/dispatch [::events/input-changed (-> % .-target .-value)])}]]]
           [Keyboard
             [Cluster
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 7])} "7"]
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 8])} "8"]
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 9])} "9"]
               [:button {:on-click #(re-frame/dispatch [::events/operation-submit :sum])} "+"]]
             [Cluster
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 4])} "4"]
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 5])} "5"]
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 6])} "6"]
               [:button {:on-click #(re-frame/dispatch [::events/operation-submit :subtract])} "-"]]
             [Cluster
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 1])} "1"]
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 2])} "2"]
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 3])} "3"]
               [:button {:on-click #(re-frame/dispatch [::events/operation-submit :multiply])} "x"]]
             [Cluster
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked 0])} "0"]
               [:button {:on-click #(re-frame/dispatch [::events/digit-clicked "."])} "."]
               [:button {:on-click #(re-frame/dispatch [::events/input-submit])} "Enter"]
               [:button {:on-click #(re-frame/dispatch [::events/operation-submit :divide])} "/"]]]]]]))
