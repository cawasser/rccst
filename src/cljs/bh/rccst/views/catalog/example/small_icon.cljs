(ns bh.rccst.views.catalog.example.small-icon
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.icons :as icons]))


(defn example []
  (acu/demo "Small Icons"
            [containers/bar
             [icons/icon {:icon "check" :size :small}]
             [icons/icon {:icon "edit" :size :small}]
             [icons/icon {:icon "save" :size :small}]
             [icons/icon {:icon "share" :size :small}]]
            '[containers/bar
              [icons/icon {:icon "check" :size :small}]
              [icons/icon {:icon "edit" :size :small}]
              [icons/icon {:icon "save" :size :small}]
              [icons/icon {:icon "share" :size :small}]]))

