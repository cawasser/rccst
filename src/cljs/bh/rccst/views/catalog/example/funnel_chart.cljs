(ns bh.rccst.views.catalog.example.funnel-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.views.catalog.example.chart.utils :as utils]

            ["recharts" :refer [FunnelChart Funnel LabelList XAxis YAxis CartesianGrid Tooltip]]))

; region ; configuration params

(def config (r/atom (merge utils/default-config)))

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
                (log/info "configurable-funnel-chart" @config)

                [:> FunnelChart {:height 400 :width 400}
                 [:> Tooltip]  ;(when @tooltip? [:> Tooltip])
                 [:> Funnel :dataKey :value :data @data :isAnimationActive isAnimationActive?
                  [:> LabelList :position :right :fill "#000" :stroke "none" :dataKey :name]]])))

;; endregion

(defn example []
      (utils/init-config-panel "funnel-chart-demo")

      (let [data (r/atom utils/paired-filled-data)]
           (bcu/configurable-demo
             "Funnel Chart"
             "Basic Funnel Chart"
             [:funnel-chart-demo/config :funnel-chart-demo/data :funnel-chart-demo/tab-panel :funnel-chart-demo/selected-tab]
             [utils/data-panel data]
             [config-panel config]
             [component data config]
             '[:> Funnel {:stuff :things}])))