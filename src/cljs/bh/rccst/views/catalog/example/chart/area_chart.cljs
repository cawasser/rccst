(ns bh.rccst.views.catalog.example.chart.area-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.chart.area-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))

(defn example []
      (let [widget-id "area-chart-demo"]
           [example/component-example
            :title "Area Chart"
            :widget-id widget-id
            :description "A simple Area Chart built using [Recharts]()"
            :data chart/sample-data
            :component chart/component
            :component-id "area-chart-demo/area-chart"
            :source-code chart/source-code]))
