(ns rccst.views.atom.containers
  "Catalog and demonstrations of available container components."
  (:require [rccst.views.atom.example.container.v-scroll-panel :as v-scroll-panel]
            [rccst.views.atom.example.container.shy-block :as shy-block]
            [rccst.views.atom.example.container.flex-panel :as flex-panel]
            [rccst.views.atom.example.container.navbar :as navbar]))


; TODO: do we need this in an RCCST namespace, or can we just grab it from woolybear/catalog?

(defn examples
  []
  [:div
   [v-scroll-panel/example]
   [shy-block/example]
   [flex-panel/example]
   [navbar/example]])

