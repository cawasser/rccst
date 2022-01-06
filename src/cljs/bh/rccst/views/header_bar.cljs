(ns bh.rccst.views.header-bar
  (:require [re-frame.core :as re-frame]

            [bh.rccst.subs :as subs]
            [bh.rccst.events :as events]))



(defn view []
  (let [name    (re-frame/subscribe [::subs/name])
        version (re-frame/subscribe [::subs/version])]
    [:div {:style {:display :flex
                   :margin  "10px"
                   :border "solid" :border-width "5px" :border-color :blue}}
     [:h1 "Hello from " @name]
     [:h3 "version: " @version]
     [:button.button {:on-click #(re-frame/dispatch [::events/logoff])} "Log Off"]]))