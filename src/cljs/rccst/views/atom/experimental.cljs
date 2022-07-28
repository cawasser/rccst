(ns rccst.views.atom.experimental
  (:require [rccst.views.atom.example.oz.line-chart :as oz-line-chart]
            [rccst.views.atom.example.oz.bar-chart :as oz-bar-chart]
            [rccst.views.atom.example.experimental.ui-element :as ui-element]

            [rccst.views.atom.example.re-com.plain-table :as plain-table]))



(defn examples []
  [:div
   [oz-bar-chart/example]
   [oz-line-chart/example]
   [plain-table/example]])
   ;[ui-element/selectable-table]])
   ;[ui-element/three-d-globe]
   ;[ui-element/slider]
   ;[ui-element/label]])
