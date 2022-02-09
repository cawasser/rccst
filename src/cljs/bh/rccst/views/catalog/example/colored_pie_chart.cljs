(ns bh.rccst.views.catalog.example.colored-pie-chart
  (:require [reagent.core :as r]

            [bh.rccst.ui-component.atom.colored-pie-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))



(defn example []
  (let [widget-id "colored-pie-chart-demo"]
    [example/example
     :title "Colored Pie Chart"
     :widget-id widget-id
     :description "Pie Chart with different colors for each slice. This requires embedding `Cell` elements
inside the `Pie` element.

> Note: Recharts supports embedding `Cell` in a variety of other chart types, for example BarChart"
     :data (r/atom utils/paired-data)
     :config (chart/config widget-id)
     :data-panel utils/tabular-data-panel
     :config-panel chart/config-panel
     :component-panel chart/component
     :source-code chart/source-code]))

