(ns bh.rccst.views.atom.example.icons.standard-icon
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.icons :as icons]))


(defn example []
  (acu/demo "Standard Icons"
    "Simple, [FontAwesome](https://fontawesome.com/v5.15/icons?d=gallery&p=1)-based icons. These examples also show off grouping small icons
    in a 'bar'"
    [containers/bar
     [icons/icon {:icon "check"}]
     [icons/icon {:icon "edit"}]
     [icons/icon {:icon "save"}]
     [icons/icon {:icon "share"}]]
    '[containers/bar
      [icons/icon {:icon "check"}]
      [icons/icon {:icon "edit"}]
      [icons/icon {:icon "save"}]
      [icons/icon {:icon "share"}]]))

