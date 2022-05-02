(ns bh.rccst.views.atom.example.chart.line-chart
  (:require [bh.rccst.views.atom.example.chart.line-chart.line-chart :as line-chart]
            [bh.rccst.views.atom.example.chart.line-chart.config-ratom-example :as line-chart-config-ratom]
            [bh.rccst.views.atom.example.chart.line-chart.config-structure-example :as line-chart-config-structure]
            [bh.rccst.views.atom.example.chart.line-chart.config-sub-example :as line-chart-config-sub]
            [bh.rccst.views.atom.example.chart.line-chart.data-ratom-example :as line-chart-data-ratom]
            [bh.rccst.views.atom.example.chart.line-chart.data-structure-example :as line-chart-data-structure]
            [bh.rccst.views.atom.example.chart.line-chart.data-sub-example :as line-chart-data-sub]))


(defn examples []
  [:div
   [line-chart/simple-example]
   [line-chart-data-ratom/example]
   [line-chart-data-structure/example]
   [line-chart-data-sub/example]
   [line-chart-config-ratom/example]
   [line-chart-config-structure/example]
   [line-chart-config-sub/example]])
