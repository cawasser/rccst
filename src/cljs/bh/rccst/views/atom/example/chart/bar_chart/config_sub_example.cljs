(ns bh.rccst.views.atom.example.chart.bar-chart.config-sub-example
  (:require [bh.rccst.ui-component.atom.chart.bar-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as e]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]))

(log/info "bh.rccst.views.atom.example.chart.bar-chart.data-sub-example")


(def default-config-data {:brush true
                          :uv    {:include true, :fill "#ff0000", :stackId "b"}
                          :pv    {:include true, :fill "#228B22", :stackId "b"}
                          :tv    {:include true, :fill "#ADD8E6", :stackId "a"}
                          :amt   {:include true, :fill "#800000", :stackId "a"}})

(defn- config [config-data]
  ; TODO: notice how we need to use '.' instead of '/' for this :topic? how can we fix this
  ; which causes an issue with subscriptions and resolve-value
  {:blackboard {:config-data config-data}
   :container  ""})


(defn- component [& {:keys [data config-data component-id container-id] :as params}]
  (let [id (r/atom nil)]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id (config default-config-data))
        (ui-utils/dispatch-local @id [:container] container-id))

      [chart/component
       :data data
       :config-data config-data
       :component-id (h/path->keyword component-id "bar-chart-2")
       :container-id component-id])))


(defn example []
  (let [container-id :bar-chart-2-config-sub-demo
        component-id (h/path->keyword container-id "bar-chart-2")]

    [e/component-example
     :title "Bar Chart 2 (Live Configuration - subscription) (WIP)"
     :description "A Bar Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the configuration for the chart.
"
     :data (r/atom chart/sample-data)
     :extra-params {:config-data [container-id :blackboard :config-data]}
     :component component
     :container-id ""
     :component-id container-id
     :source-code chart/source-code]))


