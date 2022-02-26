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


(def registry {:chart/area-chart        {:component area-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/bar-chart         {:component bar-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/colored-pie-chart {:component colored-pie-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/funnel-chart      {:component funnel-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/line-chart        {:component line-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/pie-chart         {:component pie-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/radar-chart       {:component radar-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/radial-bar-chart  {:component radial-bar-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/sankey-chart      {:component sankey-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/scatter-chart     {:component scatter-chart/component :sources [:data] :pubs nil :subs nil}
               :chart/treemap-chart     {:component treemap-chart/component :sources [:data] :pubs nil :subs nil}})

