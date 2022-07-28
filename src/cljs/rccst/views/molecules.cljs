(ns rccst.views.molecules
  (:require [bh.ui-component.tabbed-pane.utils :as tab-utils]
            [bh.ui-component.atom.bh.navbar :as navbar]
            [rccst.views.molecule.general :as general]
            [rccst.views.molecule.all :as all]
            [bh.ui-component.atom.bh.tabbed-panel :as tabbed-panel]
            [woolybear.ad.layout :as layout]))



(def navbar [[:molecules/general "General" [general/catalog]]
             [:molecules/all "All" [all/catalog]]])


(defn page
  "Page to explore the various 'molecules' (more complex UI elements)"
  []

  [layout/page {:extra-classes :is-fluid}

   ;[layout/page-header {:extra-classes :is-fluid}
   ; [:h1.has-text-info "'Molecules'"]]

   [tabbed-panel/tabbed-panel
    :extra-classes {:extra-classes :is-fluid
                    :height "85vh"}

    :title ""
    :description ""
    :short-name "molecules"
    :children navbar
    :start-page :molecules/general]])



