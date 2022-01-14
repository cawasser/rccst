(ns bh.rccst.views.catalog.example.icon-button
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.icons :as icons]
            [woolybear.ad.buttons :as buttons]))

; TODO: should this be mode to woolybear/utils?
;
(defn- tattle
      "Utility for making a fake dispatcher that just reports to the js/console"
      [evt]
      (fn [_]
          (js/console.log "Dispatched %o" evt)))

(defn example []
      (acu/demo "Buttons with icons"
                "By default, buttons take simple strings as child elements, which are rendered
                as the button label. But you're not limited to strings--button labels can include
                icons as well."
                [layout/padded {:extra-classes :level}
                 [buttons/button {:on-click (tattle [:button-demo/click :icon-edit-button])}
                  [icons/icon {:icon "edit"}] "Edit"]
                 [buttons/button {:on-click (tattle [:button-demo/click :icon-share-button])}
                  [icons/icon {:icon "share"}] "Share"]
                 [buttons/button {:on-click (tattle [:button-demo/click :icon-twitter-button])}
                  [icons/icon {:icon "twitter" :brand? true :extra-classes :has-text-info}] "Tweet"]]
                '[layout/padded {:extra-classes :level}
                  [buttons/button {:on-click (tattle [:button-demo/click :icon-edit-button])}
                   [icons/icon {:icon "edit"}] "Edit"]
                  [buttons/button {:on-click (tattle [:button-demo/click :icon-share-button])}
                   [icons/icon {:icon "share"}] "Share"]
                  [buttons/button {:on-click (tattle [:button-demo/click :icon-twitter-button])}
                   [icons/icon {:icon "twitter" :brand? true :extra-classes :has-text-info}] "Tweet"]]))
