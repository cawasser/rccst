(ns bh.rccst.views.catalog.example.treemap-chart
  (:require [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]
            ["recharts" :refer [Treemap]]
            [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]

            [bh.rccst.events :as events]
            [bh.rccst.views.catalog.utils :as bcu]

            [bh.rccst.views.catalog.example.chart.utils :as utils]))


; region ; configuration params

(def default-ratio (/ 4 3))
(def default-stroke "#ffffff")
(def default-fill "#8884d8")

(def config (r/atom {:isAnimationActive true
                     :ratio  {:include true
                              :n 4
                              :d 3}
                     :stroke  {:color "#ffffff"}
                     :fill {:color "#8884d8"}}))

;; endregion


;; region ; config and component panels


(defn- ratio-config [config]
  [rc/h-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config config ":ratio" [:ratio :include]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[utils/slider-config config 1 10 [:ratio :n]]
                          [utils/slider-config config 1 10 [:ratio :d]]]]]])


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
   :children [[utils/isAnimationActive config]
              [rc/line :src (rc/at) :size "2px"]
              [rc/v-box :src (rc/at)
               :gap "10px"
               :children [[ratio-config config]
                          [utils/color-config config :stroke [:stroke :color]]
                          [utils/color-config config :fill [:fill :color]]]]]])


(defn- component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel, see [[config]].
  "
  [data config]
  (let [ratio (reaction (get-in @config [:ratio :include]))
        isAnimationActive? (reaction (:isAnimationActive @config))]
        ;ratio-val (reaction (/ (get-in @config [:ratio :n]) (get-in @config [:ratio :d])))]

    (fn []
      [:> Treemap
       {:width 400 :height 400
        :data @data
        :dataKey "size"
        :isAnimationActive @isAnimationActive?
        :ratio default-ratio
        :stroke (get-in @config [:stroke :color])
        :fill (get-in @config [:fill :color])}])))


;; endregion


(defn example []
  (utils/init-config-panel "treemap-chart-demo")

  (let [data (r/atom utils/hierarchy-data)]
    (bcu/configurable-demo
      "Treemap Chart"
      "A simple Treemap Chart built using [Recharts](https://recharts.org/en-US/api/Treemap)"
      [:treemap-chart-demo/config :treemap-chart-demo/data :treemap-chart-demo/tab-panel :treemap-chart-demo/value]
      [:div "Dummy Data here"]                              ;[utils/tabular-data-panel data]
      [config-panel config]
      [component data config]
      '[:> Treemap
        {:width  400 :height 400
         :data   @data
         :datakey :size
         :isAnimationActive true
         :ratio  (/ 3 4)
         :stroke "#ffffff"
         :fill   "#8884d8"}])))

