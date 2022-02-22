(ns bh.rccst.views.catalog.example.chart.funnel-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.funnel-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))


(defn example []
      (let [widget-id "funnel-chart-demo"]
           [example/component-example
            :title "Funnel Chart"
            :widget-id widget-id
            :description "A simple Funnel Chart built using [Recharts]()"
            :data chart/sample-data
            :component chart/component
            :component-id (str widget-id "/funnel-chart")
            :source-code chart/source-code]))