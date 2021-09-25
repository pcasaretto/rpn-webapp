(ns rpn-webapp.views
  (:require
   [re-frame.core :as re-frame]
   [rpn-webapp.subs :as subs]
   [rpn-webapp.events :as events]))
   
(defn main-panel []
  (let
      [
       name (re-frame/subscribe [::subs/name])
       stack (re-frame/subscribe [::subs/stack])
       input (re-frame/subscribe [::subs/input])]
      [:div
       [:ol
        (map (fn [value] [:li value]) @stack)]
       [:div.ui.form]
       [:div.required.field
        [:label "Label"]
        [:input {:type      "text"
                 :value     @input
                 :on-change #(re-frame/dispatch [::events/input-changed (-> % .-target .-value)])}]]
       [:input {:type      "submit"
                :on-click #(re-frame/dispatch [::events/input-submit])}]
       [:button {:on-click #(re-frame/dispatch [::events/sum-submit])} "+"]]))
