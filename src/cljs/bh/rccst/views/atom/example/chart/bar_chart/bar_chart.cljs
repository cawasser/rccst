(ns bh.rccst.views.atom.example.chart.bar-chart.bar-chart
  (:require [bh.rccst.ui-component.atom.chart.bar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "bar-chart-demo"]
    [example/component-example
     :title "Bar Chart"
     :container-id container-id
     :description "A simple Bar Chart built using [Recharts](https://recharts.org/en-US/api/BarChart)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id "bar-chart")
     :source-code chart/source-code]))