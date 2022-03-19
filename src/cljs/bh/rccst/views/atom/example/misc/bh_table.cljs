(ns bh.rccst.views.atom.example.misc.bh-table
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [bh.rccst.ui-component.atom.bh.table :as table]))


(defn example []
  (acu/demo "Basic Table"
    "Table using HTML tags

> No meta-data"
    [layout/centered {:extra-classes :width-50}
     [table/table :data table/sample-data]]
    '[table/table :data table/sample-data]))

