(ns bh.rccst.views.catalog.example.radar-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.radar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))

(defn example []
      (let [widget-id "radar-chart-demo"]
           [example/example
            :title "Radar Chart"
            :widget-id widget-id
            :description "A simple Radar Chart built using [Recharts]()"
            :data chart/sample-data
            :config (chart/config widget-id)
            :data-panel utils/tabular-data-panel
            :config-panel chart/config-panel
            :component-panel chart/component
            :source-code chart/source-code]))

