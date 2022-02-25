(ns bh.rccst.views.atom.example.chart.treemap-chart
  (:require [bh.rccst.ui-component.atom.chart.treemap-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [taoensso.timbre :as log]))


(defn example []
  (let [widget-id "treemap-chart-demo"]
    [example/component-example
     :title "Treemap Chart"
     :widget-id widget-id
     :description "A simple Treemap Chart built using [Recharts](https://recharts.org/en-US/api/Treemap)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (str widget-id "/treemap-chart")
     :source-code chart/source-code]))

