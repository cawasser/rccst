(ns bh.rccst.views.molecule.example.composite.grid-widget
  (:require [bh.rccst.ui-component.molecule.composite.coverage-plan :as plan]
            [bh.rccst.ui-component.molecule.grid-widget :as grid]
            [bh.rccst.ui-component.utils.helpers :as h]
            [woolybear.ad.catalog.utils :as acu]
            [bh.rccst.subs :as subs]
            [re-frame.core :as re-frame]
            [re-com.core :as rc]
            [woolybear.ad.layout :as layout]))


(defn example []
  (let [container-id     "widget-grid-demo"
        logged-in?       (re-frame/subscribe [::subs/logged-in?])
        pub-sub-started? (re-frame/subscribe [::subs/pub-sub-started?])]

    (if (not @logged-in?)
      (re-frame/dispatch [:bh.rccst.events/login "string" "string"]))

    (fn []
      (if (and @logged-in? @pub-sub-started?)
        (acu/demo "Widget using a Grid for layout"
          "This experiment uses a GRID to layout the various ui components that make up the 'composite'"
          [layout/frame
           [grid/component
            :data plan/ui-definition
            :component-id (h/path->keyword container-id "grid-widget")
            :container-id container-id]])))))
