(ns bh.rccst.views.atom.miscellaneous
  "Catalog and demonstrations of miscellaneous components that don't fit anywhere else."
  (:require [bh.rccst.views.atom.example.misc.hex-color-picker :as hex-picker]
            [bh.rccst.views.atom.example.misc.rgba-color-picker :as rgba-picker]
            [bh.rccst.views.atom.example.misc.two-d-globe :as two-d-globe]
            [bh.rccst.views.atom.example.misc.three-d-globe :as three-d-globe]))


(defn examples
  []

  [:div
   [hex-picker/example]
   [rgba-picker/example]
   [two-d-globe/example]
   [three-d-globe/example]])