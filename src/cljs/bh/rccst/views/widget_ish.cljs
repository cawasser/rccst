(ns bh.rccst.views.widget-ish
  (:require [re-frame.core :as re-frame]

            [bh.rccst.subs :as subs]
            [bh.rccst.events :as events]))
            ;[bh.rccst.subscriptions :as subscriptions]))



(defn- lookup-control [uuid]
  (let [result (re-frame/subscribe [::subs/lookup uuid])
        error (re-frame/subscribe [::subs/lookup-error])]
    [:div
     [:button.button {:on-click #(re-frame/dispatch [::events/lookup uuid])} "Lookup"]
     [:div
      [:h3 "Result: " @result]
      [:h3 "Error: " @error]]]))


(defn- number-control [uuid]
  (let [number (re-frame/subscribe [::subs/source :number])]
    [:h3 "Number: " @number]))


(defn- string-control [uuid]
  (let [s (re-frame/subscribe [::subs/source :string])]
    [:h3 "String: " @s]))


(defn- subscription-error-control
  "simple control to show the most recent error message from the subscribe function
  "
  []
  (let [error (re-frame/subscribe [::subs/subscribe-error uuid])]
    [:div
     [:h3 "Error:" @error]]))


(defn view
  "returns a complex (hence 'widget-ish') view that lets the user subscribe to different data-sources
  and shows the most recent value of each subscribed source.

  ---

  - uuid : (string) uuid that uniquey identifies this widget-ish in the system

  "
  [uuid]
  (let [layout (re-frame/dispatch [::events/layout uuid])]

    (re-frame/dispatch [::events/subscribe-to #{:number :string}])

    (fn []
      [:div {:style {:width "30%" :border "solid" :border-width "2px"}}
       [:h3 {:style {:color :white :background-color :orange}} uuid]

       [lookup-control uuid]

       [:div
        [number-control]
        [string-control]]

       [subscription-error-control]])))

