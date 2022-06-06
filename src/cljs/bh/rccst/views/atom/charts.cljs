(ns bh.rccst.views.atom.charts
  (:require [bh.rccst.events :as events]
            [bh.rccst.ui-component.tabbed-panel :as tabbed-panel]
            [bh.rccst.views.atom.example.chart.area-chart :as area-chart]
            [bh.rccst.views.atom.example.chart.bar-chart :as bar-chart]
            [bh.rccst.views.atom.example.chart.radial-bar-chart :as radial-bar-chart]
            [bh.rccst.views.atom.example.chart.pie-chart :as pie-chart]
            [bh.rccst.views.atom.example.chart.colored-pie-chart :as colored-pie-chart]
            [bh.rccst.views.atom.example.chart.line-chart :as line-chart]
            [bh.rccst.views.atom.example.chart.funnel-chart :as funnel-chart]
            [bh.rccst.views.atom.example.chart.sankey-chart :as sankey-chart]
            [bh.rccst.views.atom.example.chart.scatter-chart :as scatter-chart]
            [bh.rccst.views.atom.example.chart.radar-chart :as radar-chart]
            [bh.rccst.views.atom.example.chart.treemap-chart :as treemap-chart]))


(declare examples)


(def navbar [[:charts/charts "Charts" [examples]]

             [:charts/scatter-chart "Scatter" [scatter-chart/examples]]
             [:charts/treemap-chart "TreeMap" [treemap-chart/examples]]])


(defn- examples []
  [:div
   [area-chart/examples]
   [bar-chart/examples]
   [colored-pie-chart/examples]
   [funnel-chart/examples]
   [line-chart/examples]
   [pie-chart/examples]
   [radar-chart/examples]
   [radial-bar-chart/examples]
   [sankey-chart/examples]])


(defn page
  "Charts"
  []

  [tabbed-panel/tabbed-panel
   :title ""
   :short-name "charts"
   :description ""
   :children navbar
   :start-panel :charts/charts])