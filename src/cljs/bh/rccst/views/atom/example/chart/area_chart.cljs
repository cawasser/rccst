(ns bh.rccst.views.atom.example.chart.area-chart
  (:require [bh.rccst.ui-component.atom.chart.area-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.views.atom.example.chart.alt.data-tools :as data-tools]
            [bh.rccst.views.atom.example.chart.alt.data-ratom-example :as data-ratom-example]))

(defn- data-ratom []
  [data-ratom-example/example
   :container-id :area-chart-2-data-ratom-demo
   :title "Area Chart 2 (Live Data - ratom)"
   :description "An Area Chart (2) built using [Recharts](https://recharts.org/en-US/api/AreaChart). This example shows how
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

(defn examples []
  [:div
   [:p "Examples go here"]
   [data-ratom]])

