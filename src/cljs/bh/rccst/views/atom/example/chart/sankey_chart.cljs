(ns bh.rccst.views.atom.example.chart.sankey-chart
  (:require [bh.rccst.ui-component.atom.chart.sankey-chart :as chart]
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


(log/info "bh.rccst.views.atom.example.chart.sankey-chart-2")


(def default-config-data {:Visit            {:include true :fill "#ff0000" :stroke "#ff0000"}
                          :Direct-Favourite {:include true :fill "#00ff00" :stroke "#00ff00"}
                          :Page-Click       {:include true :fill "#0000ff" :stroke "#0000ff"}
                          :Detail-Favourite {:include true :fill "#12a4a4" :stroke "#12a4a4"}
                          :Lost             {:include true :fill "#ba7b47" :stroke "#ba7b47"}})


(defn- data-ratom []
  [data-ratom-example/example
   :container-id :sankey-chart-data-ratom-demo
   :title "Sankey Chart (Live Data - ratom)"
   :description "A Sankey Chart built using [Recharts](https://recharts.org/en-US/api/SankeyChart). This example shows how
  charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

  > In _this_ case, we are using a ratom for the data.
  >
  > You can use the buttons below to change some of the data and see how the chart responds."
   :sample-data chart/sample-data
   :data-tools data-tools/dag-data-ratom-tools
   :source-code chart/source-code
   :component chart/component
   :data-panel chart-utils/dag-data-panel
   :config-panel chart/config-panel
   :link-color-fn chart/color-white->target])


(defn- data-structure []
  [data-structure-example/example
   :container-id :sankey-chart-data-ratom-demo
   :title "Sankey Chart (Live Data - structure)"
   :description "A Sankey Chart built using [Recharts](https://recharts.org/en-US/api/SankeyChart) with colored Cells. This example shows how
  charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

  > In _this_ case, we are using a plain data structure for the data, so there is no way to update it (it lives
  > only inside the chart, with no way to get at it from outside)."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :component chart/component
   :data-panel chart-utils/dag-data-panel
   :config-panel chart/config-panel
   :link-color-fn chart/color-source->white])


(defn data-sub []
  (let [container-id :sankey-chart-data-ratom-demo]
    [data-sub-example/example
     :container-id container-id
     :title "Sankey Chart (Live Data - subscription)"
     :description "A Sankey Chart built using [Recharts](https://recharts.org/en-US/api/SankeyChart) with colored Cells. This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the data for the chart.
"
     :sample-data [container-id :blackboard :topic.sample-data]
     :default-data chart/sample-data
     :data-tools data-tools/dag-data-sub-tools
     :source-code chart/source-code
     :component chart/component
     :data-panel chart-utils/dag-data-panel
     :config-panel chart/config-panel
     :link-color-fn chart/color-source->target]))


(defn- config-ratom []
  [config-ratom-example/example
   :container-id :sankey-chart-data-ratom-demo
   :title "Sankey Chart (Live Configuration - ratom)"
   :description "A Sankey Chart built using [Recharts](https://recharts.org/en-US/api/SankeyChart) with colored Cells. This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a ratom to hold the configuration for the chart.
>
> You can use the buttons in the bottom-most panel to change some of the chart configuration options and see
> how that affects the data (shown in the gray panel) and how the chart responds."
   :sample-data chart/sample-data
   :config-tools config-tools/dag-data-config-ratom-tools
   :source-code chart/source-code
   :component chart/component
   :default-config-data default-config-data])


(defn- config-structure []
  [config-structure-example/example
   :container-id :sankey-config-structure-demo
   :title "Sankey Chart (Live Configuration - structure)"
   :description "A Sankey Chart built using [Recharts](https://recharts.org/en-US/api/SankeyChart) with colored Cells. This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a plain data structure to hold the configuration for the chart.
>
> You can see the configuration data in the gray panel and how it how that affects the chart."
   :sample-data chart/sample-data
   :source-code chart/source-code
   :component chart/component
   :default-config-data default-config-data])


(defn- config-sub []
  (let [container-id :sankey-config-sub-demo]
    [config-sub-example/example
     :container-id container-id
     :title "Sankey Chart (Live Configuration - subscription)"
     :description "A Sankey Chart built using [Recharts](https://recharts.org/en-US/api/SankeyChart) with colored Cells. This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the configuration for the chart."
     :sample-data chart/sample-data
     :config-tools config-tools/dag-data-config-sub-tools
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
