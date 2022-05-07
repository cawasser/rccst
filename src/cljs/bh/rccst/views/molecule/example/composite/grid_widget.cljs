(ns bh.rccst.views.molecule.example.composite.grid-widget
  (:require [bh.rccst.ui-component.molecule.composite.coverage-plan :as plan]
            [bh.rccst.ui-component.molecule.grid-container :as grid]
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
      (re-frame/dispatch [:bh.rccst.events/login "test-user" "test-pwd"]))

    (fn []
      (if (and @logged-in? @pub-sub-started?)
        (acu/demo "Coverage Plan using a Grid for layout"
          "This experiment uses a GRID to layout the various UI components that make up the 'composite'.  Constructs 'coverage-plan', drawing the layout from `:grid-layout` which provides X/Y/W/H for each component on the widget's internal grid."
          [layout/frame
           [grid/component
            :data plan/ui-definition
            :component-id (h/path->keyword container-id "grid-widget")
            :container-id container-id
            :resizable true
            :tools true]])
        (acu/demo
          "Coverage Plan"
          [rc/alert-box :src (rc/at)
           :alert-type :info
           :heading "Waiting for (demo) Log-in"])))))
