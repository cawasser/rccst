(ns bh.rccst.views.catalog.example.scatter-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.views.catalog.example.chart.utils :as utils]

            ["recharts" :refer [ScatterChart Scatter XAxis YAxis CartesianGrid Tooltip]]))

; region ; configuration params

(def config (r/atom (merge utils/default-config)))

;; endregion

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
        :children [[utils/standard-chart-config config]]])


(defn- component
       "the chart to draw, taking cues from the settings of the configuration panel

       ---

       - data : (atom) any data shown by the component's ui
       - config : (atom) configuration settings made by the user using the config-panel, see [[config]].
       "
       [data config]
       (let [tooltip? (reaction (:tooltip @config))
             isAnimationActive? (reaction (:isAnimationActive @config))]

            (fn []
                (log/info "configurable-Scatter-chart" @config @data)

                [:> ScatterChart {:width 400 :height 400}
                 [:> CartesianGrid {:strokeDasharray (utils/strokeDasharray config)
                                    :stroke          (get-in @config [:grid :stroke])}]
                 [:> XAxis {:type "number" :dataKey :x :name "stature" :unit "cm"}]
                 [:> YAxis {:type "number" :dataKey :y :name "weight" :unit "kg"}]
                 (when @tooltip? [:> Tooltip])
                 [:> Scatter {:name "tempScatter" :data @data :fill "#8884d8"}]])))

;; endregion


(defn example []
      (utils/init-config-panel "scatter-chart-demo")
      (let [data (r/atom utils/triplet-data)]
           (bcu/configurable-demo
             "Scatter Chart"
             "Basic scatter chart"
             [:scatter-chart-demo/config :scatter-chart-demo/data :scatter-chart-demo/tab-panel :scatter-chart-demo/selected-tab]
             [utils/data-panel data]
             [config-panel config]
             [component data config]
             '[:> ScatterChart {:width 400 :height 400 }
               [:> CartesianGrid {:strokeDashArray (strokeDashArray config)}]
               [:> XAxis {:type :dataKey :name :unit}]
               [:> YAxis {:type :dataKey :name :unit}]
               [:> Tooltip]
               [:> Scatter {:name "TempScatter" :data @data :fill "#8884d8"}]])))