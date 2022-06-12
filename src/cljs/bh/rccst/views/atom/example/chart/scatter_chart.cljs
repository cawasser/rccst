(ns bh.rccst.views.atom.example.chart.scatter-chart
  (:require [bh.rccst.ui-component.atom.chart.scatter-chart :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.views.atom.example.chart.alt.data-tools :as data-tools]
            [bh.rccst.views.atom.example.chart.alt.config-tools :as config-tools]
            [bh.rccst.views.atom.example.chart.alt.data-ratom-example :as data-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.data-structure-example :as data-structure-example]
            [bh.rccst.views.atom.example.chart.alt.data-sub-example :as data-sub-example]
            [bh.rccst.views.atom.example.chart.alt.config-ratom-example :as config-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.config-structure-example :as config-structure-example]
            [bh.rccst.views.atom.example.chart.alt.config-sub-example :as config-sub-example]
            [taoensso.timbre :as log]
            [bh.rccst.views.atom.example.multi-example :as me]))


(log/info "bh.rccst.views.atom.example.chart.scatter-chart")


(def default-config-data {:Page-A {:name "Page A" :include true :color "#8884d8"}
                          :Page-B {:name "Page B" :include true :color "#ffc107"}
                          :Page-C {:name "Page C" :include true :color "#82ca9d"}
                          :Page-D {:name "Page D" :include true :color "#ff00ff"}
                          :Page-E {:name "Page E" :include true :color "#00e5ff"}
                          :Page-F {:name "Page F" :include true :color "#4db6ac"}
                          :Page-G {:name "Page G" :include true :color "#83a6ed"}
                          :values {:keys [:Page-A :Page-B :Page-C
                                          :Page-D :Page-E :Page-F :Page-G]
                                   :x :uv :y :pv :z :amt}})


(defn- data-ratom []
  [data-ratom-example/example
   :container-id :scatter-chart-data-ratom-demo
   :title "Scatter Chart (Live Data - ratom) - IN PROGRESS (not complete)"
   :description "A Scatter Chart built using [Recharts](https://recharts.org/en-US/api/ScatterChart). This example shows how
  charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

  > In _this_ case, we are using a ratom for the data.
  >
  > You can use the buttons below to change some of the data and see how the chart responds."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :data-tools data-tools/meta-tabular-data-ratom-tools
   :source-code chart/source-code
   :component chart/component
   :data-panel chart-utils/meta-tabular-data-panel
   :config-panel chart/config-panel])


(defn- data-structure []
  [data-structure-example/example
   :container-id :scatter-chart-data-ratom-demo
   :title "Scatter Chart (Live Data - structure) - IN PROGRESS (not complete)"
   :description "A Scatter Chart built using [Recharts](https://recharts.org/en-US/api/ScatterChart) with colored Cells. This example shows how
  charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

  > In _this_ case, we are using a plain data structure for the data, so there is no way to update it (it lives
  > only inside the chart, with no way to get at it from outside)."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :component chart/component
   :data-panel chart-utils/meta-tabular-data-panel
   :config-panel chart/config-panel])


(defn data-sub []
  (let [container-id :scatter-chart-data-ratom-demo]
    [data-sub-example/example
     :container-id container-id
     :title "Scatter Chart (Live Data - subscription) - IN PROGRESS (not complete)"
     :description "A Scatter Chart built using [Recharts](https://recharts.org/en-US/api/ScatterChart) with colored Cells. This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the data for the chart.
"
     :sample-data [container-id :blackboard :topic.sample-data]
     :default-data chart/sample-data
     :data-tools data-tools/meta-tabular-data-sub-tools
     :source-code chart/source-code
     :component chart/component
     :data-panel chart-utils/meta-tabular-data-panel
     :config-panel chart/config-panel]))


(defn- config-ratom []
  [config-ratom-example/example
   :container-id :scatter-chart-data-ratom-demo
   :title "Scatter Chart (Live Configuration - ratom) - IN PROGRESS (not complete)"
   :description "A Scatter Chart built using [Recharts](https://recharts.org/en-US/api/ScatterChart) with colored Cells. This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a ratom to hold the configuration for the chart.
>
> You can use the buttons in the bottom-most panel to change some of the chart configuration options and see
> how that affects the data (shown in the gray panel) and how the chart responds."
   :sample-data chart/sample-data
   :config-tools config-tools/meta-tabular-config-row-ratom-tools
   :source-code chart/source-code
   :component chart/component
   :default-config-data default-config-data])


(defn- config-structure []
  [config-structure-example/example
   :container-id :scatter-config-structure-demo
   :title "Scatter Chart (Live Configuration - structure) - IN PROGRESS (not complete)"
   :description "A Scatter Chart built using [Recharts](https://recharts.org/en-US/api/ScatterChart) with colored Cells. This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a plain data structure to hold the configuration for the chart.
>
> You can see the configuration data in the gray panel and how it how that affects the chart."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :component chart/component
   :default-config-data default-config-data])


(defn- config-sub []
  (let [container-id :scatter-config-sub-demo]
    [config-sub-example/example
     :container-id container-id
     :title "Scatter Chart (Live Configuration - subscription) - IN PROGRESS (not complete)"
     :description "A Scatter Chart built using [Recharts](https://recharts.org/en-US/api/ScatterChart) with colored Cells. This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the configuration for the chart."
     :sample-data chart/sample-data
     :config-tools config-tools/meta-tabular-config-row-sub-tools ; TODO: this tool panel does not have tools for the axes
     :source-code chart/source-code
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
