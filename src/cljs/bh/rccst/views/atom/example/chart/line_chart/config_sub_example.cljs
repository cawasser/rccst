(ns bh.rccst.views.atom.example.chart.line-chart.config-sub-example
  (:require [bh.rccst.ui-component.atom.chart.line-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as e]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.helpers :as h]
            [reagent.core :as r]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]))

(log/info "bh.rccst.views.atom.example.chart.line-chart.data-sub-example")


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


(defn- show-config [config-data]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :background "#808080"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:p {:style {:color "white"}}
               (str @config-data)]]])


(defn- config-tools [config-data]
  (let [brush? (ui-utils/subscribe-local config-data [:brush])
        uv? (ui-utils/subscribe-local config-data [:uv :include])
        tv? (ui-utils/subscribe-local config-data [:tv :include])]

    (fn []
      [rc/h-box :src (rc/at)
       :gap "10px"
       :style {:border     "1px solid" :border-radius "3px"
               :box-shadow "5px 5px 5px 2px"
               :margin     "5px" :padding "5px"}
       :children [[:label.h5 "Config:"]
                  [rc/button :on-click #(h/handle-change-path config-data [] default-config-data) :label "Default"]
                  [rc/button :on-click #(h/handle-change-path config-data [:brush] (not @brush?))
                   :label "!Brush"]
                  [rc/button :on-click #(h/handle-change-path config-data [:uv :include] (not @uv?))
                   :label "! uv data"]
                  [rc/button :on-click #(h/handle-change-path config-data [:tv :include] (not @tv?))
                   :label "! tv data"]
                  [chart-utils/color-config config-data ":amt :fill" [:amt :fill] :above-center]
                  [rc/button :on-click #((h/handle-change-path config-data [:uv :stackId] "b")
                                         (h/handle-change-path config-data [:pv :stackId] "b"))
                   :label "stack uv/pv"]
                  [rc/button :on-click #((h/handle-change-path config-data [:uv :stackId] "")
                                         (h/handle-change-path config-data [:pv :stackId] ""))
                   :label "!stack uv/pv"]
                  [rc/button :on-click #((h/handle-change-path config-data [:tv :stackId] "a")
                                         (h/handle-change-path config-data [:amt :stackId] "a"))
                   :label "stack tv/amt"]
                  [rc/button :on-click #((h/handle-change-path config-data [:tv :stackId] "")
                                         (h/handle-change-path config-data [:amt :stackId] ""))
                   :label "!stack tv/amt"]]])))


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

              [show-config (h/resolve-value config-data)]

              [:div.config-tools-part {:style {:width "100%"}}
               [config-tools config-data]]]])


(defn- component [& {:keys [data config-data component-id container-id] :as params}]
  (let [id (r/atom nil)]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id (config default-config-data))
        (ui-utils/dispatch-local @id [:container] container-id))

      [config-update-example
       :data data
       :config-data config-data
       :component-id (h/path->keyword component-id "line-chart-2")
       :container-id component-id])))


(defn example []
  (let [container-id :line-chart-2-config-sub-demo
        component-id (h/path->keyword container-id "line-chart-2")]

    [e/component-example
     :title "line Chart 2 (Live Configuration - subscription)"
     :description "A line Chart (2) built using [Recharts](https://recharts.org/en-US/api/lineChart). This example shows how
     charts can take [subscriptions](https://day8.github.io/re-frame/subscriptions/) as input and re-render as the configuration changes.

> In _this_ case, we are using a subscription to handle the configuration for the chart.
"
     :data (r/atom chart/sample-data)
     :extra-params {:config-data [container-id :blackboard :config-data]}
     :component component
     :container-id ""
     :component-id container-id
     :source-code chart/source-code]))

