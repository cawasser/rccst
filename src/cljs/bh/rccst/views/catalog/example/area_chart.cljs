(ns bh.rccst.views.catalog.example.area-chart
  (:require [reagent.core :as r]
            [bh.rccst.ui-component.atom.area-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))

(defn example []
      (let [widget-id "area-chart-demo"]
           [example/example
            :title "Area Chart"
            :widget-id widget-id
            :description "A simple Area Chart built using [Recharts]()"
            :data (r/atom (mapv (fn [d] (assoc d :d (rand-int 5000))) utils/tabular-data))
            :config (chart/config widget-id)
            :data-panel utils/tabular-data-panel
            :config-panel chart/config-panel
            :component-panel chart/component
            :source-code chart/source-code]))

