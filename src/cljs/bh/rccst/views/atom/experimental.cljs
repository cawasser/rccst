(ns bh.rccst.views.atom.experimental
  (:require [bh.rccst.views.atom.example.oz.line-chart :as oz-line-chart]
            [bh.rccst.views.atom.example.oz.bar-chart :as oz-bar-chart]
            [bh.rccst.views.atom.example.experimental.ui-element :as ui-element]))



(defn examples []
  [:div
   [oz-bar-chart/example]
   [oz-line-chart/example]])
   ;[ui-element/selectable-table]])
   ;[ui-element/three-d-globe]
   ;[ui-element/slider]
   ;[ui-element/label]])
