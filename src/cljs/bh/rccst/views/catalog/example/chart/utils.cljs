(ns bh.rccst.views.catalog.example.chart.utils
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
            [bh.rccst.ui-component.table :as table]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; LOW-LEVEL CONFIGURATION 'ATOMS' & 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(def btns-style {:font-size   "12px"
                 :line-height "20px"
                 :padding     "6px 8px"})
(def x-axis-btns [{:id :bottom :label ":bottom"}
                  {:id :top :label ":top"}])
(def y-axis-btns [{:id :left :label ":left"}
                  {:id :right :label ":right"}])
(def default-config {:isAnimationActive true
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
                                         :verticalAlign "bottom"}})


(defn data-panel
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


(defn boolean-config
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


(defn strokeDasharray
  "reconstitutes the 2-part string value required by `:strokeDasharray` from the
  2 values in the [[config]] atom.

  ---

  - config : (atom) holds a hash-map of the actual configuration properties, see [[config]].
  "
  [config]
  (str (get-in @config [:grid :strokeDasharray :dash])
    " "
    (get-in @config [:grid :strokeDasharray :space])))


(defn dashArray-config
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


(defn orientation-config
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


(defn scale-config
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


(defn layout-config
  "lets the user change the layout of a 'legend'.
  Supports:

    `horizontal`  &  `linear`

  ---

  - config : (atom) holds a hash-map of the actual configuration properties, see [[config]].
  - path : (vector) path into `config` where the scale for the layout is stored
  "
  [config path]
  (let [btns [{:id "horizontal" :label "horizontal"}
              {:id "vertical" :label "vertical"}]]
    [rc/h-box :src (rc/at)
     :children [[rc/box :src (rc/at) :align :start :child [:code ":layout"]]
                [rc/horizontal-bar-tabs
                 :src (rc/at)
                 :model (get-in @config path)
                 :tabs btns
                 :style btns-style
                 :on-change #(swap! config assoc-in path %)]]]))


(defn align-config
  "lets the user change the alignment of a 'legend'.
  Supports:

    `left` , `center` , `right`

  ---

  - config : (atom) holds a hash-map of the actual configuration properties, see [[config]].
  - path : (vector) path into `config` where the scale for the layout is stored
  "
  [config path]
  (let [btns [{:id "left" :label "left"}
              {:id "center" :label "center"}
              {:id "right" :label "right"}]]
    [rc/h-box :src (rc/at)
     :children [[rc/box :src (rc/at) :align :start :child [:code ":align"]]
                [rc/horizontal-bar-tabs
                 :src (rc/at)
                 :model (get-in @config path)
                 :tabs btns
                 :style btns-style
                 :on-change #(swap! config assoc-in path %)]]]))


(defn verticalAlign-config
  "lets the user change the vetical alignment of a 'legend'.
  Supports:

    `top` , `middle` , `bottom`

  ---

  - config : (atom) holds a hash-map of the actual configuration properties, see [[config]].
  - path : (vector) path into `config` where the scale for the layout is stored
  "
  [config path]
  (let [btns [{:id "top" :label "top"}
              {:id "middle" :label "middle"}
              {:id "bottom" :label "bottom"}]]
    [rc/h-box :src (rc/at)
     :children [[rc/box :src (rc/at) :align :start :child [:code ":verticalAlign"]]
                [rc/horizontal-bar-tabs
                 :src (rc/at)
                 :model (get-in @config path)
                 :tabs btns
                 :style btns-style
                 :on-change #(swap! config assoc-in path %)]]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; HIGH-LEVEL CONFIGURATION 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn isAnimationActive [config]
  [boolean-config config ":isAnimationActive" [:isAnimationActive]])


(defn grid [config]
  [rc/v-box :src (rc/at)
   :children [[boolean-config config ":grid" [:grid :include]]
              [dashArray-config config
               ":strokeDasharray" 1 10 [:grid :strokeDasharray]]]])


(defn x-axis [config]
  [rc/v-box :src (rc/at)
   :children [[boolean-config config ":x-axis" [:x-axis :include]]
              [orientation-config config x-axis-btns ":orientation" [:x-axis :orientation]]
              [scale-config config ":scale" [:x-axis :scale]]]])


(defn y-axis [config]
  [rc/v-box :src (rc/at)
   :children [[boolean-config config ":y-axis" [:y-axis :include]]
              [orientation-config config y-axis-btns ":orientation" [:y-axis :orientation]]
              [scale-config config ":scale" [:y-axis :scale]]]])


(defn tooltip [config]
  [boolean-config config ":tooltip" [:tooltip :include]])


(defn legend [config]
  [rc/v-box :src (rc/at)
   :children [[boolean-config config ":legend" [:legend :include]]
              [layout-config config [:legend :layout]]
              [align-config config [:legend :align]]
              [verticalAlign-config config [:legend :verticalAlign]]]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; VERY!!! HIGH-LEVEL CONFIGURATION 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn standard-chart-config [config]
  [:<>
   [isAnimationActive config]
   [rc/line :src (rc/at) :size "2px"]
   [grid config]
   [rc/line :src (rc/at) :size "2px"]
   [x-axis config]
   [rc/line :src (rc/at) :size "2px"]
   [y-axis config]
   [rc/line :src (rc/at) :size "2px"]
   [tooltip config]
   [rc/line :src (rc/at) :size "2px"]
   [legend config]])

