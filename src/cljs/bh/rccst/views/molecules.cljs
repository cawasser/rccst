(ns bh.rccst.views.molecules
  (:require [bh.rccst.ui-component.tabbed-pane.utils :as tab-utils]
            [bh.rccst.ui-component.navbar :as navbar]
            [bh.rccst.views.molecule.general :as general]
            [bh.rccst.views.molecule.all :as all]

            [bh.rccst.ui-component.tabbed-panel :as tabbed-panel]))



(def navbar [[:molecules/general "General" [general/catalog]]
             [:molecules/all "All" [all/catalog]]])


(defn page
  "Page to explore the various 'molecules' (more complex UI elements)"
  []

  [tabbed-panel/tabbed-panel
   :extra-classes {:extra-classes :is-fluid
                   :height "90vh"}

   :title "'Molecule' Catalog"
   :description "Based upon [_Atomic Design_](https://bradfrost.com/blog/post/atomic-web-design/) by Brad Frost"
   :short-name "molecules"
   :children navbar
   :start-page :molecules/general])



