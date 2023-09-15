(ns rpn-webapp.views
  (:require
   [re-frame.core :as re-frame]
   [dv.cljs-emotion-reagent :refer [jsx css defstyled keyframes global-style theme-provider]]
   [rpn-webapp.subs :as subs]
   [rpn-webapp.events :as events]))

(defstyled Center
  :div
  {
   :box-sizing "content-box"
   :margin-inline "auto"
   :max-inline-size "var(--measure, 60ch)"
   :height "95vh"
   :display "flex"
   :flex-direction "column"
   :justify-content "flex-end"
   :align-items "center"})
   
(defstyled ScrollableStack
  :div
  {
   :display "flex"
   :flex-direction "column-reverse"  ; Reverse the column direction
   :justify-content "flex-start"
   :flex "1 1 auto"
   :overflow-y "auto"
   "> *" {:margin-block 0}
   "> * + *" {:margin-block-start "1.5rem"}})

(defstyled InputKeyboardSection
  :div
  {
   :display "flex"
   :flex-direction "column"
   :flex "0 0 auto"})  ; This ensures the section doesn't grow and takes only the required space
   

(defstyled Cluster
  :div
  {
   :display "flex"
   :flex-wrap "wrap"
   :gap "var(--space, 1rem)"
   :justify-content "center"
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

(defstyled KeyboardButton
  :button
  {
   :flex "1 1 0px"
   :min-width "60px"  ; Minimum button width
   :min-height "60px"  ; Minimum button height
   :font-size "1.5rem"})  ; Larger font size for better visibility


;; Update the input style for better visibility on larger screens
(defstyled LargeInput
  :input
  {
   :font-size "1.5rem"  ; Larger font size for input
   :padding "0.5rem"})


(defn main-panel []
  (let
      [
       name (re-frame/subscribe [::subs/name])
       stack (re-frame/subscribe [::subs/stack])
       input (re-frame/subscribe [::subs/input])]
    [Center
     [ScrollableStack
      [:ol
       {:reversed true}
       (map-indexed (fn [idx value] [:li {:key idx} value]) @stack)]]
     [InputKeyboardSection
      [Cluster
        [:div.required.field
           [LargeInput {:type      "text"
                        :value     @input
                        :disabled  true
                        :on-change #(re-frame/dispatch [::events/input-changed (-> % .-target .-value)])}]]
        [Keyboard
         [Cluster
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 7])} "7"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 8])} "8"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 9])} "9"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/operation-submit :divide])} "÷"]]
         [Cluster
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 4])} "4"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 5])} "5"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 6])} "6"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/operation-submit :multiply])} "×"]]
         [Cluster
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 1])} "1"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 2])} "2"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 3])} "3"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/operation-submit :subtract])} "−"]]
         [Cluster
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked 0])} "0"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/digit-clicked "."])} "."]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/input-submit])} "Enter"]
          [KeyboardButton {:on-click #(re-frame/dispatch [::events/operation-submit :sum])} "+"]]]]]]))
