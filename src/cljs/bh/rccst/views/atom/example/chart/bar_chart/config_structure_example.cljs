(ns bh.rccst.views.atom.example.chart.bar-chart.config-structure-example
  (:require [bh.rccst.ui-component.atom.chart.bar-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.ui-component.utils :as utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.bar-chart.config-structure-example")


(def config-data {:brush false
                  :uv    {:include true, :fill "#ff0000", :stackId ""}
                  :pv    {:include false, :fill "#00ff00", :stackId ""}
                  :tv    {:include true, :fill "#0000ff", :stackId "a"}
                  :amt   {:include true, :fill "#745ea5", :stackId "a"}})



(defn- show-config [config-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :background "#808080"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:p {:style {:color "white"}}
               (str @config-data)]]])


(defn- config-example [& {:keys [data config-data container-id component-id] :as params}]
  (log/info "config-example (params)" params)

  (let [c (h/resolve-value config-data)]
    [rc/v-box :src (rc/at)
     :gap "10px"
     :width "100%"
     :height "100%"
     :children [[:div.chart-part {:style {:width "100%" :height "90%"}}
                 [chart/component
                  :data data
                  :config-data config-data
                  :component-id component-id
                  :container-id container-id
                  :component-panel chart/component]]

                [show-config c]]]))


(defn example []
  (let [container-id "bar-chart-2-config-ratom-demo"
        component-id (utils/path->keyword container-id "bar-chart-2")]
    [example/component-example
     :title "Bar Chart 2 (Live Configuration - structure)"
     :description "A Bar Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a plain data structure to hold the configuration for the chart.
>
> You can see the configuration data in the gray panel and how it how that affects the chart."
     :data chart/sample-data
     :extra-params {:config-data config-data}
     :component config-example
     :container-id container-id
     :component-id component-id
     :source-code chart/source-code]))


