(ns bh.rccst.views.catalog.charts
  (:require [bh.rccst.views.catalog.example.chart.area-chart :as area-chart]
            [bh.rccst.views.catalog.example.chart.bar-chart :as bar-chart]
            [bh.rccst.views.catalog.example.chart.colored-pie-chart :as colored-pie-chart]
            [bh.rccst.views.catalog.example.chart.line-chart :as line-chart]
            [bh.rccst.views.catalog.example.chart.pie-chart :as pie-chart]
            [bh.rccst.views.catalog.example.chart.scatter-chart :as scatter-chart]
            [bh.rccst.views.catalog.example.chart.funnel-chart :as funnel-chart]
            [bh.rccst.views.catalog.example.chart.treemap-chart :as treemap-chart]
            [bh.rccst.views.catalog.example.chart.sankey-chart :as sankey-chart]
            [bh.rccst.views.catalog.example.chart.radar-chart :as radar-chart]
            [bh.rccst.views.catalog.example.chart.radial-bar-chart :as radial-bar-chart]))



(defn catalog []
  [:div
   [area-chart/example]
   [bar-chart/example]
   [line-chart/simple-example]
   [pie-chart/example]
   [colored-pie-chart/example]
   [scatter-chart/example]
   [funnel-chart/example]
   [treemap-chart/example]
   [sankey-chart/example]
   [radar-chart/example]
   [radial-bar-chart/example]])


