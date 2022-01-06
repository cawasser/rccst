(ns bh.rccst.views.login
  (:require [taoensso.timbre :as log]
            [reagent.core :as r]
            [re-frame.core :as re-frame]

            [bh.rccst.ui-component.text-input :as input]))


(defn view
  "returns a simple 'login' page (id/password). Self-contained.

  ---

  > See also:
  >
  > [html input](https://www.w3schools.com/tags/tag_input.asp)
  "
  []
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
      [:button.button.is-primary {:on-click #(re-frame/dispatch [:bh.rccst.events/register @user-id @password])} "Register"]
      [:button.button.is-primary {:on-click #(re-frame/dispatch [:bh.rccst.events/login @user-id @password])} "Login"]]]))
