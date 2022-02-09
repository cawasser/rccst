(ns bh.rccst.ui-component.atom.scatter-chart
  (:require [re-com.core :as rc]
            [taoensso.timbre :as log]

            ["recharts" :refer [ScatterChart Scatter Brush]]

            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))


(defn config [widget-id]
  (-> utils/default-config
    (merge {:tab-panel {:value     (keyword widget-id "config")
                        :data-path [:widgets (keyword widget-id) :tab-panel]}
            :brush     false})
    (assoc-in [:x-axis :dataKey] :x)
    (assoc-in [:y-axis :dataKey] :y)
    (assoc-in [:fill :color] "#8884d8")))


(defn config-panel
  "the panel of configuration controls

  ---

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
              [rc/line :size "2px"]
              [utils/boolean-config widget-id ":brush" [:brush]]
              [rc/line :size "2px"]
              [utils/color-config-text widget-id ":fill" [:fill :color] :above-right]]])


(defn component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - widget-id : (string) unique identifier for this specific widget instance
  "
  [data widget-id]
  (let [scatter-dot-fill (ui-utils/subscribe-local widget-id [:fill :color])
        x-axis? (ui-utils/subscribe-local widget-id [:x-axis :include])
        x-axis-dataKey (ui-utils/subscribe-local widget-id [:x-axis :dataKey])
        y-axis? (ui-utils/subscribe-local widget-id [:y-axis :include])
        y-axis-dataKey (ui-utils/subscribe-local widget-id [:y-axis :dataKey])
        isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
        brush? (ui-utils/subscribe-local widget-id [:brush])]

    (fn []
      ;(log/info "configurable-Scatter-chart" @config @data)

      [c/chart
       [:> ScatterChart {:width 400 :height 400}

        (utils/standard-chart-components widget-id)

        (when @brush? [:> Brush])

        ;; TODO: looks like we need to add more attributes to x- & y-axis
        ;;
        ;(when @x-axis? [:> XAxis {:type "number" :dataKey @x-axis-dataKey :name "stature" :unit "cm"}])
        ;(when @y-axis? [:> YAxis {:type "number" :dataKey @y-axis-dataKey :name "weight" :unit "kg"}])

        [:> Scatter {:name              "tempScatter" :data @data
                     :isAnimationActive @isAnimationActive?
                     :fill              @scatter-dot-fill}]]])))

