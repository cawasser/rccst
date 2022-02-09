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
     :description "A simple Line Chart built using [Recharts]()"
     :data (r/atom utils/tabular-data)
     :config (chart/config widget-id)
     :component-panel chart/component
     :source-code chart/source-code]))


(defn stacked-example []
  (let [widget-id "stacked-line-chart-demo"]
    [example/example
     :title "Stacked Line Chart"
     :widget-id widget-id
     :description "A simple Line Chart built using [Recharts]()"
     :data (r/atom utils/tabular-data)
     :config (chart/config widget-id)
     :data-panel utils/tabular-data-panel
     :config-panel chart/config-panel
     :component-panel chart/component
     :source-code "dummy source code"]))

