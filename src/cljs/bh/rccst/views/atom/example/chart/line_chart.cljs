(ns bh.rccst.views.atom.example.chart.line-chart
  (:require [bh.rccst.ui-component.atom.chart.line-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.views.atom.example.chart.alt.config-ratom-example :as config-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.config-structure-example :as config-structure-example]
            [bh.rccst.views.atom.example.chart.alt.config-sub-example :as config-sub-example]
            [bh.rccst.views.atom.example.chart.alt.data-ratom-example :as data-ratom-example]
            [bh.rccst.views.atom.example.chart.alt.data-structure-example :as data-structure-example]
            [bh.rccst.views.atom.example.chart.alt.data-sub-example :as data-sub-example]
            [bh.rccst.views.atom.example.chart.alt.data-tools :as data-tools]
            [bh.rccst.views.atom.example.chart.alt.config-tools :as config-tools]))


(def default-config-data {:brush false
                          :uv    {:include true, :fill "#ff0000", :stackId ""}
                          :pv    {:include true, :fill "#00ff00", :stackId ""}
                          :tv    {:include true, :fill "#0000ff", :stackId "a"}
                          :amt   {:include true, :fill "#745ea5", :stackId "a"}})


(defn- data-ratom []
  [data-ratom-example/example
   :container-id :line-chart-data-ratom-demo
   :title "Line Chart (Live Data - ratom)"
   :description "A Line Chart built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
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
   :container-id :line-chart-data-structure-demo
   :title "Line Chart (Live Data - structure)"
   :description "A line Chart built using [Recharts](https://recharts.org/en-US/api/lineChart). This example shows how
   charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

   > In _this_ case, we are using a plain data structure for the data, so there is no way to update it (it lives
   > only inside the chart, with no way to get at it from outside)."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :component chart/component
   :data-panel chart-utils/meta-tabular-data-panel
   :config-panel chart/config-panel])


(defn data-sub []
  (let [container-id :line-chart-data-sub-demo]
    [data-sub-example/example
     :container-id container-id
     :title "Line Chart (Live data - subscription)"
     :description "A line Chart built using [Recharts](https://recharts.org/en-US/api/lineChart). This example shows how
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
   :container-id :line-chart-config-ratom-demo
   :title "Line Chart (Live Configuration - ratom)"
   :description "A Line Chart built using [Recharts](https://recharts.org/en-US/api/lineChart). This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a ratom to hold the configuration for the chart.
>
> You can use the buttons in the bottom-most panel to change some of the chart configuration options and see
> how that affects the data (shown in the gray panel) and how the chart responds.

> Note: Line charts CANNOT stack!"
   :sample-data chart/sample-data
   :config-tools config-tools/meta-tabular-config-column-ratom-tools
   :source-code chart/source-code
   :component chart/component
   :default-config-data default-config-data])


(defn- config-structure []
  [config-structure-example/example
   :container-id :line-chart-config-structure-demo
   :title "Line Chart (Live Configuration - structure)"
   :description "A Line Chart built using [Recharts](https://recharts.org/en-US/api/lineChart). This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a plain data structure to hold the configuration for the chart.
>
> You can see the configuration data in the gray panel and how it how that affects the chart."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :component chart/component
   :default-config-data default-config-data])


(defn- config-sub []
  (let [container-id :line-chart-config-sub-demo]
    [config-sub-example/example
     :container-id container-id
     :title "Line Chart (Live Configuration - subscription)"
     :description "A Line Chart built using [Recharts](https://recharts.org/en-US/api/lineChart). This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the configuration for the chart.

> Note: Line charts CANNOT stack!"
     :sample-data chart/sample-data
     :config-tools config-tools/meta-tabular-config-column-sub-tools
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
