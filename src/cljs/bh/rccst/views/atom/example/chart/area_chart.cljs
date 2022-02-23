(ns bh.rccst.views.atom.example.chart.area-chart
  (:require [bh.rccst.ui-component.atom.chart.area-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [taoensso.timbre :as log]))


(defn example []
  (let [widget-id "area-chart-demo"]
    [example/component-example
     :widget-id widget-id
     :title "Area Chart"
     :description "A simple Area Chart built using [Recharts](https://recharts.org/en-US/api/AreaChart)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id "area-chart-demo/area-chart"
     :source-code chart/source-code]))
