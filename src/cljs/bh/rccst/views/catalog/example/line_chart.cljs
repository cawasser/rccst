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
            [bh.rccst.ui-component.navbar :as navbar]
            [bh.rccst.ui-component.table :as table]))


(def data (r/atom [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                   {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                   {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                   {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                   {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                   {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                   {:name "Page G" :uv 3490 :pv 4300 :amt 2100}]))


(defn- data-panel [data]
  [table/table
   :data data
   :width "100%"
   :max-rows 5])


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
                     :legend            {:include true}
                     :line-uv           {:include true}
                     :line-pv           {:include true}
                     :line-amt          {:include false}}))
(def btns-style {:font-size   "12px"
                 :line-height "20px"
                 :padding     "6px 8px"})
(def x-axis-btns [{:id :bottom   :label ":bottom"}
                  {:id :top     :label ":top"}])
(def y-axis-btns [{:id :left   :label ":left"}
                  {:id :right     :label ":right"}])


(defn- boolean-config [config label path]
  [rc/checkbox :src (rc/at)
   :label [rc/box :src (rc/at) :align :start :child [:code label]]
   :model (get-in @config path)
   :on-change #(swap! config assoc-in path %)])


(defn- dashArray-config [config label min max path]
  [rc/h-box :src (rc/at)
   :children [[rc/box :src (rc/at) :align :start :child [:code label]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/slider :src (rc/at)
                           :model (get-in @config (conj path :dash))
                           :width "100px"
                           :min min :max max
                           :on-change #(swap! config assoc-in (conj path :dash) %)]
                          [rc/slider :src (rc/at)
                           :model (get-in @config (conj path :space))
                           :width "100px"
                           :min min :max max
                           :on-change #(swap! config assoc-in (conj path :space) %)]]]]])


(defn- orientation-config [config btns label path]
  [rc/h-box :src (rc/at)
   :children [[rc/box :src (rc/at) :align :start :child [:code label]]
              [rc/horizontal-bar-tabs
               :src (rc/at)
               :model (get-in @config path)
               :tabs btns
               :style btns-style
               :on-change #(swap! config assoc-in path %)]]])


(defn- scale-config [config label path]
  (let [btns [{:id "auto" :label "auto"}
              {:id "linear" :label "linear"}
              {:id "pow" :label "pow"}
              {:id "sqrt" :label "sqrt"}
              {:id "log" :label "log"}]]
    [rc/h-box :src (rc/at)
     :children [[rc/box :src (rc/at) :align :start :child [:code label]]
                [rc/horizontal-bar-tabs
                 :src (rc/at)
                 :model (get-in @config path)
                 :tabs btns
                 :style btns-style
                 :on-change #(swap! config assoc-in path %)]]]))


(defn- config-panel
  "the panel of configuration controls

  ---

  - config : (atom) holds all the configuration settings made by the user
  "
  [config]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :style {:min-width        "150px"
           :padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[rc/v-box
               :children [[boolean-config config ":grid" [:grid :include]]
                          [dashArray-config config ":strokeDasharray" 1 10 [:grid :strokeDasharray]]]]

              [rc/v-box
               :children [[boolean-config config ":x-axis" [:x-axis :include]]
                          [orientation-config config x-axis-btns ":orientation" [:x-axis :orientation]]
                          [scale-config config ":scale" [:x-axis :scale]]]]

              [rc/v-box
               :children [[boolean-config config ":y-axis" [:y-axis :include]]
                          [orientation-config config y-axis-btns ":orientation" [:y-axis :orientation]]
                          [scale-config config ":scale" [:y-axis :scale]]]]

              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[boolean-config config ":tooltip" [:tooltip :include]]
                          [boolean-config config ":legend" [:legend :include]]]]

              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[boolean-config config "line (uv)" [:line-uv :include]]
                          [boolean-config config "line (pv)" [:line-pv :include]]
                          [boolean-config config "line (amt)" [:line-amt :include]]]]]])


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


(defn- controls [data config]
  (let [data-or-config [[:line-chart/config "config"]
                        [:line-chart/data "data"]]]
    [:div
     [navbar/navbar data-or-config [:line-chart/tab-panel]]

     [tab-panel/tab-panel {:extra-classes             :rccst
                           :subscribe-to-selected-tab [:line-chart/selected-tab]}

      [tab-panel/sub-panel {:panel-id :line-chart/config}
       [config-panel config]]

      [tab-panel/sub-panel {:panel-id :line-chart/data}
       [data-panel data]]]]))


(defn- strokeDasharray [config]
  (str (get-in @config [:grid :strokeDasharray :dash])
    " "
    (get-in @config [:grid :strokeDasharray :space])))


(defn- configurable-chart
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
        line-uv? (reaction (get-in @config [:line-uv :include]))
        line-pv? (reaction (get-in @config [:line-pv :include]))
        line-amt? (reaction (get-in @config [:line-amt :include]))
        isAnimationActive? (reaction (:isAnimationActive @config))]

    (fn []
      (log/info "configurable-chart" @config)

      [:> LineChart {:width 400 :height 400 :data @data}

       (when @grid? [:> CartesianGrid {:strokeDasharray (strokeDasharray config)}])

       (when @x-axis? [:> XAxis {:dataKey     :name
                                 :orientation (get-in @config [:x-axis :orientation])
                                 :scale       (get-in @config [:x-axis :scale])}])

       (when @y-axis? [:> YAxis {:orientation (get-in @config [:y-axis :orientation])
                                 :scale       (get-in @config [:y-axis :scale])}])

       (when @tooltip? [:> Tooltip])

       (when @legend? [:> Legend])

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


(defn example []
  (re-frame/dispatch-sync [::events/init-locals :line-chart init-db])

  (bcu/configurable-demo "Line Chart"
    "Line Charts (working on adding a configuration tool for this)"
    [controls data config]
    [configurable-chart data config]
    '[layout/centered {:extra-classes :width-50}]))

