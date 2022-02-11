(ns bh.rccst.ui-component.atom.area-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            ["recharts" :refer [AreaChart Area Brush]]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [taoensso.timbre :as log]))


(defn config
  "constructs the configuration data structure for the widget. This is specific to this being a area-chart component.

  ---

  - widget-id : (string) id of the widget, in this specific case
  "
  [widget-id]
  (-> utils/default-config
    (merge
      {:tab-panel {:value     (keyword widget-id "config")
                   :data-path [:widgets (keyword widget-id) :tab-panel]}
       :brush     false
       :area-uv   {:include true :stroke "#8884d8" :fill "#8884d8" :stackId ""}
       :area-pv   {:include true :stroke "#82ca9d" :fill "#82ca9d" :stackId ""}
       :area-amt  {:include true :stroke "#5974ab" :fill "#5974ab" :stackId ""}
       :area-d    {:include true :stroke "#3db512" :fill "#3db512" :stackId ""}})
    (assoc-in [:x-axis :dataKey] :name)))


(defn- area-config [widget-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config widget-id label (conj path :include)]
              [utils/color-config widget-id ":fill" (conj path :fill) :above-right]
              [utils/color-config widget-id ":stroke" (conj path :stroke) :above-left]
              [utils/text-config widget-id ":stackId" (conj path :stackId)]]])


(defn config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.\n  - config : (atom) holds all the configuration settings made by the user
  "
  [data widget-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data widget-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[area-config widget-id "area (uv)" [:area-uv] :above-right]
                          [area-config widget-id "area (pv)" [:area-pv] :above-center]
                          [area-config widget-id "area (amt)" [:area-amt] :above-center]
                          [area-config widget-id "area (d)" [:area-d] :above-left]]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/boolean-config widget-id ":brush?" [:brush]]]])


(def source-code "dummy area Chart Code")


(defn component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - widget-id : (string) unique identifier for this specific widget
  "
  [data widget-id]
  (let [area-uv? (ui-utils/subscribe-local widget-id [:area-uv :include])
        area-uv-fill (ui-utils/subscribe-local widget-id [:area-uv :fill])
        area-uv-stroke (ui-utils/subscribe-local widget-id [:area-uv :stroke])
        area-uv-stackId (ui-utils/subscribe-local widget-id [:area-uv :stackId])
        area-pv? (ui-utils/subscribe-local widget-id [:area-pv :include])
        area-pv-fill (ui-utils/subscribe-local widget-id [:area-pv :fill])
        area-pv-stroke (ui-utils/subscribe-local widget-id [:area-pv :stroke])
        area-pv-stackId (ui-utils/subscribe-local widget-id [:area-pv :stackId])
        area-amt? (ui-utils/subscribe-local widget-id [:area-amt :include])
        area-amt-fill (ui-utils/subscribe-local widget-id [:area-amt :fill])
        area-amt-stroke (ui-utils/subscribe-local widget-id [:area-amt :stroke])
        area-amt-stackId (ui-utils/subscribe-local widget-id [:area-amt :stackId])
        area-d? (ui-utils/subscribe-local widget-id [:area-d :include])
        area-d-fill (ui-utils/subscribe-local widget-id [:area-d :fill])
        area-d-stroke (ui-utils/subscribe-local widget-id [:area-d :stroke])
        area-d-stackId (ui-utils/subscribe-local widget-id [:area-d :stackId])
        isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
        brush? (ui-utils/subscribe-local widget-id [:brush])]

    (fn []
      [c/chart
       [:> AreaChart {:width 400 :height 400 :data @data}

        (utils/standard-chart-components widget-id)

        (when @brush? [:> Brush])

        (when @area-uv? [:> Area (merge {:type              "monotone" :dataKey :uv
                                         :isAnimationActive @isAnimationActive?
                                         :stroke            @area-uv-stroke
                                         :fill              @area-uv-fill}
                                   (when (seq @area-uv-stackId) {:stackId @area-uv-stackId}))])

        (when @area-pv? [:> Area (merge {:type              "monotone" :dataKey :pv
                                         :isAnimationActive @isAnimationActive?
                                         :stroke            @area-pv-stroke
                                         :fill              @area-pv-fill}
                                   (when (seq @area-pv-stackId) {:stackId @area-pv-stackId}))])

        (when @area-amt? [:> Area (merge {:type              "monotone" :dataKey :amt
                                          :isAnimationActive @isAnimationActive?
                                          :stroke            @area-amt-stroke
                                          :fill              @area-amt-fill}
                                    (when (seq @area-amt-stackId) {:stackId @area-amt-stackId}))])

        (when @area-d? [:> Area (merge {:type              "monotone" :dataKey :d
                                        :isAnimationActive @isAnimationActive?
                                        :stroke            @area-d-stroke
                                        :fill              @area-d-fill}
                                  (when (seq @area-d-stackId) {:stackId @area-d-stackId}))])]])))