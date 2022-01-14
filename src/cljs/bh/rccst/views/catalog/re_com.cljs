(ns bh.rccst.views.catalog.re-com
  (:require [bh.rccst.views.catalog.example.table :as table]
            [bh.rccst.views.catalog.example.checkbox-re-com :as checkbox]
            [bh.rccst.views.catalog.example.progress-bar :as progress-bar]))



(defn catalog
  []
  [:div
   [table/example]

   [checkbox/example]

   [progress-bar/example]])