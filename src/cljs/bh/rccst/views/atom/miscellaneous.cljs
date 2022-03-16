(ns bh.rccst.views.atom.miscellaneous
  "Catalog and demonstrations of miscellaneous components that don't fit anywhere else."
  (:require [bh.rccst.views.atom.example.misc.resium.globe :as globe]
            [bh.rccst.views.atom.example.misc.hex-color-picker :as hex-picker]
            [bh.rccst.views.atom.example.misc.rgba-color-picker :as rgba-picker]
            [bh.rccst.views.atom.example.misc.two-d-globe :as two-d-globe]
            [bh.rccst.views.atom.example.misc.three-d-globe :as three-d-globe]
            [bh.rccst.views.atom.example.misc.basic-table :as basic-table]
            [bh.rccst.views.atom.example.misc.meta-basic-table :as meta-basic-table]))


(defn examples
  []

  [:div
   [basic-table/example]
   [meta-basic-table/example]
   [hex-picker/example]
   [rgba-picker/example]
   [two-d-globe/example]
   [three-d-globe/example]
   [globe/example]])
