(ns bh.rccst.views.atom.example.chart.bar-chart.data-sub-example
  (:require [bh.rccst.ui-component.atom.chart.bar-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as e]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.bar-chart.data-sub-example")


(defn- config [data]
  ; TODO: notice how we need to use '.' instead of '/' for this :topic? how can we fix this
  ; which causes an issue with subscriptions and resolve-value
  {:blackboard {:topic.sample-data data}
   :container  ""})


(defn- component [& {:keys [data component-id container-id] :as params}]
  (let [id (r/atom nil)]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id (config chart/sample-data))
        (ui-utils/dispatch-local @id [:container] container-id))

      [chart/component
       :data data
       :component-id (h/path->keyword component-id "bar-chart-2")
       :container-id component-id
       :data-panel chart-utils/meta-tabular-data-panel
       :config-panel chart/config-panel])))


(defn example []
  (let [container-id :bar-chart-2-data-sub-demo
        component-id (h/path->keyword container-id "bar-chart-2")]

    [e/component-example
     :title "Bar Chart 2 (Live data - subscription)"
     :description "A Bar Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the data for the chart.
"
     :data [container-id :blackboard :topic.sample-data]
     :component component
     :container-id ""
     :component-id container-id
     :source-code chart/source-code]))



(comment
  (re-frame/subscribe [:bar-chart-2-data-sub-demo])
  (re-frame/subscribe [:bar-chart-2-data-sub-demo.blackboard])
  (re-frame/subscribe [:bar-chart-2-data-sub-demo.blackboard.topic.sample-data])
  (re-frame/subscribe [:bar-chart-2-data-sub-demo.blackboard.topic.sample-data.metadata])
  (re-frame/subscribe [:bar-chart-2-data-sub-demo.blackboard.topic.sample-data.data])



  (re-frame/subscribe [:bar-chart-2-data-sub-demo :blackboard.topic.sample-data])


  (= @(re-frame/subscribe [:bar-chart-2-data-sub-demo])
    @ (re-frame/subscribe [:bar-chart-2-data-sub-demo :blackboard :topic.sample-data]))


  ())

