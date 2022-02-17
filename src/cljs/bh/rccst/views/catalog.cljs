(ns bh.rccst.views.catalog
  (:require [bh.rccst.events :as events]

            [bh.rccst.views.catalog.all :as all-demo]
            [bh.rccst.views.catalog.buttons :as buttons-demo]
            [bh.rccst.views.catalog.cards :as cards-demo]
            [bh.rccst.views.catalog.charts :as charts-demo]

            [bh.rccst.views.catalog.containers :as containers-demo]
            [bh.rccst.views.catalog.diagrams :as diagrams-demo]
            [bh.rccst.views.catalog.forms :as forms-demo]
            [bh.rccst.views.catalog.icons :as icons-demo]
            [bh.rccst.views.catalog.layouts :as layout-demo]
            [bh.rccst.views.catalog.miscellaneous :as misc]
            [bh.rccst.views.catalog.re-com :as re-com-demo]
            [bh.rccst.views.catalog.experimental :as experimental]
            [taoensso.timbre :as log]

            [bh.rccst.ui-component.tabbed-panel :as tabbed-panel]))


(def catalog-navbar [[:atoms/layouts "Layout" [layout-demo/catalog]]
                     [:atoms/containers "Containers" [containers-demo/catalog]]
                     [:atoms/cards "Cards" [cards-demo/catalog]]
                     [:atoms/charts "Charts" [charts-demo/catalog]]
                     [:atoms/diagrams "Diagrams" [diagrams-demo/catalog]]
                     [:atoms/icons "Icons / Images" [icons-demo/catalog]]
                     [:atoms/buttons "Buttons" [buttons-demo/catalog]]
                     [:atoms/forms "Forms" [forms-demo/catalog]]
                     [:atoms/re-com "Re-com" [re-com-demo/catalog]]
                     [:atoms/misc "Misc." [misc/catalog]]
                     [:atoms/experimental "Experimental" [experimental/catalog]]
                     [:atoms/all "All" [all-demo/catalog]]])


(defn page
  "Top-level AD Catalog page"
  []

  [tabbed-panel/tabbed-panel
   :title "'Atom' Catalog (refactor)"
   :short-name "atoms"
   :description "Based upon [_Atomic Design_](https://bradfrost.com/blog/post/atomic-web-design/) by Brad Frost"
   :children catalog-navbar
   :start-panel :atoms/layouts])



