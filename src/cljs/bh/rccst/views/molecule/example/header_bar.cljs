(ns bh.rccst.views.molecule.example.header-bar
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]

            [bh.rccst.ui-component.header-bar :as header-bar]))


(defn example []
  (acu/demo "Header Bar"
    "A stylized header for an applications. Includes the version number provided by the server."
    [layout/centered
     [header-bar/header-bar]]
    '[]))
