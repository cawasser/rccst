(ns bh.rccst.views.atom.example.chart.line-chart.config-ratom-example
  (:require [bh.rccst.ui-component.atom.chart.line-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.line-chart.config-ratom-example")


(def default-config-data {:brush false
                          :uv    {:include true, :fill "#ff0000", :stackId ""}
                          :pv    {:include true, :fill "#00ff00", :stackId ""}
                          :tv    {:include true, :fill "#0000ff", :stackId ""}
                          :amt   {:include true, :fill "#745ea5", :stackId ""}})
(defonce config-data (r/atom default-config-data))



(defn- show-config [config-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :background "#808080"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:p {:style {:color "white"}}
               (str @config-data)]]])


(defn- config-tools []
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Config:"]
              [rc/button :on-click #(reset! config-data default-config-data) :label "Default"]
              [rc/button :on-click #(swap! config-data update-in [:brush] not) :label "!Brush"]
              [rc/button :on-click #(swap! config-data update-in [:uv :include] not) :label "! uv data"]
              [rc/button :on-click #(swap! config-data update-in [:tv :include] not) :label "! tv data"]
              [chart-utils/color-config config-data ":amt :fill" [:amt :fill] :above-center]
              [rc/button :on-click #(reset! config-data (-> @config-data
                                                            (assoc-in [:uv :stackId] "a")
                                                            (assoc-in [:tv :stackId] "a")))
               :label "stack"]
              [rc/button :on-click #(reset! config-data (-> @config-data
                                                            (assoc-in [:uv :stackId] "")
                                                            (assoc-in [:tv :stackId] "")))
               :label "un-stack"]]])


(defn- config-update-example [& {:keys [data config-data container-id component-id] :as params}]
  ;(log/info "config-update-example (params)" params)

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

              [show-config config-data]

              [:div.config-tools-part {:style {:width "100%"}}
               [config-tools]]]])


(defn example []
  (let [container-id "line-chart-2-config-ratom-demo"
        component-id (utils/path->keyword container-id "line-chart-2")]
    [example/component-example
     :title "line Chart 2 (Live Configuration - ratom)"
     :description "A line Chart (2) built using [Recharts](https://recharts.org/en-US/api/lineChart). This example shows how
     charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the configuration changes.

> In _this_ case, we are using a ratom to hold the configuration for the chart.
>
> You can use the buttons in the bottom-most panel to change some of the chart configuration options and see
> how that affects the data (shown in the gray panel) and how the chart responds."
     :data (r/atom chart/sample-data)
     :extra-params {:config-data config-data}
     :component config-update-example
     :container-id container-id
     :component-id component-id
     :source-code chart/source-code]))

