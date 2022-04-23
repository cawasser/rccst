(ns bh.rccst.views.atom.charts
  (:require [bh.rccst.views.atom.example.chart.area-chart :as area-chart]
            [bh.rccst.views.atom.example.chart.bar-chart :as bar-chart]
            [bh.rccst.views.atom.example.chart.bar-chart.data-example :as bar-chart-data]
            [bh.rccst.views.atom.example.chart.bar-chart.config-example :as bar-chart-config]
            [bh.rccst.views.atom.example.chart.colored-pie-chart :as colored-pie-chart]
            [bh.rccst.views.atom.example.chart.line-chart :as line-chart]
            [bh.rccst.views.atom.example.chart.pie-chart :as pie-chart]
            [bh.rccst.views.atom.example.chart.scatter-chart :as scatter-chart]
            [bh.rccst.views.atom.example.chart.funnel-chart :as funnel-chart]
            [bh.rccst.views.atom.example.chart.treemap-chart :as treemap-chart]
            [bh.rccst.views.atom.example.chart.sankey-chart :as sankey-chart]
            [bh.rccst.views.atom.example.chart.radar-chart :as radar-chart]
            [bh.rccst.views.atom.example.chart.radial-bar-chart :as radial-bar-chart]))


(defn examples []
  [:div
   [area-chart/example]
   [bar-chart/example]
   [bar-chart-data/example]
   [bar-chart-config/example]
   [colored-pie-chart/example]
   [funnel-chart/example]
   [line-chart/simple-example]
   [pie-chart/example]
   [radar-chart/example]
   [radial-bar-chart/example]
   [sankey-chart/example]
   [scatter-chart/example]
   [treemap-chart/example]])


