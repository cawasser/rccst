(ns bh.rccst.views.atom.example.chart.bar-chart
  (:require [bh.rccst.views.atom.example.chart.bar-chart.bar-chart :as bar-chart]
            [bh.rccst.views.atom.example.chart.bar-chart.config-ratom-example :as bar-chart-config-ratom]
            [bh.rccst.views.atom.example.chart.bar-chart.config-structure-example :as bar-chart-config-structure]
            [bh.rccst.views.atom.example.chart.bar-chart.config-sub-example :as bar-chart-config-sub]
            [bh.rccst.views.atom.example.chart.bar-chart.data-ratom-example :as bar-chart-data-ratom]
            [bh.rccst.views.atom.example.chart.bar-chart.data-structure-example :as bar-chart-data-structure]
            [bh.rccst.views.atom.example.chart.bar-chart.data-sub-example :as bar-chart-data-sub]))


(defn examples []
  [:div
   [bar-chart/example]
   [bar-chart-data-ratom/example]
   [bar-chart-data-structure/example]
   [bar-chart-data-sub/example]
   [bar-chart-config-ratom/example]
   [bar-chart-config-structure/example]
   [bar-chart-config-sub/example]])
