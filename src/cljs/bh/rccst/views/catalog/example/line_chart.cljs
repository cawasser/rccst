(ns bh.rccst.views.catalog.example.line-chart
  (:require [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]
            ["recharts" :refer [ResponsiveContainer LineChart Line]]
            [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]

            [bh.rccst.events :as events]
            [bh.rccst.views.catalog.utils :as bcu]

            [bh.rccst.views.catalog.example.chart.utils :as utils]))


; region ; configuration params

(def config (r/atom (merge utils/default-config
                      {:line-uv  {:include true :stroke "#8884d8" :fill "#8884d8"}
                       :line-pv  {:include true :stroke "#82ca9d" :fill "#82ca9d"}
                       :line-amt {:include false :stroke "#ff00ff" :fill "#ff00ff"}})))

;; endregion


;; region ; config and component panels

(defn- line-config [config label path]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config config label (conj path :include)]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[utils/color-config config ":stroke" (conj path :stroke)]
                          [utils/color-config config ":fill" (conj path :fill)]]]]])


(defn- config-panel
  "the panel of configuration controls

  ---

  - config : (atom) holds all the configuration settings made by the user
  "
  [config]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config config]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[line-config config "line (uv)" [:line-uv]]
                          [line-config config "line (pv)" [:line-pv]]
                          [line-config config "line (amt)" [:line-amt]]]]]])


(defn- component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel, see [[config]].
  "
  [data config]
  (let [line-uv? (reaction (get-in @config [:line-uv :include]))
        line-pv? (reaction (get-in @config [:line-pv :include]))
        line-amt? (reaction (get-in @config [:line-amt :include]))
        isAnimationActive? (reaction (:isAnimationActive @config))]

    (fn []
      (log/info "configurable-chart" @config)

      [:> LineChart {:width 400 :height 400 :data @data}

       (utils/standard-chart-components config)

       (when @line-uv? [:> Line {:type              "monotone" :dataKey :uv
                                 :isAnimationActive @isAnimationActive?
                                 :stroke            (get-in @config [:line-uv :stroke])
                                 :fill              (get-in @config [:line-uv :fill])}])

       (when @line-pv? [:> Line {:type              "monotone" :dataKey :pv
                                 :isAnimationActive @isAnimationActive?
                                 :stroke            (get-in @config [:line-pv :stroke])
                                 :fill              (get-in @config [:line-pv :fill])}])

       (when @line-amt? [:> Line {:type              "monotone" :dataKey :amt
                                  :isAnimationActive @isAnimationActive?
                                  :stroke            (get-in @config [:line-amt :stroke])
                                  :fill              (get-in @config [:line-amt :fill])}])])))

;; endregion


(defn simple-example []
  (utils/init-config-panel "line-chart-demo")

  (let [data (r/atom utils/tabular-data)]
    (bcu/configurable-demo
      "Line Chart"
      "A simple Line Chart built using [Recharts]()"
      [:line-chart-demo/config :line-chart-demo/data :line-chart-demo/tab-panel :line-chart-demo/selected-tab]
      [utils/data-panel data]
      [config-panel config]
      [component data config]
      '[:> LineChart {:width 400 :height 400 :data @data}
        [:> CartesianGrid {:strokeDasharray (strokeDasharray config)}]
        [:> XAxis {:dataKey :name :orientation :bottom :scale "auto"}]
        [:> YAxis {:orientation :left :scale "auto"}]
        [:> Tooltip]
        [:> Legend]
        [:> Line {:type              "monotone" :dataKey :uv
                  :isAnimationActive true
                  :stroke            "#8884d8" :fill "#8884d8"}]
        [:> Line {:type              "monotone" :dataKey :pv
                  :isAnimationActive true
                  :stroke            "#82ca9d" :fill "#82ca9d"}]
        [:> Line {:type              "monotone" :dataKey :amt
                  :isAnimationActive true
                  :stroke            "#ff00ff"
                  :fill              "#ff00ff"}]])))



(defn stacked-example []
  (utils/init-config-panel "line-chart-demo")

  (let [data (r/atom utils/tabular-data)]
    (bcu/configurable-demo
      "Line Chart"
      "A simple Line Chart built using [Recharts]()"
      [:line-chart-demo/config :line-chart-demo/data :line-chart-demo/tab-panel :line-chart-demo/selected-tab]
      [utils/data-panel data]
      [config-panel config]
      [component data config]
      '[:> LineChart {:width 400 :height 400 :data @data}
        [:> CartesianGrid {:strokeDasharray (strokeDasharray config)}]
        [:> XAxis {:dataKey :name :orientation :bottom :scale "auto"}]
        [:> YAxis {:orientation :left :scale "auto"}]
        [:> Tooltip]
        [:> Legend]
        [:> Line {:type              "monotone" :dataKey :uv
                  :isAnimationActive true
                  :stroke            "#8884d8" :fill "#8884d8"}]
        [:> Line {:type              "monotone" :dataKey :pv
                  :isAnimationActive true
                  :stroke            "#82ca9d" :fill "#82ca9d"}]
        [:> Line {:type              "monotone" :dataKey :amt
                  :isAnimationActive true
                  :stroke            "#ff00ff"
                  :fill              "#ff00ff"}]])))

