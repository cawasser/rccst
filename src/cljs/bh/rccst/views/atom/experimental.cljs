(ns bh.rccst.views.atom.experimental
  (:require [bh.rccst.views.atom.example.oz.line-chart :as oz-line-chart]
            [bh.rccst.views.atom.example.oz.bar-chart :as oz-bar-chart]))


(defn examples []
  [:div
   [oz-bar-chart/example]
   [oz-line-chart/example]])
