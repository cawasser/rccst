(ns bh.rccst.views.atom.example.chart.radar-chart
  (:require [bh.rccst.ui-component.atom.chart.radar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]))



(defn example []
      (let [widget-id "radar-chart-demo"]
           [example/component-example
            :title "Radar Chart"
            :widget-id widget-id
            :description "A simple Radar Chart built using [Recharts]()"
            :data chart/sample-data
            :component chart/component
            :component-id (str widget-id "/radar-chart")
            :source-code chart/source-code]))

