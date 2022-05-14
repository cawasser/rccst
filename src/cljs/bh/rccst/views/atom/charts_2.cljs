(ns bh.rccst.views.atom.charts-2
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


(def navbar [[:charts/area-chart "Area" [area-chart/examples]]
             [:charts/bar-chart "Bar" [bar-chart/examples]]
             [:charts/colored-pie-chart "Colored Pie" [colored-pie-chart/examples]]
             [:charts/funnel-chart "Funnel" [funnel-chart/examples]]
             [:charts/line-chart "Line" [line-chart/examples]]
             [:charts/pie-chart "Pie" [pie-chart/examples]]
             [:charts/radar-chart "Radar" [radar-chart/examples]]
             [:charts/radial-bar-chart "Radial Bar" [radial-bar-chart/examples]]
             [:charts/sankey-chart "Sankey" [sankey-chart/examples]]
             [:charts/scatter-chart "Scatter" [scatter-chart/examples]]
             [:charts/treemap-chart "TreeMap" [treemap-chart/examples]]])
                ;[:charts/all "All" [all-demo/examples]]])


(defn page
  "Charts"
  []

  [tabbed-panel/tabbed-panel
   :title ""
   :short-name "charts"
   :description ""
   :children navbar
   :start-panel :charts/area-chart])