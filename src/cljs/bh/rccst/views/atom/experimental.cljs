(ns bh.rccst.views.atom.experimental
  (:require [bh.rccst.views.atom.example.oz.line-chart :as oz-line-chart]
            [bh.rccst.views.atom.example.oz.bar-chart :as oz-bar-chart]
            [bh.rccst.views.atom.example.experimental.ui-element :as ui-element]

            [bh.rccst.views.atom.example.re-com.plain-table :as plain-table]))



(defn examples []
  [:div
   [oz-bar-chart/example]
   [oz-line-chart/example]
   [plain-table/example]])
   ;[ui-element/selectable-table]])
   ;[ui-element/three-d-globe]
   ;[ui-element/slider]
   ;[ui-element/label]])
