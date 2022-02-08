(ns bh.rccst.views.catalog.example.scatter-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [taoensso.timbre :as log]
            [bh.rccst.ui-component.atom.scatter-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.views.catalog.example.chart.utils :as utils]))


(defn example []
      (let [widget-id "scatter-chart-demo"]
           [example/example
            :title "Scatter Chart"
            :widget-id widget-id
            :description "A simple Scatter Chart built using [Recharts]()"
            :data (r/atom utils/triplet-data)
            :config (chart/config widget-id)
            :data-panel utils/tabular-data-panel
            :config-panel chart/config-panel
            :component-panel chart/component
            :source-code "[:> ScatterChart {:width 400 :height 400}
                            [:> CartesianGrid {:strokeDashArray (strokeDashArray config)}]
                            [:> XAxis {:type :dataKey :name :unit}]
                            [:> YAxis {:type :dataKey :name :unit}]
                            [:> Tooltip]
                            [:> Scatter {:name \"TempScatter\" :data @data :fill \"#8884d8\"}]]"]))
