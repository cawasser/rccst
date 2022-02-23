(ns bh.rccst.views.atom.example.chart.bar-chart
  (:require [bh.rccst.ui-component.atom.chart.bar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [taoensso.timbre :as log]))


(defn example []
  (let [widget-id "bar-chart-demo"]
    [example/component-example
     :title "Bar Chart"
     :widget-id widget-id
     :description "A simple Bar Chart built using [Recharts]()"
     :data chart/sample-data
     :component chart/component
     :component-id (str widget-id "/bar-chart")
     :source-code chart/source-code]))

