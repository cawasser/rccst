(ns bh.rccst.views.atom.example.chart.treemap-chart
  (:require [bh.rccst.ui-component.atom.chart.treemap-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "treemap-chart-demo"]
    [example/component-example
     :title "Treemap Chart"
     :container-id container-id
     :description "A simple Treemap Chart built using [Recharts](https://recharts.org/en-US/api/Treemap)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id "treemap-chart")
     :source-code chart/source-code]))

