(ns bh.rccst.views.atom.containers
  "Catalog and demonstrations of available container components."
  (:require [bh.rccst.views.atom.example.container.v-scroll-panel :as v-scroll-panel]
            [bh.rccst.views.atom.example.container.shy-block :as shy-block]
            [bh.rccst.views.atom.example.container.flex-panel :as flex-panel]
            [bh.rccst.views.atom.example.container.navbar :as navbar]))


; TODO: do we need this in an RCCST namespace, or can we just grab it from woolybear/catalog?
; TODO: should this be a 'utils' function instead?
;

(defn examples
  []
  [:div
   [v-scroll-panel/example]
   [shy-block/example]
   [flex-panel/example]
   [navbar/example]])

