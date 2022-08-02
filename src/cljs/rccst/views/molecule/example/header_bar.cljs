(ns rccst.views.molecule.example.header-bar
  (:require [bh.ui-component.atom.re-com.button :as button]
            [bh.ui-component.atom.re-com.header-bar :as header-bar]
            [rccst.events :as events]
            [rccst.subs :as subs]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (let [version (re-frame/subscribe [::subs/version])
        uuid    (re-frame/subscribe [::subs/uuid])]

    (acu/demo "Header Bar"
      "A stylized header for an applications. Includes the version number provided by the server."
      [layout/centered

       [header-bar/header-bar
        :children [[:h1 "RCCST"]
                   [rc/h-box :src (rc/at)
                    :gap "10px"
                    :align :center
                    :children [[:h5.is-small "version: " (or @version "unknown")]
                               [:h5.is-small (or @uuid "none")]
                               [button/button
                                "Log Off"
                                #(re-frame/dispatch [::events/logoff]) "Log Off"]]]]]]
      '[])))


