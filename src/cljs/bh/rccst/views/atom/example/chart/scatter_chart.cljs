(ns bh.rccst.views.atom.example.chart.scatter-chart
  (:require [bh.rccst.ui-component.atom.chart.scatter-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [taoensso.timbre :as log]))


(defn example []
  (let [widget-id "scatter-chart-demo"]
    [example/component-example
     :title "Scatter Chart"
     :widget-id widget-id
     :description "A simple Scatter Chart built using [Recharts]()"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (str widget-id "/scatter-chart")
     :source-code chart/source-code]))
