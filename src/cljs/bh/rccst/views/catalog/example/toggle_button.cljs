(ns bh.rccst.views.catalog.example.toggle-button
  (:require [reagent.ratom :as ratom]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.buttons :as buttons]))


(defn example []
      (let [demo-toggle-state-1 (ratom/atom false)
            demo-toggle-state-2 (ratom/atom false)]
           (acu/demo "Toggle button"
                           "This component works similar to a checkbox, in that it has an \"on\" state and
                     and \"off\" state, representing some value in the app-db. Use the :subscribe-to-on?
                     option to pass the current state to the toggle button, and the :class-for-on option
                     to pass the CSS class to add to the toggle button when the current state is \"on.\"
                     You can also pass the :class-for-off option to specify a CSS class to be added when
                     the toggle button is \"off,\" however it is usually sufficient to just leave the
                     toggle button in its default state when not active. Pass a (required) :on-click option
                     to specify an event to dispatch when the button is clicked. It is up to the event
                     handler to update the on/off value so that the toggle button displays in the correct
                     state. All other standard button options also apply to the toggle button (including
                     :extra-classes and :subscribe-to-classes)."
                     [layout/padded {:extra-classes :level}
                      [buttons/toggle-button {:subscribe-to-on? demo-toggle-state-1
                                              :class-for-on :is-primary
                                              :on-click (fn [_] (swap! demo-toggle-state-1 not))}
                       "Click to toggle 1"]
                      [buttons/toggle-button {:subscribe-to-on? demo-toggle-state-2
                                              :class-for-on :is-primary
                                              :on-click (fn [_] (swap! demo-toggle-state-2 not))}
                       "Click to toggle 2"]]
                     '[layout/padded {:extra-classes :level}
                       [buttons/toggle-button {:subscribe-to-on? [:toggle-demo/button 1]
                                               :class-for-on :is-primary
                                               :on-click [:toggle-demo/click 1]}
                        "Click to toggle 1"]
                       [buttons/toggle-button {:subscribe-to-on? [:toggle-demo/button 2]
                                               :class-for-on :is-primary
                                               :on-click [:toggle-demo/click 2]}
                        "Click to toggle 2"]])))