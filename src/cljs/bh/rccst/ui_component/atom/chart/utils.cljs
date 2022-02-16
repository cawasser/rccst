(ns bh.rccst.ui-component.atom.chart.utils
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]

            ["recharts" :refer [XAxis YAxis CartesianGrid Tooltip Legend]]
            ["react-colorful" :refer [HexColorPicker]]

            [bh.rccst.events :as events]
            [bh.rccst.ui-component.utils :as u]
            [bh.rccst.ui-component.table :as table]

            [woolybear.packs.tab-panel :as tab-panel]))


(defn init-config-panel
  "this need some REALLY GOOD documentation!"
  [base-id]
  ;(log/info "init-config-panel" base-id)
  (let [formal-id (keyword base-id)
        data-path [formal-id :tab-panel]
        config-id (keyword base-id "config")
        data-id (keyword base-id "data")
        db-id (keyword "db" base-id)
        tab-id (keyword base-id "tab-panel")
        value-id (keyword base-id "value")
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
      value-id
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
                   {:name "Page A" :uv 1890 :pv 4800 :amt 2181}
                   {:name "Page A" :uv 2390 :pv 3800 :amt 2500}
                   {:name "Page G" :uv 3490 :pv 4300 :amt 2100}])

(def tabular-data-org [{:name "Page A" :org "Alpha" :uv 4000 :pv 2400 :amt 2400}
                       {:name "Page B" :org "Alpha" :uv 3000 :pv 1398 :amt 2210}
                       {:name "Page C" :org "Bravo" :uv 2000 :pv 9800 :amt 2290}
                       {:name "Page D" :org "Bravo" :uv 2780 :pv 3908 :amt 2000}
                       {:name "Page A" :org "Charlie" :uv 1890 :pv 4800 :amt 2181}
                       {:name "Page A" :org "Charlie" :uv 2390 :pv 3800 :amt 2500}
                       {:name "Page G" :org "Charlie" :uv 3490 :pv 4300 :amt 2100}])

(def meta-tabular-data
  "docstring"
  {:metadata {:type :tabular
              :id :name
              :fields {:name :string :uv :number :pv :number :tv :number :amt :number :owner :string}}
   :data [{:name "Page A" :uv 4000 :pv 2400 :tv 1500 :amt 2400 :owner "Bob"}
          {:name "Page B" :uv 3000 :pv 1398 :tv 1500 :amt 2210 :owner "Bob"}
          {:name "Page C" :uv 2000 :pv 9800 :tv 1500 :amt 2290 :owner "Sally"}
          {:name "Page D" :uv 2780 :pv 3908 :tv 1500 :amt 2000 :owner "Sally"}
          {:name "Page E" :uv 1890 :pv 4800 :tv 1500 :amt 2181 :owner "Alex"}
          {:name "Page F" :uv 2390 :pv 3800 :tv 1500 :amt 2500 :owner "Erin"}
          {:name "Page G" :uv 3490 :pv 4300 :tv 1500 :amt 2100 :owner "Alvin"}]})

(def some-other-tabular [{:id "Page A" :a 4000 :b 2400 :c 2400}
                         {:id "Page B" :a 3000 :b 1398 :c 2210}
                         {:id "Page C" :a 2000 :b 9800 :c 2290}
                         {:id "Page D" :a 2780 :b 3908 :c 2000}
                         {:id "Page E" :a 1890 :b 4800 :c 2181}
                         {:id "Page F" :a 2390 :b 3800 :c 2500}
                         {:id "Page G" :a 3490 :b 4300 :c 2100}])

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

(def hierarchy-data [{:name     "axis"
                      :children [{:name "Axis" :size 24593}
                                 {:name "Axes" :size 1302}
                                 {:name "AxisGridLine" :size 652}
                                 {:name "AxisLabel" :size 636}
                                 {:name "CartesianAxes" :size 6703}]}
                     {:name     "controls"
                      :children [{:name "TooltipControl" :size 8435}
                                 {:name "SelectionControl" :size 7862}
                                 {:name "PanZoomControl" :size 5222}
                                 {:name "HoverControl" :size 4896}
                                 {:name "ControlList" :size 4665}
                                 {:name "ClickControl" :size 3824}
                                 {:name "ExpandControl" :size 2832}
                                 {:name "DragControl" :size 2649}
                                 {:name "AnchorControl" :size 2138}
                                 {:name "Control" :size 1353}
                                 {:name "IControl" :size 763}]}
                     {:name     "data"
                      :children [{:name "Data" :size 20544}
                                 {:name "NodeSprite" :size 19382}
                                 {:name "DataList" :size 19788}
                                 {:name "DataSprite" :size 10349}
                                 {:name "EdgeSprite" :size 3301}
                                 {:name     "render"
                                  :children [{:name "EdgeRenderer" :size 5569}
                                             {:name "ShapeRenderer" :size 2247}
                                             {:name "ArrowType" :size 698}
                                             {:name "IRenderer" :size 353}]}
                                 {:name "ScaleBinding" :size 11275}
                                 {:name "TreeBuilder" :size 9930}
                                 {:name "Tree" :size 7147}]}
                     {:name     "events"
                      :children [{:name "DataEvent" :size 7313}
                                 {:name "SelectionEvent" :size 6880}
                                 {:name "TooltipEvent" :size 3701}
                                 {:name "VisualizationEvent" :size 2117}]}
                     {:name     "legend"
                      :children [{:name "Legend" :size 20859}
                                 {:name "LegendRange" :size 10530}
                                 {:name "LegendItem" :size 4614}]}
                     {:name     "operator"
                      :children [{:name     "distortion"
                                  :children [{:name "Distortion" :size 6314}
                                             {:name "BifocalDistortion" :size 4461}
                                             {:name "FisheyeDistortion" :size 3444}]}
                                 {:name     "encoder"
                                  :children [{:name "PropertyEncoder" :size 4138}
                                             {:name "Encoder" :size 4060}
                                             {:name "ColorEncoder" :size 3179}
                                             {:name "SizeEncoder" :size 1830}
                                             {:name "ShapeEncoder" :size 1690}]}
                                 {:name     "filter"
                                  :children [{:name "FisheyeTreeFilter" :size 5219}
                                             {:name "VisibilityFilter" :size 3509}
                                             {:name "GraphDistanceFilter" :size 3165}]}
                                 {:name "IOperator" :size 1286}
                                 {:name     "label"
                                  :children [{:name "Labeler" :size 9956}
                                             {:name "RadialLabeler" :size 3899}
                                             {:name "StackedAreaLabeler" :size 3202}]}
                                 {:name     "layout"
                                  :children [{:name "RadialTreeLayout" :size 12348}
                                             {:name "NodeLinkTreeLayout" :size 12870}
                                             {:name "CirclePackingLayout" :size 12003}
                                             {:name "CircleLayout" :size 9317}
                                             {:name "TreeMapLayout" :size 9191}
                                             {:name "StackedAreaLayout" :size 9121}
                                             {:name "Layout" :size 7881}
                                             {:name "AxisLayout" :size 6725}
                                             {:name "IcicleTreeLayout" :size 4864}
                                             {:name "DendrogramLayout" :size 4853}
                                             {:name "ForceDirectedLayout" :size 8411}
                                             {:name "BundledEdgeRouter" :size 3727}
                                             {:name "IndentedTreeLayout" :size 3174}
                                             {:name "PieLayout" :size 2728}
                                             {:name "RandomLayout" :size 870}]}
                                 {:name "OperatorList" :size 5248}
                                 {:name "OperatorSequence" :size 4190}
                                 {:name "OperatorSwitch" :size 2581}
                                 {:name "Operator" :size 2490}
                                 {:name "SortOperator" :size 2023}]}])

(def dag-data {:nodes [{:name "Visit"}
                       {:name "Direct-Favourite"}
                       {:name "Page-Click"}
                       {:name "Detail-Favourite"}
                       {:name "Lost"}]
               :links [{:source 0 :target 1 :value 3728.3}
                       {:source 0 :target 2 :value 354170}
                       {:source 2 :target 3 :value 62429}
                       {:source 2 :target 4 :value 291741}]})


;; endregion


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; DATA DISPLAY/EDIT PANELS
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(defn tabular-data-panel
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


(defn meta-tabular-data-panel
  "provides a simple tabular component (via `bh.rccst.ui-component.table`) to show the data presented
  in the Chart.

> Note: `table` uses the keys of the first hash-map in `@data` as the header label for the columns

  ---

  - data : (atom) atom wrapping data with metadata included"

  [data]
  [table/meta-table
   :width 500
   :data data
   :max-rows 5])


(defn dag-data-panel
  "provides a UI component to show the DAG data presented in the Chart.

> Note: `table` uses the keys of the first hash-map in `@data` as the header label for the columns

  ---

  - data : (atom) vector of content hash-maps."

  [data]
  [:div "DAG data will be shown here"])


(defn hierarchy-data-panel
  "provides a UI component to show the hierarchical data presented in the Chart.

  ---

  - data : (atom) data to show/edit"

  [data]
  [:div "hierarchical data will be shown here"])

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
                                         :dataKey     ""
                                         :orientation :bottom
                                         :scale       "auto"}
                     :y-axis            {:include     true
                                         :dataKey     ""
                                         :orientation :left
                                         :scale       "auto"}
                     :tooltip           {:include true}
                     :legend            {:include       true
                                         :layout        "horizontal"
                                         :align         "center"
                                         :verticalAlign "bottom"}})


