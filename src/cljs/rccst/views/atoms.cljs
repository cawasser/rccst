(ns rccst.views.atoms
  (:require [bh.ui-component.atom.bh.tabbed-panel :as tabbed-panel]
            [rccst.views.atom.all :as all-demo]
            [rccst.views.atom.astrouxds :as astro-demo]
            [rccst.views.atom.buttons :as buttons-demo]
            [rccst.views.atom.cards :as cards-demo]

            [rccst.views.atom.charts :as charts-demo]
            [rccst.views.atom.containers :as containers-demo]
            [rccst.views.atom.diagrams :as diagrams-demo]
            [rccst.views.atom.experimental :as experimental]
            [rccst.views.atom.forms :as forms-demo]
            [rccst.views.atom.icons :as icons-demo]
            [rccst.views.atom.layouts :as layout-demo]
            [rccst.views.atom.miscellaneous :as misc]
            [rccst.views.atom.re-com :as re-com-demo]

            [woolybear.ad.layout :as layout]))


(def atom-navbar [[:atoms/layouts "Layout" [layout-demo/examples]]
                  [:atoms/containers "Containers" [containers-demo/examples]]
                  [:atoms/cards "Cards" [cards-demo/examples]]
                  [:atoms/charts "Charts" [charts-demo/page]]
                  [:atoms/diagrams "Diagrams" [diagrams-demo/examples]]
                  [:atoms/icons "Icons / Images" [icons-demo/examples]]
                  [:atoms/buttons "Buttons" [buttons-demo/examples]]
                  [:atoms/forms "Forms" [forms-demo/examples]]
                  [:atoms/re-com "Re-com" [re-com-demo/examples]]
                  [:atoms/astro "AstroUXDS" [astro-demo/examples]]
                  [:atoms/misc "Misc." [misc/examples]]
                  [:atoms/experimental "Experimental" [experimental/examples]]
                  [:atoms/all "All" [all-demo/examples]]])


(defn page
  "Top-level AD Atom page"
  []

  [layout/page {:extra-classes :is-fluid}

   ;[layout/page-header {:extra-classes :is-fluid}
   ; [:h1.has-text-info "'Atoms'"]]

   [tabbed-panel/tabbed-panel
    :extra-classes {:extra-classes :is-fluid
                    :height        "85vh"}
    :title ""
    :short-name "atoms"
    :description ""
    :children atom-navbar
    :start-panel :atoms/layouts]])



