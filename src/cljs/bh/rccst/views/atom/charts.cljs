(ns bh.rccst.views.atom.charts
  (:require ;[bh.rccst.views.atom.example.chart.area-chart :as area-chart]
            ;[bh.rccst.views.atom.example.chart.bar-chart :as bar-chart]
            ;[bh.rccst.views.atom.example.chart.bar-chart.data-ratom-example :as bar-chart-data-ratom]
            ;[bh.rccst.views.atom.example.chart.bar-chart.data-structure-example :as bar-chart-data-structure]
            ;[bh.rccst.views.atom.example.chart.bar-chart.data-sub-example :as bar-chart-data-sub]
            ;[bh.rccst.views.atom.example.chart.bar-chart.config-ratom-example :as bar-chart-config-ratom]
            ;[bh.rccst.views.atom.example.chart.bar-chart.config-structure-example :as bar-chart-config-structure]
            ;[bh.rccst.views.atom.example.chart.bar-chart.config-sub-example :as bar-chart-config-sub]
            ;[bh.rccst.views.atom.example.chart.colored-pie-chart :as colored-pie-chart]
            ;[bh.rccst.views.atom.example.chart.line-chart :as line-chart]
            ;[bh.rccst.views.atom.example.chart.pie-chart :as pie-chart]
            [bh.rccst.views.atom.example.chart.scatter-chart :as scatter-chart]
            [bh.rccst.views.atom.example.chart.funnel-chart :as funnel-chart]
            [bh.rccst.views.atom.example.chart.treemap-chart :as treemap-chart]
            [bh.rccst.views.atom.example.chart.sankey-chart :as sankey-chart]
            [bh.rccst.views.atom.example.chart.radar-chart :as radar-chart]
            [bh.rccst.views.atom.example.chart.radial-bar-chart :as radial-bar-chart]))


(defn examples []
  [:div
   ;[area-chart/example]
   ;[bar-chart/example]
   ;[bar-chart-data-ratom/example]
   ;[bar-chart-data-structure/example]
   ;[bar-chart-data-sub/example]
   ;[bar-chart-config-ratom/example]
   ;[bar-chart-config-structure/example]
   ;[bar-chart-config-sub/example]
   ;[colored-pie-chart/example]
   [funnel-chart/example]
   ;[line-chart/simple-example]
   ;[pie-chart/example]
   [radar-chart/example]
   [radial-bar-chart/example]
   [sankey-chart/example]
   [scatter-chart/example]
   [treemap-chart/example]])


