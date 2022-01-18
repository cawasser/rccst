(ns bh.rccst.views.catalog.containers
  "Catalog and demonstrations of available container components."
  (:require [bh.rccst.views.catalog.example.v-scroll-panel :as v-scroll-panel]
            [bh.rccst.views.catalog.example.shy-block :as shy-block]
            [bh.rccst.views.catalog.example.flex-panel :as flex-panel]
            [bh.rccst.views.catalog.example.navbar :as navbar]))


; TODO: do we need this in an RCCST namespace, or can we just grab it from woolybear/catalog?
; TODO: should this be a 'utils' function instead?
;

(defn catalog
  []
  [:div

   [v-scroll-panel/example]

   [shy-block/example]

   [flex-panel/example]

   [navbar/example]])

