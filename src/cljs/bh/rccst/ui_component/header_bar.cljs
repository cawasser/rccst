(ns bh.rccst.ui-component.header-bar
  (:require [re-frame.core :as re-frame]
            [re-com.core :as rc]

            [bh.rccst.subs :as subs]
            [bh.rccst.events :as events]
            [bh.rccst.ui-component.button :as button]))



(defn header-bar
  "creates a nice, simple header for the UI, displaying the app title, the server version number,
  the uuid returned by the server that is assigned to this user, and a button to log out of the
  server.

  > See also:
  >
  > [re-com](https://github.com/Day8/re-com)
  "
  []
  (let [version (re-frame/subscribe [::subs/version])
        uuid (re-frame/subscribe [::subs/uuid])]
    (fn []
      [rc/h-box :src (rc/at)
       :width "90%"
       :style {:background-color :lightgray
               :border "solid"
               :border-width "5px"
               :border-color :blue
               :padding "10px"}
       :justify :between
       :children [[:h1 "RCCST"]
                  [rc/h-box :src (rc/at)
                   :gap "10px"
                   :align :center
                   :children [[:h5.is-small "version: " @version]
                              [:h5.is-small @uuid]
                              [button/button
                               "Log Off"
                               #(re-frame/dispatch [::events/logoff]) "Log Off"]]]]])))