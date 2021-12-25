(ns bh.rccst.views
  (:require
   [re-frame.core :as re-frame]
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


(defn- subscribe-to-control [keyword]
  (let [result (re-frame/subscribe [::subs/subscribed keyword])]
    [:div {:style {:margin "10px"}}
     [:button.button.is-primary
      {:on-click #(re-frame/dispatch [::events/subscribe-to keyword])}
      (str keyword)]
     [:h3 "Result:" (str @result)]]))


(defn- subscription-error-control []
  (let [error (re-frame/subscribe [::subs/subscribe-error])]
    [:div
     [:h3 "Error:" @error]]))


(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        counter (re-frame/subscribe [::subs/counter])
        s (re-frame/subscribe [::subs/set])]
    (fn []
      [:div
       [:h1 "Hello from " @name]
       [:h3 "Counter: " @counter]
       [:button.button {:on-click #(re-frame/dispatch [::subscriptions/start])} "Start"]
       [:h3 "Last 3: " (str @s)]

       [number-control]
       [string-control]
       [lookup-control]
       [:div {:style {:display :flex}}
        [subscribe-to-control :dummy]
        [subscribe-to-control :something-else]
        [subscribe-to-control :a-third-thing]]
       [subscription-error-control]])))


; some things for the repl
(comment
  (re-frame/dispatch [::events/initialize-db])

  @re-frame.db/app-db

  (re-frame/dispatch [::events/add-to-set 7])

  ())