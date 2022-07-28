(ns rccst.views.atom.charts
  (:require [rccst.views.atom.example.chart.area-chart :as area-chart]
            [rccst.views.atom.example.chart.bar-chart :as bar-chart]
            [rccst.views.atom.example.chart.colored-pie-chart :as colored-pie-chart]
            [rccst.views.atom.example.chart.funnel-chart :as funnel-chart]
            [rccst.views.atom.example.chart.line-chart :as line-chart]
            [rccst.views.atom.example.chart.pie-chart :as pie-chart]
            [rccst.views.atom.example.chart.radar-chart :as radar-chart]
            [rccst.views.atom.example.chart.radial-bar-chart :as radial-bar-chart]
            [rccst.views.atom.example.chart.sankey-chart :as sankey-chart]
            [rccst.views.atom.example.chart.scatter-chart :as scatter-chart]
            [rccst.views.atom.example.chart.treemap-chart :as treemap-chart]))


(defn page
  "Charts"
  []

  [:div
   [area-chart/examples]
   [bar-chart/examples]
   [colored-pie-chart/examples]
   [funnel-chart/examples]
   [line-chart/examples]
   [pie-chart/examples]
   [radar-chart/examples]
   [radial-bar-chart/examples]
   [sankey-chart/examples]
   [scatter-chart/examples]
   [treemap-chart/examples]])

