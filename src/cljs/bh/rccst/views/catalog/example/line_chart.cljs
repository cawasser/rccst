(ns bh.rccst.views.catalog.example.line-chart
  (:require [reagent.core :as r]
            [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.line-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))


(defn simple-example []
  (let [widget-id "line-chart-demo"]
    [example/component-example
     :title "Line Chart"
     :widget-id widget-id
     :description "A simple Line Chart built using [Recharts](https://recharts.org/en-US)"
     :data (r/atom utils/tabular-data)
     :component chart/component
     :component-id "line-chart-demo/line-chart"
     :source-code chart/source-code]))



