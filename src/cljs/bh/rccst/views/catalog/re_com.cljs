(ns bh.rccst.views.catalog.re-com
  (:require [bh.rccst.views.catalog.example.table :as table]
            [bh.rccst.views.catalog.example.alert-box :as alert-box]
            [bh.rccst.views.catalog.example.line :as line]))



(defn catalog
  []
  [:div

   [table/example]
   [alert-box/example]
   [line/example]])