(defn column-picker [data widget-id label path]
  (let [model (u/subscribe-local widget-id path)
        headings (apply set (map keys (get @data :data)))
        btns (mapv (fn [h] {:id h :label h}) headings)]
    (fn [data widget-id label path]
      [rc/h-box :src (rc/at)
       :gap "5px"
       :children [[rc/box :src (rc/at) :align :start :child [:code label]]
                  [rc/horizontal-bar-tabs
                   :src (rc/at)
                   :model @model
                   :tabs btns
                   :style btns-style
                   :on-change #(u/dispatch-local widget-id path %)]]])))


(defn boolean-config
  "lets the user turn on/of some component of the Chart

  ---

  - config : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - label : (string) tell the user which subcomponent this control is manipulating
  - path : (vector) path into `config` where the subcomponent 'inclusion' value is stored
  "
  [config label path]

  (let [checked? (u/subscribe-local config path)]
    (fn [config label path]
      [rc/checkbox :src (rc/at)
       :label [rc/box :src (rc/at) :align :start :child [:code label]]
       :model @checked?
       :on-change #(u/dispatch-local config path %)])))


(defn slider-config
  ([widget-id min max step path]
   (let [model (u/subscribe-local widget-id path)]
     (fn [widget-id min max step path]
       [rc/slider :src (rc/at)
        :model @model
        :width "100px"
        :min min :max max :step step
        :on-change #(u/dispatch-local widget-id path %)])))

  ([widget-id min max path]
   [slider-config widget-id min max 1 path]))


(defn text-config [widget-id label path]
  (let [model (u/subscribe-local widget-id path)]
    (fn [widget-id label path])
    [rc/h-box :src (rc/at)
     :gap "5px"
     :children [[rc/label :src (rc/at) :label label]
                [rc/input-text :src (rc/at)
                 :model (str @model)
                 :width "50px"
                 :on-change #(u/dispatch-local widget-id path %)]]]))


(defn strokeDasharray
  "reconstitutes the 2-part string value required by `:strokeDasharray` from the
  2 values in the [[config]] atom.

  ---

  - dash
  - space
  "
  [dash & space]
  (str dash " " (first space)))


(defn dashArray-config
  "provides the user with 2 sliders to control the 2 parts of the `:strokeDasharray`
  property of a chart's [`CartesianGrid`](https://recharts.org/en-US/api/CartesianGrid)

  ---

  - config : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - label : (string) tell the user which axis this control is manipulating
  - min : (integer) minimum value for the slider
  - max : (integer) maximum value for the slider
  - path : (vector) path into `config` where the :strokeDasharray is stored
  "

  [widget-id label min max path]
  [rc/h-box :src (rc/at)
   :children [[rc/box :src (rc/at) :align :start :child [:code label]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[slider-config widget-id min max (conj path :dash)]
                          [slider-config widget-id min max (conj path :space)]]]]])


(defn orientation-config
  "lets the user configure the orientation of an axis. Which axis is defined by the arguments.

  ---

  - config : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - btns : (vector) define the button that set the value(s).

  | key       | description                                                          |
  |:----------|:---------------------------------------------------------------------|
  | `:id`     | the value to set when the use click the corresponding button control |
  | `:label`  | the label to put on the button                                       |

  Each axis support a different set of possible orientations:

  | axis      | allowed orientations   |
  |:----------|:-----------------------|
  | X Axis    | `:top`  `:bottom`     |
  | Y Axis    | `:left`  `:right`     |

  - label : (string) tell the user which axis this control is manipulating
  - path : (vector) path into `config` where the orientation for the correct axis is stored
  "
  [widget-id btns label path]

  (let [model (u/subscribe-local widget-id path)]
    (fn [widget-id btns label path]
      [rc/h-box :src (rc/at)
       :children [[rc/box :src (rc/at) :align :start :child [:code label]]
                  [rc/horizontal-bar-tabs
                   :src (rc/at)
                   :model @model
                   :tabs btns
                   :style btns-style
                   :on-change #(u/dispatch-local widget-id path %)]]])))


(defn scale-config
  "lets the user change the scale of an 'axis'. Which axis is defined by the arguments.
  Supports only:

    `auto`  `linear`  `pow`  `sqrt`  `log`

  scale types. Recharts supports many more. See [here](https://recharts.org/en-US/api/XAxis#scale)

  ---

  - config : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - label : (string) tell the user which axis this control is manipulating
  - path : (vector) path into `config` where the scale for the correct axis is stored
  "
  [widget-id label path]
  (let [model (u/subscribe-local widget-id path)
        btns [{:id "auto" :label "auto"}
              {:id "linear" :label "linear"}
              {:id "pow" :label "pow"}
              {:id "sqrt" :label "sqrt"}
              {:id "log" :label "log"}]]
    (fn [widget-id label path]
      [rc/h-box :src (rc/at)
       :children [[rc/box :src (rc/at) :align :start :child [:code label]]
                  [rc/horizontal-bar-tabs
                   :src (rc/at)
                   :model @model
                   :tabs btns
                   :style btns-style
                   :on-change #(u/dispatch-local widget-id path %)]]])))


(defn layout-config
  "lets the user change the layout of a 'legend'.
  Supports:

    `horizontal`  &  `linear`

  ---

  - widget-id : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - path : (vector) path into `config` where the scale for the layout is stored
  "
  [widget-id path]
  (let [model (u/subscribe-local widget-id path)
        btns [{:id "horizontal" :label "horizontal"}
              {:id "vertical" :label "vertical"}]]
    (fn [widget-id path]
      [rc/h-box :src (rc/at)
       :children [[rc/box :src (rc/at) :align :start :child [:code ":layout"]]
                  [rc/horizontal-bar-tabs
                   :src (rc/at)
                   :model @model
                   :tabs btns
                   :style btns-style
                   :on-change #(u/dispatch-local widget-id path %)]]])))


(defn align-config
  "lets the user change the alignment of a 'legend'.
  Supports:

    `left`  `center`  `right`

  ---

  - config : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - path : (vector) path into `config` where the scale for the layout is stored
  "
  [widget-id path]
  (let [model (u/subscribe-local widget-id path)
        btns [{:id "left" :label "left"}
              {:id "center" :label "center"}
              {:id "right" :label "right"}]]
    (fn [widget-id path]
      [rc/h-box :src (rc/at)
       :children [[rc/box :src (rc/at) :align :start :child [:code ":align"]]
                  [rc/horizontal-bar-tabs
                   :src (rc/at)
                   :model @model
                   :tabs btns
                   :style btns-style
                   :on-change #(u/dispatch-local widget-id path %)]]])))


(defn verticalAlign-config
  "lets the user change the vetical alignment of a 'legend'.
  Supports:

    `top`  `middle`  `bottom`

  ---

  - config : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - path : (vector) path into `config` where the scale for the layout is stored
  "
  [widget-id path]
  (let [model (u/subscribe-local widget-id path)
        btns [{:id "top" :label "top"}
              {:id "middle" :label "middle"}
              {:id "bottom" :label "bottom"}]]
    (fn [widget-id path]
      [rc/h-box :src (rc/at)
       :children [[rc/box :src (rc/at) :align :start :child [:code ":verticalAlign"]]
                  [rc/horizontal-bar-tabs
                   :src (rc/at)
                   :model @model
                   :tabs btns
                   :style btns-style
                   :on-change #(u/dispatch-local widget-id path %)]]])))


(defn color-config [widget-id label path & [position]]
  (let [showing? (r/atom false)
        p (or position :right-center)
        background-color (u/subscribe-local widget-id path)]
    (fn [widget-id label path & [position]]
      [rc/popover-anchor-wrapper :src (rc/at)
       :showing? @showing?
       :position p
       :anchor [rc/button :src (rc/at)
                :label label
                :style {:background-color @background-color
                        :color            (u/best-text-color
                                            (u/hex->rgba @background-color))}
                :on-click #(swap! showing? not)]
       :popover [rc/popover-content-wrapper :src (rc/at)
                 :close-button? true
                 :no-clip? true
                 :body [:> HexColorPicker {:color     @background-color
                                           :on-change #(u/dispatch-local widget-id path %)}]]])))


(defn color-config-text [widget-id label path & [position]]
  (let [model (u/subscribe-local widget-id path)]
    (fn [widget-id label path & [position]]
      [rc/h-box :src (rc/at)
       :gap "5px"
       :children [[color-config widget-id label path position]
                  [rc/input-text :src (rc/at)
                   :width "100px"
                   :model @model
                   :on-change #(u/dispatch-local widget-id path %)]]])))


;; endregion


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; HIGH-LEVEL CONFIGURATION 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(defn isAnimationActive [widget-id]
  [boolean-config widget-id ":isAnimationActive" [:isAnimationActive]])


(defn grid [widget-id]
  [rc/v-box :src (rc/at)
   :children [[boolean-config widget-id ":grid" [:grid :include]]
              [dashArray-config widget-id
               ":strokeDasharray" 1 10 [:grid :strokeDasharray]]
              [color-config-text widget-id ":stroke" [:grid :stroke]]]])


(defn x-axis [data widget-id]
  [rc/v-box :src (rc/at)
   :children [[boolean-config widget-id ":x-axis" [:x-axis :include]]
              [column-picker data widget-id ":dataKey" [:x-axis :dataKey]]
              [orientation-config widget-id x-axis-btns ":orientation" [:x-axis :orientation]]
              [scale-config widget-id ":scale" [:x-axis :scale]]]])


(defn y-axis [data widget-id]
  [rc/v-box :src (rc/at)
   :children [[boolean-config widget-id ":y-axis" [:y-axis :include]]
              [column-picker data widget-id ":dataKey" [:y-axis :dataKey]]
              [orientation-config widget-id y-axis-btns ":orientation" [:y-axis :orientation]]
              [scale-config widget-id ":scale" [:y-axis :scale]]]])


(defn tooltip [widget-id]
  [boolean-config widget-id ":tooltip" [:tooltip :include]])


(defn legend [widget-id]
  [rc/v-box :src (rc/at)
   :children [[boolean-config widget-id ":legend" [:legend :include]]
              [layout-config widget-id [:legend :layout]]
              [align-config widget-id [:legend :align]]
              [verticalAlign-config widget-id [:legend :verticalAlign]]]])


(defn option [chart-id label path-root]
  (let [chosen-path (conj path-root :chosen)
        keys-path (conj path-root :keys)
        chosen (u/subscribe-local chart-id chosen-path)
        keys (u/subscribe-local chart-id keys-path)
        btns (->> @keys
               (map (fn [k]
                      {:id k :label k})))]

    ;(log/info "option" @keys @chosen btns)

    (fn [chart-id label path-root]
      [rc/h-box :src (rc/at)
       :children [[rc/box :src (rc/at) :align :start :child [:code label]]
                  [rc/horizontal-bar-tabs
                   :src (rc/at)
                   :model @chosen
                   :tabs btns
                   :style btns-style
                   :on-change #(u/dispatch-local chart-id chosen-path %)]]])))

;; endregion


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; VERY!!! HIGH-LEVEL CONFIGURATION 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(defn standard-chart-config [data widget-id]
  [:<>
   [isAnimationActive widget-id]
   [rc/line :src (rc/at) :size "2px"]
   [grid widget-id]
   [rc/line :src (rc/at) :size "2px"]
   [x-axis data widget-id]
   [rc/line :src (rc/at) :size "2px"]
   [y-axis data widget-id]
   [rc/line :src (rc/at) :size "2px"]
   [tooltip widget-id]
   [rc/line :src (rc/at) :size "2px"]
   [legend widget-id]])


(defn non-gridded-chart-config [widget-id]
  [:<>
   [isAnimationActive widget-id]
   [rc/line :src (rc/at) :size "2px"]
   [tooltip widget-id]
   [rc/line :src (rc/at) :size "2px"]
   [legend widget-id]])


;; endregion

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; HIGH-LEVEL COMPONENT 'MOLECULES'
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(defn standard-chart-components [widget-id]
  (let [grid? (u/subscribe-local widget-id [:grid :include])
        grid-dash (u/subscribe-local widget-id [:grid :strokeDasharray :dash])
        grid-space (u/subscribe-local widget-id [:grid :strokeDasharray :space])
        grid-stroke (u/subscribe-local widget-id [:grid :stroke])

        x-axis? (u/subscribe-local widget-id [:x-axis :include])
        x-axis-dataKey (u/subscribe-local widget-id [:x-axis :dataKey])
        x-axis-orientation (u/subscribe-local widget-id [:x-axis :orientation])
        x-axis-scale (u/subscribe-local widget-id [:x-axis :scale])

        y-axis? (u/subscribe-local widget-id [:y-axis :include])
        y-axis-dataKey (u/subscribe-local widget-id [:y-axis :dataKey])
        y-axis-orientation (u/subscribe-local widget-id [:y-axis :orientation])
        y-axis-scale (u/subscribe-local widget-id [:y-axis :scale])

        tooltip? (u/subscribe-local widget-id [:tooltip :include])

        legend? (u/subscribe-local widget-id [:legend :include])
        legend-layout (u/subscribe-local widget-id [:legend :layout])
        legend-align (u/subscribe-local widget-id [:legend :align])
        legend-verticalAlign (u/subscribe-local widget-id [:legend :verticalAlign])]

    [:<>
     (when @grid? [:> CartesianGrid {:strokeDasharray (strokeDasharray @grid-dash @grid-space)
                                     :stroke          @grid-stroke}])

     (when @x-axis? [:> XAxis {:dataKey     @x-axis-dataKey
                               :orientation @x-axis-orientation
                               :scale       @x-axis-scale}])

     (when @y-axis? [:> YAxis {:dataKey     @y-axis-dataKey
                               :orientation @y-axis-orientation
                               :scale       @y-axis-scale}])

     (when @tooltip? [:> Tooltip])

     (when @legend? [:> Legend {:layout        @legend-layout
                                :align         @legend-align
                                :verticalAlign @legend-verticalAlign}])]))


(defn non-gridded-chart-components [widget-id]
  (let [tooltip? (u/subscribe-local widget-id [:tooltip :include])
        legend? (u/subscribe-local widget-id [:legend :include])
        legend-layout (u/subscribe-local widget-id [:legend :layout])
        legend-align (u/subscribe-local widget-id [:legend :align])
        legend-verticalAlign (u/subscribe-local widget-id [:legend :verticalAlign])]

    [:<>
     (when @tooltip? [:> Tooltip])

     (when @legend? [:> Legend {:layout        @legend-layout
                                :align         @legend-align
                                :verticalAlign @legend-verticalAlign}])]))

;; endregion

