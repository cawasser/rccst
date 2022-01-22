(ns bh.rccst.views.catalog.miscellaneous
  "Catalog and demonstrations of miscellaneous components that don't fit anywhere else."
  (:require [bh.rccst.views.catalog.example.hex-color-picker :as hex-picker]
            [bh.rccst.views.catalog.example.rgba-color-picker :as rgba-picker]
            [bh.rccst.views.catalog.example.two-d-globe :as two-d-globe]
            [bh.rccst.views.catalog.example.three-d-globe :as three-d-globe]))


(defn catalog
  []

  [:div
   [hex-picker/example]
   [rgba-picker/example]
   [two-d-globe/example]
   [three-d-globe/example]])