(ns bh.rccst.views.catalog.re-com
  (:require [bh.rccst.views.catalog.example.table :as table]
            [bh.rccst.views.catalog.example.checkbox-re-com :as checkbox]))




(defn catalog
  []
  [:div

   [table/example]

   [checkbox/example]])
