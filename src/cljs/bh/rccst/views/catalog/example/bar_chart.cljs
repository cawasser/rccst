(ns bh.rccst.views.catalog.example.bar-chart
  (:require [reagent.core :as r]
            [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.bar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))

(defn example []
  (let [widget-id "bar-chart-demo"]
    [example/component-example
       :title "Bar Chart"
       :widget-id widget-id
       :description "A simple Bar Chart built using [Recharts]()"
       :data (r/atom (mapv (fn [d] (assoc d :d (rand-int 5000))) utils/tabular-data))
       :component chart/component
       :component-id "bar-chart-demo/bar-chart"
       :source-code chart/source-code]))

