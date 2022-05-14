(ns bh.rccst.views.atom.charts
  (:require [bh.rccst.views.atom.example.chart.funnel-chart :as funnel-chart]
            [bh.rccst.views.atom.example.chart.radar-chart :as radar-chart]
            [bh.rccst.views.atom.example.chart.sankey-chart :as sankey-chart]
            [bh.rccst.views.atom.example.chart.scatter-chart :as scatter-chart]
            [bh.rccst.views.atom.example.chart.treemap-chart :as treemap-chart]))


(defn examples []
  [:div
   [funnel-chart/example]
   [radar-chart/example]
   [sankey-chart/example]
   [scatter-chart/example]
   [treemap-chart/example]])


