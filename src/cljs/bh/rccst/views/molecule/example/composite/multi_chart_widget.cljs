(ns bh.rccst.views.molecule.example.composite.multi-chart-widget
  (:require [bh.rccst.ui-component.molecule.composite.multi-chart :as widget]
            [bh.rccst.ui-component.molecule.grid-widget :as grid]
            [bh.rccst.ui-component.utils.helpers :as h]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.views.molecule.example.composite.multi-chart-widget")


(defn example []
  (let [container-id "multi-chart-widget"]

    (fn []
      (acu/demo "Multiple Charts in a Widget"
        "This example provides a 'widget' (collection of UI Components) organized into a digraph (Event Model) that
          describes the flow of data from sources (remote or local) into and out-of the UI."
        [layout/frame
         [:div {:style {:width "1000px" :height "800px"}}
          [grid/component
           :data (r/atom widget/ui-definition)
           :component-id (h/path->keyword container-id "widget")]]]
        widget/source-code))))
