(ns bh.rccst.views.atom.example.chart.radial-bar-chart
  (:require [bh.rccst.ui-component.atom.chart.radial-bar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "radial-bar-chart-demo"]
    [example/component-example
     :title "Radial Bar Chart"
     :widget-id container-id
     :description "A simple Radial Bar Chart built using [Recharts](https://recharts.org/en-US/api/RadialBarChart)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id"radial-bar-chart")
     :source-code chart/source-code]))
