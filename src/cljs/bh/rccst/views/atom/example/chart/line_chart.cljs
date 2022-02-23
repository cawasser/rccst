(ns bh.rccst.views.atom.example.chart.line-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.chart.line-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]))


(defn simple-example []
  (let [widget-id "line-chart-demo"]
    [example/component-example
     :title "Line Chart"
     :widget-id widget-id
     :description "A simple Line Chart built using [Recharts](https://recharts.org/en-US)"
     :data chart/sample-data
     :component chart/component
     :component-id "line-chart-demo/line-chart"
     :source-code chart/source-code]))



