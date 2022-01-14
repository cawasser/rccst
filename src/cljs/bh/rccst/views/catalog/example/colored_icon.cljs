(ns bh.rccst.views.catalog.example.colored-icon
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.icons :as icons]))


(defn example []
  (acu/demo "Colored Icons"
            "FontAwesome icons are treated as text by the browser, so you can use
            Bulma text classes like has-text-success to color your icons. These
            examples also show off grouping small icons in a 'bar'"
            [containers/bar
             [icons/icon {:icon "check" :extra-classes :has-text-success}]
             [icons/icon {:icon "edit" :extra-classes :has-text-danger}]
             [icons/icon {:icon "save" :extra-classes :has-text-info}]
             [icons/icon {:icon "share" :extra-classes :has-text-primary}]]
            '[containers/bar
              [icons/icon {:icon "check" :extra-classes :has-text-success}]
              [icons/icon {:icon "edit" :extra-classes :has-text-danger}]
              [icons/icon {:icon "save" :extra-classes :has-text-info}]
              [icons/icon {:icon "share" :extra-classes :has-text-primary}]]))

