(ns bh.rccst.views.atom.example.misc.meta-bh-table
  (:require [bh.rccst.ui-component.atom.bh.table :as table]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Basic Table with meta-data"
    "Table using HTML tags

> Now with meta-data!"
    [layout/centered {:extra-classes :width-50}
     [table/table :data table/sample-meta-data]]
    '[table/table :data table/sample-meta-data]))

