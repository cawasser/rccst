(ns bh.rccst.views.catalog.miscellaneous
  "Catalog and demonstrations of miscellaneous components that don't fit anywhere else."
  (:require [bh.rccst.views.catalog.example.hex-color-picker :as hex-picker]
            [bh.rccst.views.catalog.example.rgba-color-picker :as rgba-picker]))


(defn catalog
  []

  [:div
   [hex-picker/example]
   [rgba-picker/example]])