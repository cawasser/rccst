(ns bh.rccst.ui-component.atom.area-chart
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]
            [reagent.core :as r]
            ["recharts" :refer [AreaChart Area Brush]]

            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]))


(def sample-data
  "the Area Chart works best with \"tabular data\" so we return the tabular-data from utils,
  and we mix-in a fourth column just to show how it can be done"
  (r/atom (mapv (fn [d] (assoc d :d (rand-int 5000))) utils/tabular-data)))


(defn config
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a line-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. area charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - chart-id : (string) unique id of the chart
  "
  [chart-id]
  (-> utils/default-config
    (merge
      {:tab-panel {:value     (keyword chart-id "config")
                   :data-path [:widgets (keyword chart-id) :tab-panel]}
       :brush     false
       :area-uv   {:include true :stroke "#8884d8" :fill "#8884d8" :stackId ""}
       :area-pv   {:include true :stroke "#82ca9d" :fill "#82ca9d" :stackId ""}
       :area-amt  {:include true :stroke "#5974ab" :fill "#5974ab" :stackId ""}
       :area-d    {:include true :stroke "#3db512" :fill "#3db512" :stackId ""}})
    (assoc-in [:x-axis :dataKey] :name)))


(defn- area-config
  "construct ui-component(s) for setting the properties of an 'area', specifically:

  - `:fill`
  - `:stroke`
  - `:stackId`

  ---

  - chart-id : (string) unique identifier for his chart instance
  - label : (string) label to use for this `area`
  - path : (vector of keywords) path to the data inside the config data structure
  - position : (keyword) position to place the re-com/popover that shows the `color-picker`

> See also:
>
> [Re-com/popover](https://re-com.day8.com.au/#/popovers)
  "
  [chart-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config chart-id label (conj path :include)]
              [utils/color-config chart-id ":fill" (conj path :fill) :above-right]
              [utils/color-config chart-id ":stroke" (conj path :stroke) :above-left]
              [utils/text-config chart-id ":stackId" (conj path :stackId)]]])


(defn config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.\n  - config : (atom) holds all the configuration settings made by the user
  "
  [data chart-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data chart-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[area-config chart-id "area (uv)" [:area-uv] :above-right]
                          [area-config chart-id "area (pv)" [:area-pv] :above-center]
                          [area-config chart-id "area (amt)" [:area-amt] :above-center]
                          [area-config chart-id "area (d)" [:area-d] :above-left]]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/boolean-config chart-id ":brush?" [:brush]]]])


(def source-code "dummy area Chart Code")


(defn component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - widget-id : (string) unique identifier for this specific widget
  "
  [data chart-id]
  (let [area-uv? (ui-utils/subscribe-local chart-id [:area-uv :include])
        area-uv-fill (ui-utils/subscribe-local chart-id [:area-uv :fill])
        area-uv-stroke (ui-utils/subscribe-local chart-id [:area-uv :stroke])
        area-uv-stackId (ui-utils/subscribe-local chart-id [:area-uv :stackId])
        area-pv? (ui-utils/subscribe-local chart-id [:area-pv :include])
        area-pv-fill (ui-utils/subscribe-local chart-id [:area-pv :fill])
        area-pv-stroke (ui-utils/subscribe-local chart-id [:area-pv :stroke])
        area-pv-stackId (ui-utils/subscribe-local chart-id [:area-pv :stackId])
        area-amt? (ui-utils/subscribe-local chart-id [:area-amt :include])
        area-amt-fill (ui-utils/subscribe-local chart-id [:area-amt :fill])
        area-amt-stroke (ui-utils/subscribe-local chart-id [:area-amt :stroke])
        area-amt-stackId (ui-utils/subscribe-local chart-id [:area-amt :stackId])
        area-d? (ui-utils/subscribe-local chart-id [:area-d :include])
        area-d-fill (ui-utils/subscribe-local chart-id [:area-d :fill])
        area-d-stroke (ui-utils/subscribe-local chart-id [:area-d :stroke])
        area-d-stackId (ui-utils/subscribe-local chart-id [:area-d :stackId])
        isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])
        brush? (ui-utils/subscribe-local chart-id [:brush])]

    (fn []
      [c/chart
       [:> AreaChart {:width 400 :height 400 :data @data}

        (utils/standard-chart-components chart-id)

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
