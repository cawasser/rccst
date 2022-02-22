(ns bh.rccst.views.catalog.example.chart.treemap-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.treemap-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]))


(defn example []
  (let [widget-id "treemap-chart-demo"]
    [example/component-example
     :title "Treemap Chart"
     :widget-id widget-id
     :description "A simple Treemap Chart built using [Recharts](https://recharts.org/en-US/api/Treemap)"
     :data chart/sample-data
     :component chart/component
     :component-id (str widget-id "/treemap-chart")
     :source-code chart/source-code]))

