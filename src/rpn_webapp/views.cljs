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

(defn digits-panel [f]
  (map (fn [value] [:button {:on-click #((f value))} value]) (range 9)))

   
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
              [:label "Label"]
              [:input {:type      "text"
                       :value     @input
                       :on-change #(re-frame/dispatch [::events/input-changed (-> % .-target .-value)])}]
              [:input {:type      "submit"
                       :on-click #(re-frame/dispatch [::events/input-submit])}]]]]
         (digits-panel #(re-frame/dispatch [::events/digit-clicked %]))
         [Cluster
          [:button {:on-click #(re-frame/dispatch [::events/operation-submit :sum])} "+"]
          [:button {:on-click #(re-frame/dispatch [::events/operation-submit :subtract])} "-"]
          [:button {:on-click #(re-frame/dispatch [::events/operation-submit :multiply])} "*"]
          [:button {:on-click #(re-frame/dispatch [::events/operation-submit :divide])} "/"]]]]))
