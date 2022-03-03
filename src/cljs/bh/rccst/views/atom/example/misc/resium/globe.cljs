(ns bh.rccst.views.atom.example.misc.resium.globe
  (:require [bh.rccst.ui-component.atom.resium.globe :as globe]
            [woolybear.ad.catalog.utils :as acu]))

(defn example []
      (acu/demo "Resium Globe"
          [globe/globe]))
