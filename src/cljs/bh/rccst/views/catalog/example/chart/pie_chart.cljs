(ns bh.rccst.views.catalog.example.chart.pie-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.chart.pie-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))


(defn example []
  (let [widget-id "pie-chart-demo"]
    [example/component-example
     :title "Pie Chart"
     :widget-id widget-id
     :description       "Pie Chart with a default fill for each slice. Each slice is sized correctly and labeled with the value, but
      they are all the same color.

> See `Colored Pie Chart` for an example of how to get the slices to be different colors."
     :data chart/sample-data
     :component chart/component
     :component-id (str widget-id "/pie-chart")
     :source-code chart/source-code]))