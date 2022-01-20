(ns bh.rccst.views.catalog.example.bar-chart
  (:require [woolybear.ad.layout :as layout]
            [woolybear.packs.tab-panel :as tab-panel]
            [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-frame.core :as re-frame]
            [re-com.core :as rc]

            [bh.rccst.events :as events]
            [bh.rccst.ui-component.table :as table]
            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.views.catalog.example.chart.utils :as utils]
            ["recharts" :refer [BarChart Bar
                                XAxis YAxis CartesianGrid
                                Tooltip Legend]]))


(def data-path [:bar-chart :tab-panel])
(def init-db
  {:tab-panel (tab-panel/mk-tab-panel-data data-path :bar-chart/config)})

(re-frame/reg-sub
  :db/bar-chart
  (fn [db _]
    (:bar-chart db)))

(re-frame/reg-sub
  :bar-chart/tab-panel
  :<- [:db/bar-chart]
  (fn [navbar]
    (:tab-panel navbar)))

(re-frame/reg-sub
  :bar-chart/selected-tab
  :<- [:bar-chart/tab-panel]
  (fn [tab-panel]
    (:value tab-panel)))


(def data (r/atom [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                   {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                   {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                   {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                   {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                   {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                   {:name "Page G" :uv 3490 :pv 4300 :amt 2100}]))


(def config (r/atom {:isAnimationActive true
                     :grid              {:include         true
                                         :strokeDasharray {:dash "3" :space "3"}}
                     :x-axis            {:include     true
                                         :orientation :bottom
                                         :scale       "auto"}
                     :y-axis            {:include     true
                                         :orientation :left
                                         :scale       "auto"}
                     :tooltip           {:include true}
                     :legend            {:include       true
                                         :layout        "horizontal"
                                         :align         "center"
                                         :verticalAlign "bottom"}
                     :bar-uv            {:include true}
                     :bar-pv            {:include true}
                     :bar-amt           {:include false}}))


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
                          [utils/boolean-config config "bar (amt)" [:bar-amt :include]]]]]])


(defn- component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel
  "
  [data config]
  (let [grid? (reaction (get-in @config [:grid :include]))
        x-axis? (reaction (get-in @config [:x-axis :include]))
        y-axis? (reaction (get-in @config [:y-axis :include]))
        tooltip? (reaction (get-in @config [:tooltip :include]))
        legend? (reaction (get-in @config [:legend :include]))
        bar-uv? (reaction (get-in @config [:bar-uv :include]))
        bar-pv? (reaction (get-in @config [:bar-pv :include]))
        bar-amt? (reaction (get-in @config [:bar-amt :include]))
        isAnimationActive? (reaction (:isAnimationActive @config))]

    (fn []
      [:> BarChart {:width 400 :height 400 :data @data}

       (when @grid? [:> CartesianGrid {:strokeDasharray (utils/strokeDasharray config)}])

       (when @x-axis? [:> XAxis {:dataKey     :name
                                 :orientation (get-in @config [:x-axis :orientation])
                                 :scale       (get-in @config [:x-axis :scale])}])

       (when @y-axis? [:> YAxis {:orientation (get-in @config [:y-axis :orientation])
                                 :scale       (get-in @config [:y-axis :scale])}])

       (when @tooltip? [:> Tooltip])

       (when @legend? [:> Legend {:layout        (get-in @config [:legend :layout])
                                  :align         (get-in @config [:legend :align])
                                  :verticalAlign (get-in @config [:legend :verticalAlign])}])

       (when @bar-uv? [:> Bar {:type              "monotone" :dataKey :uv
                               :isAnimationActive @isAnimationActive?
                               :fill              "#8884d8"}])

       (when @bar-pv? [:> Bar {:type              "monotone" :dataKey :pv
                               :isAnimationActive @isAnimationActive?
                               :fill              "#82ca9d"}])

       (when @bar-amt? [:> Bar {:type              "monotone" :dataKey :amt
                                :isAnimationActive @isAnimationActive?
                                :fill              "#ff00ff"}])])))


(defn example []
  (re-frame/dispatch-sync [::events/init-locals :bar-chart init-db])

  (bcu/configurable-demo "Bar Chart"
    "Bar Charts (this would be really cool with support for changing options live)"
    [:bar-chart/config :bar-chart/data :bar-chart/tab-panel :bar-chart/selected-tab]
    [utils/data-panel data]
    [config-panel config]
    [component data config]
    '[layout/centered {:extra-classes :width-50}
      [:> BarChart {:width 400 :height 400 :data @data}
       [:> CartesianGrid {:strokeDasharray "3 3"}]
       [:> XAxis {:dataKey "title"}]
       [:> YAxis]
       [:> Tooltip]
       [:> Legend]
       [:> Bar {:type "monotone" :dataKey "uv" :fill "#8884d8"}]
       [:> Bar {:type "monotone" :dataKey "pv" :fill "#82ca9d"}]]]))

