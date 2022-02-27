(ns bh.rccst.ui-component.atom.chart.registry
  (:require [bh.rccst.ui-component.atom.chart.area-chart :as area-chart]
            [bh.rccst.ui-component.atom.chart.bar-chart :as bar-chart]
            [bh.rccst.ui-component.atom.chart.colored-pie-chart :as colored-pie-chart]
            [bh.rccst.ui-component.atom.chart.funnel-chart :as funnel-chart]
            [bh.rccst.ui-component.atom.chart.line-chart :as line-chart]
            [bh.rccst.ui-component.atom.chart.pie-chart :as pie-chart]
            [bh.rccst.ui-component.atom.chart.radar-chart :as radar-chart]
            [bh.rccst.ui-component.atom.chart.radial-bar-chart :as radial-bar-chart]
            [bh.rccst.ui-component.atom.chart.sankey-chart :as sankey-chart]
            [bh.rccst.ui-component.atom.chart.scatter-chart :as scatter-chart]
            [bh.rccst.ui-component.atom.chart.treemap-chart :as treemap-chart]))


(def registry {:chart/area-chart        area-chart/meta-data
               :chart/bar-chart         bar-chart/meta-data
               :chart/colored-pie-chart colored-pie-chart/meta-data
               :chart/funnel-chart      funnel-chart/meta-data
               :chart/line-chart        line-chart/meta-data
               :chart/pie-chart         pie-chart/meta-data
               :chart/radar-chart       radar-chart/meta-data
               :chart/radial-bar-chart  radial-bar-chart/meta-data
               :chart/sankey-chart      sankey-chart/meta-data
               :chart/scatter-chart     scatter-chart/meta-data
               :chart/treemap-chart     treemap-chart/meta-data})

