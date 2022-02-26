(ns bh.rccst.views.atom.example.chart.area-chart
  (:require [bh.rccst.ui-component.atom.chart.area-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "area-chart-demo"]
    [example/component-example
     :widget-id container-id
     :title "Area Chart"
     :description "A simple Area Chart built using [Recharts](https://recharts.org/en-US/api/AreaChart)"
     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id "area-chart")
     :source-code chart/source-code]))
