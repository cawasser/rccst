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

       [lookup-control]])))


; some things for the repl
(comment
  (re-frame/dispatch [::events/initialize-db])

  @re-frame.db/app-db

  (re-frame/dispatch [::events/add-to-set 7])

  ())