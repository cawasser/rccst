(ns bh.rccst.views.catalog.example.bar-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.bar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]))


(defn example []
  (let [widget-id "bar-chart-demo"]
    [example/component-example
       :title "Bar Chart"
       :widget-id widget-id
       :description "A simple Bar Chart built using [Recharts]()"
       :data chart/sample-data
       :component chart/component
       :component-id "bar-chart-demo/bar-chart"
       :source-code chart/source-code]))

