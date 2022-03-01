(ns bh.rccst.views.atom.example.chart.pie-chart
  (:require [bh.rccst.ui-component.atom.chart.pie-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "pie-chart-demo"]
    [example/component-example
     :title "Pie Chart"
     :widget-id container-id
     :description "Pie Chart with a default fill for each slice. Each slice is sized correctly and labeled with the value, but
      they are all the same color.

Uses [Recharts](https://recharts.org/en-US/api/PieChart)

> See `Colored Pie Chart` for an example of how to get the slices to be different colors."
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id "pie-chart")
     :source-code chart/source-code]))
