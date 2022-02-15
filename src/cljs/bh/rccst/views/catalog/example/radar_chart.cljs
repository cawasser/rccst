(ns bh.rccst.views.catalog.example.radar-chart
  (:require [bh.rccst.ui-component.atom.radar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]))



(defn example []
      (let [widget-id "radar-chart-demo"]
           [example/component-example
            :title "Radar Chart"
            :widget-id widget-id
            :description "A simple Radar Chart built using [Recharts]()"
            :data chart/sample-data
            :component chart/component
            :component-id "radar-chart-demo/radar-chart"
            :source-code chart/source-code]))

