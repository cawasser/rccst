(ns bh.rccst.views.atom.example.chart.sankey-chart-2
  (:require [bh.rccst.ui-component.atom.chart.sankey-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.views.atom.example.chart.alt.data-tools :as data-tools]
            [bh.rccst.views.atom.example.chart.alt.config-tools :as config-tools]
            [bh.rccst.views.atom.example.chart.alt.data-ratom-example :as data-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.data-structure-example :as data-structure-example]
            [bh.rccst.views.atom.example.chart.alt.data-sub-example :as data-sub-example]
            [bh.rccst.views.atom.example.chart.alt.config-ratom-example :as config-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.config-structure-example :as config-structure-example]
            [bh.rccst.views.atom.example.chart.alt.config-sub-example :as config-sub-example]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.sankey-chart-2")



(def default-config-data {})


(defn- data-ratom []
  [data-ratom-example/example
   :container-id :sankey-2-chart-data-ratom-demo
   :title "Sankey Chart 2 (Live Data - ratom)"
   :description "A Sankey Chart 2 built using [Recharts](https://recharts.org/en-US/api/SankeyChart). This example shows how
  charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

  > In _this_ case, we are using a ratom for the data.
  >
  > You can use the buttons below to change some of the data and see how the chart responds."
   :sample-data chart/sample-data
   :data-tools (fn [& x] [:div "data tools"]) ;data-tools/meta-tabular-data-ratom-tools
   :source-code chart/source-code
   :component chart/component
   :data-panel (fn [& x] [:div "data panel"]) ;chart-utils/tabular-data-panel
   :config-panel chart/config-panel])


(defn examples []
  [:div
   [data-ratom]])
   ;[data-structure]
   ;[data-sub]
   ;[config-ratom]
   ;[config-structure]
   ;[config-sub]])


