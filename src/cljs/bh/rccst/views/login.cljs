(ns bh.rccst.views.login
  (:require [taoensso.timbre :as log]
            [reagent.core :as r]
            [re-frame.core :as re-frame]

            [bh.rccst.events :as events]
            [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.text-input :as input]))


(defn view []
  (let [user-id (r/atom "")
        password (r/atom "")]
    [:div
     [:div {:style {:display :flex}}
      [:h3 "User"]
      [input/input :text "user name" user-id]]
     [:div {:style {:display :flex}}
      [:h3 "Password"]
      [input/input :text "password" password]]

     [:div {:style {:display :flex}}
      [:button.button.is-primary {:on-click #(re-frame/dispatch [::events/register @user-id @password])} "Register"]
      [:button.button.is-primary {:on-click #(re-frame/dispatch [::events/login @user-id @password])} "Login"]]]))
