(ns bh.rccst.views.atom.miscellaneous
  "Catalog and demonstrations of miscellaneous components that don't fit anywhere else."
  (:require [bh.rccst.views.atom.example.misc.resium.globe :as globe]
            [bh.rccst.views.atom.example.misc.hex-color-picker :as hex-picker]
            [bh.rccst.views.atom.example.misc.rgba-color-picker :as rgba-picker]
            [bh.rccst.views.atom.example.misc.two-d-globe :as two-d-globe]
            [bh.rccst.views.atom.example.misc.bh.bh-table :as bh-table]
            [bh.rccst.views.atom.example.misc.bh.meta-bh-table :as meta-bh-table]
            [bh.rccst.views.atom.example.misc.bh.meta-coc-bh-table :as meta-coc-bh-table]
            [bh.rccst.views.atom.example.misc.worldwind.globe :as ww-globe]
            [bh.rccst.views.atom.example.misc.bh.data-table :as data-table]))


(defn examples
  []

  [:div
   [data-table/example]
   [ww-globe/example]
   [globe/example]
   [bh-table/example]
   [meta-bh-table/example]
   [meta-coc-bh-table/example]
   [hex-picker/example]
   [rgba-picker/example]
   [two-d-globe/example]])

