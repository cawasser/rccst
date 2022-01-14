(ns bh.rccst.views.catalog.example.brand-icon
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.icons :as icons]))


(defn example []
  (acu/demo "Brand Icons"
            "Brand icons from the (free) FontAwesome collection."
            [containers/bar
             [icons/icon {:icon "google" :brand? true}]
             [icons/icon {:icon "jenkins" :brand? true}]
             [icons/icon {:icon "facebook" :brand? true}]
             [icons/icon {:icon "amazon" :brand? true}]]

            '[containers/bar
              [icons/icon {:icon "google" :brand? true}]
              [icons/icon {:icon "jenkins" :brand? true}]
              [icons/icon {:icon "facebook" :brand? true}]
              [icons/icon {:icon "amazon" :brand? true}]]))
