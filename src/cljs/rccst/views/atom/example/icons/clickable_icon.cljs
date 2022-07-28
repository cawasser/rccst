(ns rccst.views.atom.example.icons.clickable-icon
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.icons :as icons]))


(defn example []
  (acu/demo "Clickable Icons"
    "Watch the JS console for messages when clicking icons."
    [containers/bar
     [icons/icon {:icon     "comment"
                  :on-click (fn [_] (js/console.log "Dispatched %o.", [:icon-demo/clicked :comment-icon]))}]
     [icons/icon {:icon     "gamepad"
                  :on-click (fn [_] (js/console.log "Dispatched %o.", [:icon-demo/clicked :gamepad-icon]))}]
     [icons/icon {:icon     "frog"
                  :on-click (fn [_] (js/console.log "Dispatched %o.", [:icon-demo/clicked :frog-icon]))}]
     [icons/icon {:icon     "helicopter"
                  :on-click (fn [_] (js/console.log "Dispatched %o.", [:icon-demo/clicked :helicopter-icon]))}]]

    '[containers/bar
      [icons/icon {:icon     "comment"
                   :on-click [:icon-demo/clicked :comment-icon]}]
      [icons/icon {:icon     "gamepad"
                   :on-click [:icon-demo/clicked :gamepad-icon]}]
      [icons/icon {:icon     "frog"
                   :on-click [:icon-demo/clicked :frog-icon]}]
      [icons/icon {:icon     "helicopter"
                   :on-click [:icon-demo/clicked :helicopter-icon]}]]))
