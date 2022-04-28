(ns bh.rccst.views.molecules
  (:require [bh.rccst.ui-component.tabbed-pane.utils :as tab-utils]
            [bh.rccst.ui-component.navbar :as navbar]
            [bh.rccst.views.molecule.general :as general]
            [bh.rccst.views.molecule.all :as all]
            [bh.rccst.ui-component.tabbed-panel :as tabbed-panel]
            [woolybear.ad.layout :as layout]))



(def navbar [[:molecules/general "General" [general/catalog]]
             [:molecules/all "All" [all/catalog]]])


(defn page
  "Page to explore the various 'molecules' (more complex UI elements)"
  []

  [layout/page {:extra-classes :is-fluid}

   [layout/page-header {:extra-classes :is-fluid}
    [:h1.has-text-info "'Molecules'"]]

   [tabbed-panel/tabbed-panel
    :extra-classes {:extra-classes :is-fluid
                    :height "80vh"}

    :title ""
    :description ""
    :short-name "molecules"
    :children navbar
    :start-page :molecules/general]])



