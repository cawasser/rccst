(ns bh.rccst.ui-component.atom.scatter-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]

            ["recharts" :refer [ScatterChart Scatter Brush]]

            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(def sample-data
  "the Scatter Chart works best with \"triplet data\" so we return the triplet-data from utils"
  (r/atom utils/triplet-data))


(defn config
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a line-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. line charts
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
    (merge {:tab-panel {:value     (keyword chart-id "config")
                        :data-path [:widgets (keyword chart-id) :tab-panel]}
            :brush     false})
    (assoc-in [:x-axis :dataKey] :x)
    (assoc-in [:y-axis :dataKey] :y)
    (assoc-in [:fill :color] "#8884d8")))


(defn config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.
  - config : (atom) holds all the configuration settings made by the user
  "
  [data chart-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data chart-id]
              [rc/line :size "2px"]
              [utils/boolean-config chart-id ":brush" [:brush]]
              [rc/line :size "2px"]
              [utils/color-config-text chart-id ":fill" [:fill :color] :above-right]]])


(defn component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - widget-id : (string) unique identifier for this specific widget instance
  "
  [data chart-id]
  (let [scatter-dot-fill (ui-utils/subscribe-local chart-id [:fill :color])
        x-axis? (ui-utils/subscribe-local chart-id [:x-axis :include])
        x-axis-dataKey (ui-utils/subscribe-local chart-id [:x-axis :dataKey])
        y-axis? (ui-utils/subscribe-local chart-id [:y-axis :include])
        y-axis-dataKey (ui-utils/subscribe-local chart-id [:y-axis :dataKey])
        isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])
        brush? (ui-utils/subscribe-local chart-id [:brush])]

    (fn []
      ;(log/info "configurable-Scatter-chart" @config @data)

      [c/chart
       [:> ScatterChart {:width 400 :height 400}

        (utils/standard-chart-components chart-id)

        (when @brush? [:> Brush])

        ;; TODO: looks like we need to add more attributes to x- & y-axis
        ;;
        ;(when @x-axis? [:> XAxis {:type "number" :dataKey @x-axis-dataKey :name "stature" :unit "cm"}])
        ;(when @y-axis? [:> YAxis {:type "number" :dataKey @y-axis-dataKey :name "weight" :unit "kg"}])

        [:> Scatter {:name              "tempScatter" :data @data
                     :isAnimationActive @isAnimationActive?
                     :fill              @scatter-dot-fill}]]])))

