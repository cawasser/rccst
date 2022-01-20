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


(def data (r/atom [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                   {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                   {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                   {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                   {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                   {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                   {:name "Page G" :uv 3490 :pv 4300 :amt 2100}]))


(defn- data-panel
  "provides a simple tabular component (via `bh.rccst.ui-component.table`) to show the data presented
  in the Chart.

> Note: `table` uses the keys of the first hash-map in `@data` as the header label for the columns

  ---

  - data : (atom) vector of content hash-maps."

  [data]
  [table/table
   :width 500
   :data data
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
                     :legend            {:include       true
                                         :layout        "horizontal"
                                         :align         "center"
                                         :verticalAlign "middle"}
                     :line-uv           {:include true}
                     :line-pv           {:include true}
                     :line-amt          {:include false}}))
(def btns-style {:font-size   "12px"
                 :line-height "20px"
                 :padding     "6px 8px"})
(def x-axis-btns [{:id :bottom :label ":bottom"}
                  {:id :top :label ":top"}])
(def y-axis-btns [{:id :left :label ":left"}
                  {:id :right :label ":right"}])


(defn- boolean-config
  "lets the user turn on/of some component of the Chart

  ---

  - config : (atom) holds a hash-map of the actual configuration properties, see [[config]].
  - label : (string) tell the user which subcomponent this control is manipulating
  - path : (vector) path into `config` where the subcomponent 'inclusion' value is stored
  "
  [config label path]
  [rc/checkbox :src (rc/at)
   :label [rc/box :src (rc/at) :align :start :child [:code label]]
   :model (get-in @config path)
   :on-change #(swap! config assoc-in path %)])


(defn- strokeDasharray
  "reconstitutes the 2-part string value required by `:strokeDasharray` from the
  2 values in the [[config]] atom.

  ---

  - config : (atom) holds a hash-map of the actual configuration properties, see [[config]].
  "
  [config]
  (str (get-in @config [:grid :strokeDasharray :dash])
    " "
    (get-in @config [:grid :strokeDasharray :space])))


(defn- dashArray-config
  "provides the user with 2 sliders to control the 2 parts of the `:strokeDasharray`
  property of a chart's [`CartesianGrid`](https://recharts.org/en-US/api/CartesianGrid)

  ---

  - config : (atom) holds a hash-map of the actual configuration properties, see [[config]].
  - label : (string) tell the user which axis this control is manipulating
  - min : (integer) minimum value for the slider
  - max : (integer) maximum value for the slider
  - path : (vector) path into `config` where the :strokeDasharray is stored
  "

  [config label min max path]
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


(defn- orientation-config
  "lets the user configure the orientation of an axis. Which axis is defined by the arguments.

  ---

  - config : (atom) holds a hash-map of the actual configuration properties, see [[config]].
  - btns : (vector) define the button that set the value(s).

  | key       | description                                                          |
  |:----------|:---------------------------------------------------------------------|
  | `:id`     | the value to set when the use click the corresponding button control |
  | `:label`  | the label to put on the button                                       |

  Each axis support a different set of possible orientations:

  | axis      | allowed orientations   |
  |:----------|:-----------------------|
  | X Axis    | `:top` , `:bottom`     |
  | Y Axis    | `:left` , `:right`     |

  - label : (string) tell the user which axis this control is manipulating
  - path : (vector) path into `config` where the orientation for the correct axis is stored
  "
  [config btns label path]
  [rc/h-box :src (rc/at)
   :children [[rc/box :src (rc/at) :align :start :child [:code label]]
              [rc/horizontal-bar-tabs
               :src (rc/at)
               :model (get-in @config path)
               :tabs btns
               :style btns-style
               :on-change #(swap! config assoc-in path %)]]])


(defn- scale-config
  "lets the user change the scale of an 'axis'. Which axis is defined by the arguments.
  Supports only:

    `auto` , `linear` , `pow` , `sqrt` , `log`

  scale types. Recharts supports many more. See [here](https://recharts.org/en-US/api/XAxis#scale)

  ---

  - config : (atom) holds a hash-map of the actual configuration properties, see [[config]].
  - label : (string) tell the user which axis this control is manipulating
  - path : (vector) path into `config` where the scale for the correct axis is stored
  "
  [config label path]
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
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config config]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[boolean-config config "line (uv)" [:line-uv :include]]
                          [boolean-config config "line (pv)" [:line-pv :include]]
                          [boolean-config config "line (amt)" [:line-amt :include]]]]]])


(defn- component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel, see [[config]].
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

       (when @legend? [:> Legend {:layout        (get-in @config [:legend :layout])
                                  :align         (get-in @config [:legend :align])
                                  :verticalAlign (get-in @config [:legend :verticalAlign])}])

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

  (bcu/configurable-demo
    "Line Chart"
    "A simple Line Chart built using [Recharts]()"
    [:line-chart/config :line-chart/data :line-chart/tab-panel :line-chart/selected-tab]
    [data-panel data]
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

