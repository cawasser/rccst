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
            [taoensso.timbre :as log]
            [bh.rccst.views.atom.example.multi-example :as me]))


(def default-config-data {:domain [0 150],
                          :A {:include true, :name :A, :fill "#8884d8",
                              :stroke "#8884d8", :fillOpacity 0.6},
                          :B {:include true, :name :B, :fill "#ffc107",
                              :stroke "#ffc107", :fillOpacity 0.6},})
                          ;:fullMark {:include true, :name :fullMark, :fill "#82ca9d",
                          ;           :stroke "#82ca9d", :fillOpacity 0.6}})


(defn- data-ratom []
  [data-ratom-example/example
   :container-id :radar-chart-2-data-ratom-demo
   :title "Radar Chart 2 (Live Data - ratom) - IN PROGRESS (not complete)"
   :description "A Radar Chart (2) built using [Recharts](https://recharts.org/en-US/api/RadarChart). This example shows how
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


(defn- data-structure []
  [data-structure-example/example
   :container-id :radar-chart-2-data-structure-demo
   :title "Radar Chart 2 (Live Data - structure) - IN PROGRESS (not complete)"
   :description "A Radar Chart (2) built using [Recharts](https://recharts.org/en-US/api/RadarChart). This example shows how
  charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

  > In _this_ case, we are using a plain data structure for the data, so there is no way to update it (it lives
  > only inside the chart, with no way to get at it from outside)."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :component chart/component
   :data-panel chart-utils/meta-tabular-data-panel
   :config-panel chart/config-panel])


(defn data-sub []
  [data-sub-example/example
   :container-id :radar-chart-2-data-sub-demo
   :title "Radar Chart 2 (Live Data - subscription) - IN PROGRESS (not complete)"
   :description "A Radar Chart (2) built using [Recharts](https://recharts.org/en-US/api/RadarChart). This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the data for the chart.
"
   :sample-data [:radar-chart-2-data-sub-demo :blackboard :topic.sample-data]
   :default-data chart/sample-data
   :data-tools data-tools/meta-tabular-data-sub-tools
   :source-code chart/source-code
   :component chart/component
   :data-panel chart-utils/meta-tabular-data-panel
   :config-panel chart/config-panel])


(defn- config-ratom []
  [config-ratom-example/example
   :container-id :radar-chart-2-config-ratom-demo
   :title "Radar Chart 2 (Live Configuration - ratom) - IN PROGRESS (not complete)"
   :description "A Radar Chart (2) built using [Recharts](https://recharts.org/en-US/api/RadarChart). This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a ratom to hold the configuration for the chart.
>
> You can use the buttons in the bottom-most panel to change some of the chart configuration options and see
> how that affects the data (shown in the gray panel) and how the chart responds."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :config-tools config-tools/meta-tabular-config-column-ratom-tools
   :component chart/component
   :default-config-data default-config-data])


(defn- config-structure []
  [config-structure-example/example
   :container-id :radar-chart-2-config-structure-demo
   :title "Radar Chart 2 (Live Configuration - structure) - IN PROGRESS (not complete)"
   :description "A Radar Chart (2) built using [Recharts](https://recharts.org/en-US/api/RadarChart). This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a plain data structure to hold the configuration for the chart.
>
> You can see the configuration data in the gray panel and how it how that affects the chart."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :component chart/component
   :default-config-data default-config-data])


(defn- config-sub []
  (let [container-id :radar-chart-2-config-sub-demo]
    [config-sub-example/example
     :container-id container-id
     :title "Radar Chart 2 (Live Configuration - subscription) - IN PROGRESS (not complete)"
     :description "A Radar Chart (2) built using [Recharts](https://recharts.org/en-US/api/RadarChart). This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the configuration for the chart."
     :sample-data chart/sample-data
     :source-code chart/source-code
     :config-tools config-tools/meta-tabular-config-column-sub-tools
     :component chart/component
     :config-data [container-id :blackboard :config-data]
     :default-config-data default-config-data]))


(defn examples []
  [me/examples {"data-ratom" [data-ratom]
                "data-struct"  [data-structure]
                "data-sub"  [data-sub]
                "config-ratom"  [config-ratom]
                "config-struct"  [config-structure]
                "config-sub"  [config-sub]}])
