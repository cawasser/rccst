(ns bh.rccst.views.catalog.experimental
  (:require [bh.rccst.views.catalog.example.oz.line-chart :as oz-line-chart]))


(defn catalog []
  [:div
   [oz-line-chart/example]])
