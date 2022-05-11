(ns bh.rccst.views.atom.example.chart.radar-chart
  (:require [bh.rccst.ui-component.atom.chart.radar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "radar-chart-demo"]
    [example/component-example
     :title "Radar Chart"
     :container-id container-id
     :description "A simple Radar Chart built using [Recharts](https://recharts.org/en-US/api/RadarChart)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id "radar-chart")
     :source-code chart/source-code]))

