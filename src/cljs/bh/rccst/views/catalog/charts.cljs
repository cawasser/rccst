(ns bh.rccst.views.catalog.charts
  (:require [bh.rccst.views.catalog.example.bar-chart :as bar-chart]
            [bh.rccst.views.catalog.example.colored-pie-chart :as colored-pie-chart]
            [bh.rccst.views.catalog.example.line-chart :as line-chart]
            [bh.rccst.views.catalog.example.pie-chart :as pie-chart]
            [bh.rccst.views.catalog.example.scatter-chart :as scatter-chart]
            [bh.rccst.views.catalog.example.funnel-chart :as funnel-chart]
            [bh.rccst.views.catalog.example.treemap-chart :as treemap-chart]
            [bh.rccst.views.catalog.example.sankey-chart :as sankey-chart]))



(defn catalog []
  [:div
   ;[bar-chart/example]
   ;[line-chart/simple-example]
   ;[pie-chart/example]
   ;[colored-pie-chart/example]
   ;[scatter-chart/example]
   ;[funnel-chart/example]
   ;[treemap-chart/example]
   [sankey-chart/example]])


