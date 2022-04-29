(ns bh.rccst.views.atom.example.chart.colored-pie-chart
  (:require [bh.rccst.views.atom.example.chart.colored-pie-chart.data-ratom-example :as data-ratom-chart]
            [bh.rccst.views.atom.example.chart.colored-pie-chart.colored-pie-chart :as colored-pie-chart]))



(defn examples []
  [:div
   ;[colored-pie-chart/example]
   [data-ratom-chart/example]])


