(ns bh.rccst.views.catalog.charts
  (:require [bh.rccst.views.catalog.example.area-chart :as area-chart]
            [bh.rccst.views.catalog.example.bar-chart :as bar-chart]
            [bh.rccst.views.catalog.example.colored-pie-chart :as colored-pie-chart]
            [bh.rccst.views.catalog.example.line-chart :as line-chart]
            [bh.rccst.views.catalog.example.pie-chart :as pie-chart]
            [bh.rccst.views.catalog.example.scatter-chart :as scatter-chart]
            [bh.rccst.views.catalog.example.funnel-chart :as funnel-chart]
            [bh.rccst.views.catalog.example.treemap-chart :as treemap-chart]
            [bh.rccst.views.catalog.example.sankey-chart :as sankey-chart]
            [bh.rccst.views.catalog.example.radar-chart :as radar-chart]
            [bh.rccst.views.catalog.example.radial-bar-chart :as radial-bar-chart]))



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


