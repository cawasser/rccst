(ns rccst.views
  (:require [bh.ui-component.atom.bh.tabbed-panel :as tabbed-panel]
            [rccst.views.atoms :as atoms]
            [rccst.views.giants :as giants]
            [rccst.views.molecules :as molecules]
            [rccst.views.organisms :as organisms]
            [rccst.views.technologies :as tech]
            [rccst.views.templates :as templates]
            [rccst.views.welcome :as welcome]
            [woolybear.ad.layout :as layout]))


(def navbar [[:app-bar/welcome "Welcome!" [welcome/page]]
             [:app-bar/tech "Technologies" [tech/page]]
             [:app-bar/atoms "'Atoms'" [atoms/page]]
             [:app-bar/molecules "'Molecules'" [molecules/page]]
             [:app-bar/organisms "'Organisms'" [organisms/page]]
             [:app-bar/templates "'Templates'" [templates/page]]
             [:app-bar/giants "Giants" [giants/view]]])


(defn view
  []

  [layout/page {:extra-classes :is-fluid}

   [tabbed-panel/tabbed-panel
    :extra-classes {:extra-classes :is-fluid
                    :width "90%"
                    :height        "95vh"}
    :title ""
    :short-name "catalog"
    :description ""
    :children navbar
    :start-panel :app-bar/welcome]
   [:p "UI Component Catalog"]])

