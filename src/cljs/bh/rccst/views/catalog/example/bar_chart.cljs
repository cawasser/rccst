(ns bh.rccst.views.catalog.example.bar-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]

            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.views.catalog.example.chart.utils :as utils]
            ["recharts" :refer [BarChart Bar]]))


;; region ; configuration params

(def config (r/atom (merge utils/default-config
                      {:bar-uv  {:include true}
                       :bar-pv  {:include true}
                       :bar-amt {:include false}
                       :bar-d   {:include false}})))

;; endregion


;; region ; config and component panels

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
               :children [[utils/boolean-config config "bar (uv)" [:bar-uv :include]]
                          [utils/boolean-config config "bar (pv)" [:bar-pv :include]]
                          [utils/boolean-config config "bar (amt)" [:bar-amt :include]]
                          [utils/boolean-config config "bar (d)" [:bar-d :include]]]]]])


(defn- component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel
  "
  [data config]
  (let [bar-uv? (reaction (get-in @config [:bar-uv :include]))
        bar-pv? (reaction (get-in @config [:bar-pv :include]))
        bar-amt? (reaction (get-in @config [:bar-amt :include]))
        bar-d? (reaction (get-in @config [:bar-d :include]))
        isAnimationActive? (reaction (:isAnimationActive @config))]

    (fn []
      [:> BarChart {:width 400 :height 400 :data @data}

       (utils/standard-chart-components config)

       (when @bar-uv? [:> Bar {:type              "monotone" :dataKey :uv
                               :isAnimationActive @isAnimationActive?
                               :fill              "#8884d8"}])

       (when @bar-pv? [:> Bar {:type              "monotone" :dataKey :pv
                               :isAnimationActive @isAnimationActive?
                               :fill              "#82ca9d"}])

       (when @bar-amt? [:> Bar {:type              "monotone" :dataKey :amt
                                :isAnimationActive @isAnimationActive?
                                :fill              "#ff00ff"}])

       (when @bar-d? [:> Bar {:type              "monotone" :dataKey :d
                              :isAnimationActive @isAnimationActive?
                              :fill              "#DC143C"}])])))

;; endregion


(defn example []
  (utils/init-config-panel "bar-chart")

  (let [data (r/atom (mapv (fn [d] (assoc d :d (rand-int 5000))) utils/tabular-data))]
    (bcu/configurable-demo "Bar Chart"
      "Bar Charts (this would be really cool with support for changing options live)"
      [:bar-chart/config :bar-chart/data :bar-chart/tab-panel :bar-chart/selected-tab]
      [utils/data-panel data]
      [config-panel config]
      [component data config]
      '[:> BarChart {:width 400 :height 400 :data @data}
        [:> CartesianGrid {:strokeDasharray "3 3"}]
        [:> XAxis {:dataKey "title"}]
        [:> YAxis]
        [:> Tooltip]
        [:> Legend {:layout "horizontal"}]
        [:> Bar {:type "monotone" :dataKey "uv" :fill "#8884d8"}]
        [:> Bar {:type "monotone" :dataKey "pv" :fill "#82ca9d"}]])))

