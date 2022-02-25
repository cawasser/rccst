(ns bh.rccst.views.atom.example.chart.colored-pie-chart
  (:require [bh.rccst.ui-component.atom.chart.colored-pie-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [taoensso.timbre :as log]))



(defn example []
  (let [widget-id "colored-pie-chart-demo"]
    [example/component-example
     :title "Colored Pie Chart"
     :widget-id widget-id
     :description "Pie Chart with different colors for each slice. This requires embedding `Cell` elements
inside the `Pie` element.

> Note: Recharts supports embedding `Cell` in a variety of other chart types, for example BarChart"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (str widget-id "/colored-pie-chart")
     :source-code chart/source-code]))

