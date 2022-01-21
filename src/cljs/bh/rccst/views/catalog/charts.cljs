(ns bh.rccst.views.catalog.charts
  (:require [bh.rccst.views.catalog.example.bar-chart :as bar-chart]
            [bh.rccst.views.catalog.example.colored-pie-chart :as colored-pie-chart]
            [bh.rccst.views.catalog.example.line-chart :as line-chart]
            [bh.rccst.views.catalog.example.pie-chart :as pie-chart]))



(defn catalog []
  [:div
   [bar-chart/example]
   [line-chart/example]
   [pie-chart/example]
   [colored-pie-chart/example]])


