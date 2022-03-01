(ns bh.rccst.views.atom.example.chart.line-chart
  (:require [bh.rccst.ui-component.atom.chart.line-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]))


(defn simple-example []
  (let [container-id "line-chart-demo"]
    [example/component-example
     :title "Line Chart"
     :widget-id container-id
     :description "A simple Line Chart built using [Recharts](https://recharts.org/en-US/api/LineChart)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id "line-chart")
     :source-code chart/source-code]))



