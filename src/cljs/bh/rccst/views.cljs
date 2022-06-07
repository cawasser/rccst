(ns bh.rccst.views
  (:require [bh.rccst.ui-component.tabbed-panel :as tabbed-panel]
            [bh.rccst.views.atoms :as atoms]
            [bh.rccst.views.giants :as giants]
            [bh.rccst.views.molecules :as molecules]
            [bh.rccst.views.organisms :as organisms]
            [bh.rccst.views.technologies :as tech]
            [bh.rccst.views.templates :as templates]
            [bh.rccst.views.welcome :as welcome]
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

