(ns bh.rccst.views.catalog.example.treemap-chart
  (:require [taoensso.timbre :as log]
            [reagent.core :as r]

            [bh.rccst.ui-component.atom.treemap-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))


(defn example []
  (let [widget-id "treemap-chart-demo"]
    [example/component-example
     :title "Treemap Chart"
     :widget-id widget-id
     :description "A simple Treemap Chart built using [Recharts](https://recharts.org/en-US/api/Treemap)"
     :data (r/atom utils/hierarchy-data)
     :component chart/component
     :component-id (str widget-id "/treemap-chart")
     :source-code chart/source-code]))

