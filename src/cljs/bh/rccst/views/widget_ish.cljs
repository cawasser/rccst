(ns bh.rccst.views.widget-ish
  (:require [re-frame.core :as re-frame]

            [bh.rccst.subs :as subs]
            [bh.rccst.events :as events]
            [bh.rccst.subscriptions :as subscriptions]))



(defn- lookup-control []
  (let [result (re-frame/subscribe [::subs/lookup])
        error (re-frame/subscribe [::subs/lookup-error])]
    [:div
     [:button.button {:on-click #(re-frame/dispatch [::events/lookup])} "Lookup"]
     [:div
      [:h3 "Result: " @result]
      [:h3 "Error: " @error]]]))


(defn- number-control []
  (let [number (re-frame/subscribe [::subs/source :number])]
    [:h3 "Number: " @number]))


(defn- string-control []
  (let [s (re-frame/subscribe [::subs/source :string])]
    [:h3 "String: " @s]))


(defn- subscribe-to-control [keyword child]
  (let [result (re-frame/subscribe [::subs/subscribed keyword])]
    [:div {:style {:margin "10px"}}
     [:button.button.is-primary
      {:on-click #(re-frame/dispatch [::events/subscribe-to keyword])}
      (str keyword)]
     [:h3 "Result:" (str @result)] child]))


(defn- subscription-error-control []
  (let [error (re-frame/subscribe [::subs/subscribe-error])]
    [:div
     [:h3 "Error:" @error]]))


(defn view []
  (let [name    (re-frame/subscribe [::subs/name])
        version (re-frame/subscribe [::subs/version])]
    (fn []
      [:div {:style {:width "75%"}}
       [:div {:style {:display :flex
                      :margin  "10px"}}
        [:h1 "Hello from " @name]
        [:h3 "version: " @version]]

       [lookup-control]

       [:div {:style {:display :flex}}
        [subscribe-to-control :number [number-control]]
        [subscribe-to-control :string [string-control]]]

       [subscription-error-control]])))

