(ns bh.rccst.views.catalog.miscellaneous
  "Catalog and demonstrations of miscellaneous components that don't fit anywhere else."
  (:require [bh.rccst.views.catalog.example.color-picker :as picker]))


(defn catalog
  []

  [:div
   [picker/example]])