(ns bh.rccst.views.atom.example.astrouxds.table
  (:require [bh.rccst.ui-component.atom.astrouxds.table :as table]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]))

(defn example []
  (acu/demo
    "Table"
    [rc/h-box :src (rc/at)
     :gap "50px"
     :children [[table/table]]]))
