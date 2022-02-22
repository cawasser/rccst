(ns bh.rccst.views.catalog.example.chart.scatter-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.scatter-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))


(defn example []
      (let [widget-id "scatter-chart-demo"]
           [example/component-example
            :title "Scatter Chart"
            :widget-id widget-id
            :description "A simple Scatter Chart built using [Recharts]()"
            :data chart/sample-data
            :component chart/component
            :component-id (str widget-id "/scatter-chart")
            :source-code chart/source-code]))
