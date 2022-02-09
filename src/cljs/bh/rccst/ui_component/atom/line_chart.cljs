(ns bh.rccst.ui-component.atom.line-chart
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]

            ["recharts" :refer [LineChart Line Brush]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]))




(defn config
  "constructs the configuration data structure for the widget. This is specific to this being a
  line-chart component.

> Note: it is possible to make this be a common configuration for ANY line-chart use

  ---

  - widget-id : (string) id of the widget,
  "
  [widget-id]
  (-> utils/default-config
    (merge
      {:tab-panel {:value     (keyword widget-id "config")
                   :data-path [:widgets (keyword widget-id) :tab-panel]}
       :brush     false
       :line-uv   {:include true :stroke "#8884d8" :fill "#8884d8"}
       :line-pv   {:include true :stroke "#82ca9d" :fill "#82ca9d"}
       :line-amt  {:include false :stroke "#ff00ff" :fill "#ff00ff"}})
    (assoc-in [:x-axis :dataKey] :name)))


(defn- line-config [widget-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config widget-id label (conj path :include)]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[utils/color-config widget-id ":stroke" (conj path :stroke) position]
                          [utils/color-config widget-id ":fill" (conj path :fill) position]]]]])


(defn config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.
  - config : (atom) holds all the configuration settings made by the user
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
               :children [[line-config widget-id "line (uv)" [:line-uv] :above-right]
                          [line-config widget-id "line (pv)" [:line-pv] :above-right]
                          [line-config widget-id "line (amt)" [:line-amt] :above-center]]]
              [utils/boolean-config widget-id ":brush?" [:brush]]]])


(defn component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - widget-id : (string) unique identifier for this specific widget instanc
  "
  [data widget-id]
  (let [line-uv? (ui-utils/subscribe-local widget-id [:line-uv :include])
        line-uv-stroke (ui-utils/subscribe-local widget-id [:line-uv :stroke])
        line-uv-fill (ui-utils/subscribe-local widget-id [:line-uv :fill])
        line-pv? (ui-utils/subscribe-local widget-id [:line-pv :include])
        line-pv-stroke (ui-utils/subscribe-local widget-id [:line-pv :stroke])
        line-pv-fill (ui-utils/subscribe-local widget-id [:line-pv :fill])
        line-amt? (ui-utils/subscribe-local widget-id [:line-amt :include])
        line-amt-stroke (ui-utils/subscribe-local widget-id [:line-amt :stroke])
        line-amt-fill (ui-utils/subscribe-local widget-id [:line-amt :fill])
        isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
        brush? (ui-utils/subscribe-local widget-id [:brush])]

    (fn []
      ;(log/info "configurable-chart" @config)

      [c/chart
       [:> LineChart {:width 400 :height 400 :data @data}

        (utils/standard-chart-components widget-id)

        (when @brush? [:> Brush])

        (when @line-uv? [:> Line {:type              "monotone" :dataKey :uv
                                  :isAnimationActive @isAnimationActive?
                                  :stroke            @line-uv-stroke
                                  :fill              @line-uv-fill}])

        (when @line-pv? [:> Line {:type              "monotone" :dataKey :pv
                                  :isAnimationActive @isAnimationActive?
                                  :stroke            @line-pv-stroke
                                  :fill              @line-pv-fill}])

        (when @line-amt? [:> Line {:type              "monotone" :dataKey :amt
                                   :isAnimationActive @isAnimationActive?
                                   :stroke            @line-amt-stroke
                                   :fill              @line-amt-fill}])]])))

