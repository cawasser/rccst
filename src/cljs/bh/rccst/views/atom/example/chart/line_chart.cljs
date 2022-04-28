(ns bh.rccst.views.atom.example.chart.line-chart
  (:require [bh.rccst.views.atom.example.chart.line-chart.line-chart :as line-chart]
            [bh.rccst.views.atom.example.chart.line-chart.data-ratom-example :as line-chart-data-ratom]))


(defn examples []
  [:div
   [line-chart/simple-example]
   [line-chart-data-ratom/example]])
