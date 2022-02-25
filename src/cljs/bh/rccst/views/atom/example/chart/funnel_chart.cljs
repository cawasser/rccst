(ns bh.rccst.views.atom.example.chart.funnel-chart
  (:require [bh.rccst.ui-component.atom.chart.funnel-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [taoensso.timbre :as log]))


(defn example []
  (let [widget-id "funnel-chart-demo"]
    [example/component-example
     :title "Funnel Chart"
     :widget-id widget-id
     :description "A simple Funnel Chart built using [Recharts]()"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (str widget-id "/funnel-chart")
     :source-code chart/source-code]))