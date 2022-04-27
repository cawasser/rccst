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
         ;;
         ;; NOTE: the :height MUST be specified here since the ResponsiveContainer down in bowels of the chart needs a height
         ;; in order to actually draw the Recharts components. just saying "100%" doesn't work, since the
         ;; that really means "be as big as you need" and ResponsiveContainer then doesn't know what to do.
         ;;
         [:div {:style {:width "100%" :height "800px"}}
          [grid/component
           :data (r/atom widget/ui-definition)
           :component-id (h/path->keyword container-id "widget")]]]
        widget/source-code))))
