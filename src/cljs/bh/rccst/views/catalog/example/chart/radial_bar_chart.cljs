(ns bh.rccst.views.catalog.example.chart.radial-bar-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.chart.radial-bar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]))


(defn example []
      (let [widget-id "radial-bar-chart-demo"]
           [example/component-example
            :title "Radial Bar Chart"
            :widget-id widget-id
            :description "A simple Radial Bar Chart built using [Recharts]()"
            :data chart/sample-data
            :component chart/component
            :component-id "radial-bar-chart-demo/radial-bar-chart"
            :source-code chart/source-code]))
