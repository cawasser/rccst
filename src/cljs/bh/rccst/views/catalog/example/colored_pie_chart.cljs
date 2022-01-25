(ns bh.rccst.views.catalog.example.colored-pie-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]

            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.views.catalog.example.chart.utils :as utils]

            ["recharts" :refer [PieChart Pie Cell]]))


(def config (r/atom (merge utils/default-config
                      {:colors (zipmap (map :name utils/paired-data)
                                 ["#ffff00" "#ff0000" "#00ff00"
                                  "#0000ff" "#009999" "#ff00ff"])})))


(defn- color-anchors [config]
  [:<>
   (doall
     (map (fn [[id _]]
            ^{:key id}[utils/color-config-text config id [:colors id] :right-above])
       (:colors @config)))])


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
   :children [[utils/non-gridded-chart-config config]
              [rc/line :src (rc/at) :size "2px"]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/label :src (rc/at) :label "Pie Colors"]
                          (color-anchors config)]]]])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel
  "
  [data config]
  (let [isAnimationActive? (reaction (:isAnimationActive @config))]

    (fn []
      [:> PieChart {:width 400 :height 400 :label true}
       (utils/non-gridded-chart-components config)
       [:> Pie {:data @data
                :dataKey "value" :nameKey "name"
                :label true
                :isAnimationActive @isAnimationActive?}
        (doall
          (map-indexed
            (fn [idx {name :name}]
              ^{:key (str idx name)}
              [:> Cell {:key (str "cell-" idx)
                        :fill (get-in @config [:colors name])}])
            @data))]])))


(defn example []
  (utils/init-config-panel "colored-pie-chart-demo")

  (let [data (r/atom utils/paired-data)]
    (bcu/configurable-demo "Colored Pie Chart"
      "Pie Chart with different colors for each slice. This requires embedding `Cell` elements
inside the `Pie` element.

> Note: Recharts supports embedding `Cell` in a variety of other chart types, for example BarChart"

      [:pie-chart-demo/config :pie-chart-demo/data :pie-chart-demo/tab-panel :pie-chart-demo/selected-tab]
      [utils/tabular-data-panel data]
      [config-panel config]
      [component-panel data config]

      '[:> PieChart {:width 400 :height 400 :label true}
        [:> Tooltip]
        [:> Legend]
        [:> Pie {:data @data :label true}
         (map-indexed (fn [idx _]
                        [:> Cell {:key (str "cell-" idx) :fill (get @colors idx)}])
           @data)]])))

