(ns bh.rccst.views.atom.example.chart.radar-chart
  (:require [bh.rccst.ui-component.atom.chart.radar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.views.atom.example.chart.alt.data-ratom-example :as data-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.data-structure-example :as data-structure-example]
            [bh.rccst.views.atom.example.chart.alt.data-sub-example :as data-sub-example]
            [bh.rccst.views.atom.example.chart.alt.config-ratom-example :as config-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.config-structure-example :as config-structure-example]
            [bh.rccst.views.atom.example.chart.alt.config-sub-example :as config-sub-example]
            [bh.rccst.views.atom.example.chart.alt.data-tools :as data-tools]
            [bh.rccst.views.atom.example.chart.alt.config-tools :as config-tools]
            [taoensso.timbre :as log]))

(defn- data-ratom []
  [data-ratom-example/example
   :container-id :radar-chart-2-data-ratom-demo
   :title "Radar Chart 2 (Live Data - ratom)"
   :description "A Radar Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
  charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

  > In _this_ case, we are using a ratom for the data.
  >
  > You can use the buttons below to change some of the data and see how the chart responds."
   :sample-data chart/sample-data
   :data-tools data-tools/meta-tabular-data-ratom-tools
   :source-code chart/source-code
   :component chart/component
   :data-panel chart-utils/meta-tabular-data-panel
   :config-panel chart/config-panel])

;(defn example* []
;  (let [container-id "radar-chart-demo"]
;    [example/component-example
;     :title "Radar Chart"
;     :container-id container-id
;     :description "A simple Radar Chart built using [Recharts](https://recharts.org/en-US/api/RadarChart)"
;     :data chart/sample-data
;     :component chart/configurable-component
;     :component-id (utils/path->keyword container-id "radar-chart")
;     :source-code chart/source-code]))


(defn examples []
  [:div
   [data-ratom]
   ;[data-structure]
   ;[data-sub]
   ;[config-ratom]
   ;[config-structure]
   ;[config-sub]
   ])
