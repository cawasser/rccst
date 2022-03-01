(ns bh.rccst.views.atom.example.chart.funnel-chart
  (:require [bh.rccst.ui-component.atom.chart.funnel-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "funnel-chart-demo"]
    [example/component-example
     :title "Funnel Chart"
     :widget-id container-id
     :description "A simple Funnel Chart built using [Recharts](https://recharts.org/en-US/api/FunnelChart)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id "funnel-chart")
     :source-code chart/source-code]))