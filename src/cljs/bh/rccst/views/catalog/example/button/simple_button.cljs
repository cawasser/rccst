(ns bh.rccst.views.catalog.example.button.simple-button
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
  (acu/demo "Simple button"
    "The generic [buttons/button] component is the base component for a number of common,
    more-specialized buttons like the OK button, Save button, and Cancel button. Takes the
    standard extra-classes and subscribe-to-classes options, plus a required on-click
    option, which specifies the event to dispatch when the button is clicked."
    [layout/padded
     [buttons/button {:on-click (tattle
                                  [:button-demo/click :ooo/you-actually-clicked-it!])}
      "Click me!"]]
    '[layout/padded
      [buttons/button {:on-click [:button-demo/click :ooo/you-actually-clicked-it!]}]]))
