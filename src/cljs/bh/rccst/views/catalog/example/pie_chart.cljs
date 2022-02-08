(ns bh.rccst.views.catalog.example.pie-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]

            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.views.catalog.example.chart.utils :as utils]

            ["recharts" :refer [PieChart Pie]]))



(def config (r/atom (merge utils/default-config
                      {:fill "#8884d8"})))


(defn- config-panel
  "the panel of configuration controls

  ---

  - config : (atom) holds all the configuration settings made by the user
  "
  [config]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "500px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config config]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[utils/color-config-text config ":fill" [:fill]]]]]])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel
  "
  [data config]
  (let [isAnimationActive? (reaction (:isAnimationActive @config))]

    (fn []
      [:> PieChart {:width 400 :height 400
                    :label true}
       (utils/non-gridded-chart-components config)

       [:> Pie {:dataKey "value" :name-Key "name"
                :data @data
                :fill (:fill @config)
                :label true
                :isAnimationActive @isAnimationActive?}]])))


(defn example []
  (utils/init-config-panel "pie-chart-demo")

  (let [data (r/atom utils/paired-data)]

    (bcu/configurable-demo "Pie Chart"
      "Pie Chart with a default fill for each slice. Each slice is sized correctly and labeled with the value, but
      they are all the same color.

> See `Colored Pie Chart` for an example of how to get the slices to be different colors."
      [:pie-chart-demo/config :pie-chart-demo/data :pie-chart-demo/tab-panel :pie-chart-demo/value]
      [utils/tabular-data-panel data]
      [config-panel config]
      [component-panel data config]
      '[:> PieChart {:width 400 :height 400}
        [:> Tooltip]
        [:> Legend]
        [:> Pie {:dataKey "value" :data @data :fill "#8884d8"}]])))
