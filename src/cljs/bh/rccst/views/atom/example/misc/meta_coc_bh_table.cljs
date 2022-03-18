(ns bh.rccst.views.atom.example.misc.meta-coc-bh-table
  (:require [bh.rccst.ui-component.atom.bh.table :as table]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Basic Table with meta-data, showing available Chain-of-Custody data"
    "Table using HTML tags

> And with Chain-of-Custody!!"
    [layout/centered {:extra-classes :width-50}
     [table/table :data table/sample-meta-coc-data]]
    '[table/table :data table/sample-meta-coc-data]))

