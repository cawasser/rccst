(ns bh.rccst.views.catalog.example.specialized-button
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.buttons :as buttons]))

; TODO: should this be mode to woolybear/utils?
;
(defn- tattle
      "Utility for making a fake dispatcher that just reports to the js/console"
      [evt]
      (fn [_]
          (js/console.log "Dispatched %o" evt)))


(defn example []
      (acu/demo "Specialized buttons"
                "The OK, Save, Cancel, Delete, and Close buttons are all simple wrappers around the simple
                [buttons/button] component, each one supplying a default label and related classes so that
                the indicated button can be implemented with the minimum of ceremony."
                [layout/padded {:extra-classes :level}
                 [buttons/ok-button {:on-click (tattle [:button-demo/click :ok-button])}]
                 [buttons/save-button {:on-click (tattle [:button-demo/click :save-button])}]
                 [buttons/cancel-button {:on-click (tattle [:button-demo/click :cancel-button])}]
                 [buttons/delete-button {:on-click (tattle [:button-demo/click :delete-button])}]
                 [buttons/close-button {:on-click (tattle [:button-demo/click :close-button])}]]

                '[layout/padded {:extra-classes :level}
                  [buttons/ok-button {:on-click (tattle [:button-demo/click :ok-button])}]
                  [buttons/save-button {:on-click (tattle [:button-demo/click :save-button])}]
                  [buttons/cancel-button {:on-click (tattle [:button-demo/click :cancel-button])}]
                  [buttons/delete-button {:on-click (tattle [:button-demo/click :delete-button])}]
                  [buttons/close-button {:on-click (tattle [:button-demo/click :close-button])}]]))
