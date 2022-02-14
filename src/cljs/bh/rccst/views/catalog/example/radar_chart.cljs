(ns bh.rccst.views.catalog.example.radar-chart
  (:require [reagent.core :as r]
            ["recharts" :refer [RadarChart PolarGrid PolarAngleAxis PolarRadiusAxis Radar]]
            [bh.rccst.ui-component.atom.radar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))

(def data [{:subject "Math" :A 120 :B 110 :fullMark 150}
           {:subject "Chinese" :A 98 :B 130 :fullMark 150}
           {:subject "English" :A 100 :B 110 :fullMark 150}
           {:subject "History" :A 77 :B 81 :fullMark 150}
           {:subject "Economics" :A 99 :B 140 :fullMark 150}
           {:subject "Literature" :A 98 :B 105 :fullMark 150}])

(defn example []
      [:div
       [:> RadarChart {:width 400 :height 400 :cx "50%" :cy "50%" :outerRadius "80%" :data data}
        [:> PolarGrid]
        [:> PolarAngleAxis {:dataKey :subject}]
        [:> PolarRadiusAxis]
        [:> Radar {:name "Mike" :dataKey :A :stroke "#8884d8" :fill "#8884d8" :fillOpacity 0.6}]
        [:> Radar {:name "Sally" :dataKey :B :stroke "#82ca9d" :fill "#82ca9d" :fillOpacity 0.6}]]]
      )

;(defn example []
;      (let [widget-id "radar-chart-demo"]
;           [example/example
;            :title "Radar Chart"
;            :widget-id widget-id
;            :description "A simple Radar Chart built using [Recharts]()"
;            :data (r/atom data)
;            :config (chart/config widget-id)
;            :data-panel utils/tabular-data-panel
;            :config-panel chart/config-panel
;            :component-panel chart/component
;            :source-code chart/source-code]))
;
