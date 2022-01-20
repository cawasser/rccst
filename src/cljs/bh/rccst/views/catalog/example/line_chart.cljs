(ns bh.rccst.views.catalog.example.line-chart
  (:require [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]
            ["recharts" :refer [LineChart Line
                                XAxis YAxis CartesianGrid
                                Tooltip Legend]]
            [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]

            [bh.rccst.events :as events]
            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.ui-component.table :as table]

            [bh.rccst.views.catalog.example.chart.utils :as utils]))

;; region ; support for the tabs/panels

(def data-path [:line-chart :tab-panel])
(def init-db
  {:tab-panel (tab-panel/mk-tab-panel-data data-path :line-chart/config)})

(re-frame/reg-sub
  :db/line-chart
  (fn [db _]
    (:line-chart db)))

(re-frame/reg-sub
  :line-chart/tab-panel
  :<- [:db/line-chart]
  (fn [navbar]
    (:tab-panel navbar)))

(re-frame/reg-sub
  :line-chart/selected-tab
  :<- [:line-chart/tab-panel]
  (fn [tab-panel]
    (:value tab-panel)))

;; endregion


; region ; data and configuration params

(def data (r/atom [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                   {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                   {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                   {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                   {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                   {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                   {:name "Page G" :uv 3490 :pv 4300 :amt 2100}]))


(def config (r/atom (merge utils/default-config
                     {:line-uv           {:include true}
                      :line-pv           {:include true}
                      :line-amt          {:include false}})))

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
               :children [[utils/boolean-config config "line (uv)" [:line-uv :include]]
                          [utils/boolean-config config "line (pv)" [:line-pv :include]]
                          [utils/boolean-config config "line (amt)" [:line-amt :include]]]]]])


(defn- component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel, see [[config]].
  "
  [data config]
  (let [;grid? (reaction (get-in @config [:grid :include]))
        ;x-axis? (reaction (get-in @config [:x-axis :include]))
        ;y-axis? (reaction (get-in @config [:y-axis :include]))
        ;tooltip? (reaction (get-in @config [:tooltip :include]))
        ;legend? (reaction (get-in @config [:legend :include]))
        line-uv? (reaction (get-in @config [:line-uv :include]))
        line-pv? (reaction (get-in @config [:line-pv :include]))
        line-amt? (reaction (get-in @config [:line-amt :include]))
        isAnimationActive? (reaction (:isAnimationActive @config))]

    (fn []
      (log/info "configurable-chart" @config)

      [:> LineChart {:width 400 :height 400 :data @data}

       (utils/standard-chart-components config)

       (when @line-uv? [:> Line {:type              "monotone" :dataKey :uv
                                 :isAnimationActive @isAnimationActive?
                                 :stroke            "#8884d8" :fill "#8884d8"}])

       (when @line-pv? [:> Line {:type              "monotone" :dataKey :pv
                                 :isAnimationActive @isAnimationActive?
                                 :stroke            "#82ca9d" :fill "#82ca9d"}])

       (when @line-amt? [:> Line {:type              "monotone" :dataKey :amt
                                  :isAnimationActive @isAnimationActive?
                                  :stroke            "#ff00ff"
                                  :fill              "#ff00ff"}])])))

;; endregion


(defn example []
  (re-frame/dispatch-sync [::events/init-locals :line-chart init-db])

  (bcu/configurable-demo
    "Line Chart"
    "A simple Line Chart built using [Recharts]()"
    [:line-chart/config :line-chart/data :line-chart/tab-panel :line-chart/selected-tab]
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
                :fill              "#ff00ff"}]]))

