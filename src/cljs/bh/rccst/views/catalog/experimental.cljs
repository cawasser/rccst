(ns bh.rccst.views.catalog.experimental
  (:require [bh.rccst.views.catalog.example.oz.line-chart :as oz-line-chart]
            [bh.rccst.views.catalog.example.oz.bar-chart :as oz-bar-chart]))


(defn catalog []
  [:div
   [oz-bar-chart/example]
   [oz-line-chart/example]])
