(ns bh.rccst.views.atom.example.chart.scatter-chart
  (:require [bh.rccst.ui-component.atom.chart.scatter-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "scatter-chart-demo"]
    [example/component-example
     :title "Scatter Chart"
     :widget-id container-id
     :description "A simple Scatter Chart built using [Recharts](https://recharts.org/en-US/api/ScatterChart)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id "scatter-chart")
     :source-code chart/source-code]))
