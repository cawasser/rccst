(ns bh.rccst.views.atom.example.chart.area-chart
  (:require [bh.rccst.views.atom.example.chart.area-chart.area-chart :as area-chart]
            [bh.rccst.views.atom.example.chart.area-chart.data-ratom-example :as data-ratom]))

(defn examples []
  [:div
   [area-chart/example]
   [data-ratom/example]])

;
; this code was moved to an area-chart folder.
; keeping it here until I can finish refactoring
;
;(defn example []
;  (let [container-id "area-chart-demo"]
;    [example/component-example
;     :container-id container-id
;     :title "Area Chart"
;     :description "A simple Area Chart built using [Recharts](https://recharts.org/en-US/api/AreaChart)"
;     :data chart/sample-data
;     :component chart/configurable-component
;     :component-id (utils/path->keyword container-id "area-chart")
;     :source-code chart/source-code]))
