(ns bh.rccst.views.login
  (:require [taoensso.timbre :as log]
            [reagent.core :as r]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]

            [bh.rccst.ui-component.button :as button]))


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
    [rc/v-box
     :src (rc/at)
     :children [
                [rc/h-box :src (rc/at)
                 :gap "10px"
                 :align :center
                 :children [[:h4 {:style {:width "5%"}} "User"]
                            [rc/input-text :src (rc/at)
                             :placeholder "user name"
                             :on-change #(reset! user-id %)
                             :model user-id]]]
                [rc/h-box :src (rc/at)
                 :gap "10px"
                 :align :center
                 :children [[:h4 {:style {:width "5%"}} "Password"]
                            [rc/input-password :src (rc/at)
                             :placeholder "password"
                             :on-change #(reset! password %)
                             :model password]]]
                [rc/gap :size "20px"]

                [rc/h-box
                 :align :center
                 :children [[button/button "Register"
                             #(re-frame/dispatch
                                [:bh.rccst.events/register @user-id @password])]
                            [rc/gap :size "10px"]
                            [button/button "Login"
                             #(re-frame/dispatch
                                [:bh.rccst.events/login @user-id @password])]]]]]))

