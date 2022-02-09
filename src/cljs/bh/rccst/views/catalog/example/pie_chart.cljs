(ns bh.rccst.views.catalog.example.pie-chart
  (:require [reagent.core :as r]

            [bh.rccst.ui-component.atom.pie-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.views.catalog.example.chart.utils :as utils]))


(defn example []
  (let [widget-id "pie-chart-demo"]
    [example/example
     :title "Pie Chart"
     :widget-id widget-id
     :description       "Pie Chart with a default fill for each slice. Each slice is sized correctly and labeled with the value, but
      they are all the same color.

> See `Colored Pie Chart` for an example of how to get the slices to be different colors."
     :data (r/atom utils/paired-data)
     :config (chart/config widget-id)
     :data-panel utils/tabular-data-panel
     :config-panel chart/config-panel
     :component-panel chart/component
     :source-code chart/source-code]))
