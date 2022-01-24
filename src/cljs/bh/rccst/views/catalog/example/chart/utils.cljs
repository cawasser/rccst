(ns bh.rccst.views.catalog.example.chart.utils
  (:require [taoensso.timbre :as log]
            ["recharts" :refer [XAxis YAxis CartesianGrid
                                Tooltip Legend]]
            ["react-colorful" :refer [HexColorPicker]]

            [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-frame.core :as re-frame]
            [re-com.core :as rc]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.events :as events]
            [bh.rccst.ui-component.table :as table]))


(defn init-config-panel [base-id]
  (log/info "init-config-panel" base-id)
  (let [formal-id (keyword base-id)
        data-path [formal-id :tab-panel]
        config-id (keyword base-id "config")
        data-id (keyword base-id "data")
        db-id (keyword "db" base-id)
        tab-id (keyword base-id "tab-panel")
        selected-id (keyword base-id "selected-tab")
        init-db {:tab-panel (tab-panel/mk-tab-panel-data
                              data-path config-id)}]

    (re-frame/reg-sub
      db-id
      (fn [db _]
        (formal-id db)))

    (re-frame/reg-sub
      tab-id
      :<- [db-id]
      (fn [navbar]
        (:tab-panel navbar)))

    (re-frame/reg-sub
      selected-id
      :<- [tab-id]
      (fn [tab-panel]
        (:value tab-panel)))

    (re-frame/dispatch-sync [::events/init-locals formal-id init-db])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; SOME EXAMPLE DATA
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(def tabular-data [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                   {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                   {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                   {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                   {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                   {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                   {:name "Page G" :uv 3490 :pv 4300 :amt 2100}])

(def paired-data [{:name "Group A" :value 400}
                  {:name "Group B" :value 300}
                  {:name "Group C" :value 300}
                  {:name "Group D" :value 200}
                  {:name "Group E" :value 278}
                  {:name "Group F" :value 189}])

(def triplet-data [{:x 100 :y 200 :z 200}
                   {:x 120 :y 100 :z 260}
                   {:x 170 :y 300 :z 400}
                   {:x 140 :y 250 :z 280}
                   {:x 150 :y 400 :z 500}
                   {:x 110 :y 280 :z 200}])

;; endregion

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; LOW-LEVEL CONFIGURATION 'ATOMS' & 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(def btns-style {:font-size   "12px"
                 :line-height "20px"
                 :padding     "6px 8px"})
(def x-axis-btns [{:id :bottom :label ":bottom"}
                  {:id :top :label ":top"}])
(def y-axis-btns [{:id :left :label ":left"}
                  {:id :right :label ":right"}])
(def default-config {:isAnimationActive true
                     :grid              {:include         true
                                         :strokeDasharray {:dash "3" :space "3"}
                                         :stroke          "#a9a9a9"}
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


(defn color-config [config label path]
  (let [showing? (r/atom false)]
    (fn []
      [rc/h-box :src (rc/at)
       :gap "5px"
       :children [[rc/popover-anchor-wrapper :src (rc/at)
                   :showing? showing?
                   :position :right-center
                   :anchor [rc/button :src (rc/at)
                            :label label
                            :on-click #(swap! showing? not)]
                   :popover [rc/popover-content-wrapper :src (rc/at)
                             :close-button? true
                             :body [:> HexColorPicker {:color     (get-in @config path)
                                                       :on-change #(swap! config assoc-in path %)}]]]
                  [rc/input-text :src (rc/at)
                   :model (get-in @config path)
                   :on-change #(swap! config assoc-in path %)]]])))



;; endregion


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; HIGH-LEVEL CONFIGURATION 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(defn isAnimationActive [config]
  [boolean-config config ":isAnimationActive" [:isAnimationActive]])


(defn grid [config]
  [rc/v-box :src (rc/at)
   :children [[boolean-config config ":grid" [:grid :include]]
              [dashArray-config config
               ":strokeDasharray" 1 10 [:grid :strokeDasharray]]
              [color-config config ":stroke" [:grid :stroke]]]])


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

;; endregion


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; VERY!!! HIGH-LEVEL CONFIGURATION 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

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


(defn non-gridded-chart-config [config]
  [:<>
   [isAnimationActive config]
   [rc/line :src (rc/at) :size "2px"]
   [tooltip config]
   [rc/line :src (rc/at) :size "2px"]
   [legend config]])


;; endregion

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; HIGH-LEVEL COMPONENT 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(defn standard-chart-components [config]
  (let [grid? (reaction (get-in @config [:grid :include]))
        x-axis? (reaction (get-in @config [:x-axis :include]))
        y-axis? (reaction (get-in @config [:y-axis :include]))
        tooltip? (reaction (get-in @config [:tooltip :include]))
        legend? (reaction (get-in @config [:legend :include]))]

    [:<>
     (when @grid? [:> CartesianGrid {:strokeDasharray (strokeDasharray config)
                                     :stroke          (get-in @config [:grid :stroke])}])

     (when @x-axis? [:> XAxis {:dataKey     :name
                               :orientation (get-in @config [:x-axis :orientation])
                               :scale       (get-in @config [:x-axis :scale])}])

     (when @y-axis? [:> YAxis {:orientation (get-in @config [:y-axis :orientation])
                               :scale       (get-in @config [:y-axis :scale])}])

     (when @tooltip? [:> Tooltip])

     (when @legend? [:> Legend {:layout        (get-in @config [:legend :layout])
                                :align         (get-in @config [:legend :align])
                                :verticalAlign (get-in @config [:legend :verticalAlign])}])]))


(defn non-gridded-chart-components [config]
  (let [tooltip? (reaction (get-in @config [:tooltip :include]))
        legend? (reaction (get-in @config [:legend :include]))]

    [:<>
     (when @tooltip? [:> Tooltip])

     (when @legend? [:> Legend {:layout        (get-in @config [:legend :layout])
                                :align         (get-in @config [:legend :align])
                                :verticalAlign (get-in @config [:legend :verticalAlign])}])]))

;; endregion
