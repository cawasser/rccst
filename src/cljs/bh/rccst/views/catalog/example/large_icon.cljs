(ns bh.rccst.views.catalog.example.large-icon
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.icons :as icons]))


(defn example []
  (acu/demo "Large Icons"
            [containers/bar
             [icons/icon {:icon "backward" :size :large}]
             [icons/icon {:icon "stop" :size :large}]
             [icons/icon {:icon "play" :size :large}]
             [icons/icon {:icon "forward" :size :large}]]
            '[containers/bar
              [icons/icon {:icon "backward" :size :large}]
              [icons/icon {:icon "stop" :size :large}]
              [icons/icon {:icon "play" :size :large}]
              [icons/icon {:icon "forward" :size :large}]]))

