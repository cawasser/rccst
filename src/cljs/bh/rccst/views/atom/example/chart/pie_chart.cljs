(ns bh.rccst.views.atom.example.chart.pie-chart
  (:require [bh.rccst.ui-component.atom.chart.pie-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.views.atom.example.chart.alt.data-ratom-example :as data-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.data-structure-example :as data-structure-example]
            [bh.rccst.views.atom.example.chart.alt.data-sub-example :as data-sub-example]
            [bh.rccst.views.atom.example.chart.alt.config-ratom-example :as config-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.config-structure-example :as config-structure-example]
            [bh.rccst.views.atom.example.chart.alt.config-sub-example :as config-sub-example]
            [bh.rccst.views.atom.example.chart.alt.data-tools :as data-tools]
            [bh.rccst.views.atom.example.chart.alt.config-tools :as config-tools]))


(def default-config-data {:name [:Page-A :Page-B :Page-C :Page-D :Page-E :Page-F :Page-G]
                          :fill "#8e81ce"
                          :value {:keys [:uv :pv :tv :amt] :chosen :uv}})


(defn- data-ratom []
  [data-ratom-example/example
   :container-id :pie-chart-data-ratom-demo
   :title "Pie Chart (Live Data - ratom)"
   :description "A Pie Chart built using [Recharts](https://recharts.org/en-US/api/PieChart) with colored Cells. This example shows how
  charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

  > In _this_ case, we are using a ratom for the data.
  >
  > You can use the buttons below to change some of the data and see how the chart responds."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :data-tools data-tools/meta-tabular-data-ratom-tools
   :component chart/component
   :data-panel chart-utils/meta-tabular-data-panel
   :config-panel chart/config-panel])


(defn- data-structure []
  [data-structure-example/example
   :container-id :pie-chart-2-data-structure-demo
   :title "Pie Chart 2 (Live Data - structure)"
   :description "Pie Chart (2) built using [Recharts](https://recharts.org/en-US/api/pieChart). This example shows how
  charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

  > In _this_ case, we are using a plain data structure for the data, so there is no way to update it (it lives
  > only inside the chart, with no way to get at it from outside)."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :data-tools data-tools/meta-tabular-data-ratom-tools
   :component chart/component
   :data-panel chart-utils/meta-tabular-data-panel
   :config-panel chart/config-panel])


(defn data-sub []
  [data-sub-example/example
   :container-id :pie-chart-2-data-sub-demo
   :title "Pie Chart 2 (Live Data - subscription)"
   :description "Pie Chart (2) built using [Recharts](https://recharts.org/en-US/api/pieChart). This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the data for the chart.
"
   :sample-data [:pie-chart-2-data-sub-demo :blackboard :topic.sample-data]
   :default-data chart/sample-data
   :data-tools data-tools/meta-tabular-data-sub-tools
   :source-code chart/source-code
   :component chart/component
   :data-panel chart-utils/meta-tabular-data-panel
   :config-panel chart/config-panel])


(defn- config-ratom []
  [config-ratom-example/example
   :container-id :pie-chart-2-config-ratom-demo
   :title "Pie Chart 2 (Live Configuration - ratom)"
   :description "Pie Chart (2) built using [Recharts](https://recharts.org/en-US/api/pieChart). This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a ratom to hold the configuration for the chart.
>
> You can use the buttons in the bottom-most panel to change some of the chart configuration options and see
> how that affects the data (shown in the gray panel) and how the chart responds."
   :sample-data chart/sample-data
   :config-tools config-tools/meta-tabular-config-pie-row-ratom-tools
   :source-code chart/source-code
   :component chart/component
   :default-config-data default-config-data])


(defn- config-structure []
  [config-structure-example/example
   :container-id :pie-chart-2-config-structure-demo
   :title "Pie Chart 2 (Live Configuration - structure)"
   :description "Pie Chart (2) built using [Recharts](https://recharts.org/en-US/api/pieChart). This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a plain data structure to hold the configuration for the chart.
>
> You can see the configuration data in the gray panel and how that affects the chart."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :component chart/component
   :default-config-data default-config-data])


(defn- config-sub []
  (let [container-id :pie-chart-2-config-sub-demo]
    [config-sub-example/example
     :container-id container-id
     :title "Pie Chart 2 (Live Configuration - subscription)"
     :description " Pie Chart (2) built using [Recharts](https://recharts.org/en-US/api/pieChart). This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the configuration for the chart."
     :sample-data chart/sample-data
     :config-tools config-tools/meta-tabular-config-pie-row-sub-tools
     :source-code chart/source-code
     :component chart/component
     :config-data [container-id :blackboard :config-data]
     :default-config-data default-config-data]))



(defn examples []
  [:div
   [data-ratom]
   [data-structure]
   [data-sub]
   [config-ratom]
   [config-structure]
   [config-sub]])

