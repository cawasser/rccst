(ns bh.rccst.views.header-bar
  (:require [re-frame.core :as re-frame]

            [bh.rccst.subs :as subs]
            [bh.rccst.events :as events]))



(defn view []
  (let [name (re-frame/subscribe [::subs/name])
        version (re-frame/subscribe [::subs/version])
        uuid (re-frame/subscribe [::subs/uuid])]
    [:div {:style {:margin "10px"
                   :border "solid" :border-width "5px" :border-color :blue}}
     [:h1 "Hello from " @name]
     [:div {:style {:display :flex}}
      [:h5.is-small "version: " @version]
      [:h6.is-small @uuid]
      [:button.button.is-primary {:on-click #(re-frame/dispatch [::events/logoff])} "Log Off"]]]))