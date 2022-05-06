(ns bh.rccst.ui-component.atom.chart.utils
  (:require [bh.rccst.events :as events]
            [bh.rccst.ui-component.atom.re-com.table :as table]
            [bh.rccst.ui-component.utils :as u]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]

            ["recharts" :refer [XAxis YAxis CartesianGrid Tooltip Legend]]
            ["react-colorful" :refer [HexColorPicker]]))


(log/info "bh.rccst.ui-component.atom.chart.utils")


(defn init-config-panel
  "this need some REALLY GOOD documentation!"
  [base-id]
  ;(log/info "init-config-panel" base-id)
  (let [formal-id (keyword base-id)
        data-path [formal-id :tab-panel]
        config-id (keyword base-id "config")
        data-id   (keyword base-id "data")
        db-id     (keyword "db" base-id)
        tab-id    (keyword base-id "tab-panel")
        value-id  (keyword base-id "value")
        init-db   {:tab-panel (tab-panel/mk-tab-panel-data
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
; DATA DISPLAY/EDIT PANELS
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(defn dummy-data-panel [data]
  [:div "a dummy data panel"])


(defn tabular-data-panel
  "provides a simple tabular component (via `bh.rccst.ui-component.table`) to show the data presented
  in the Chart.

> Note: `table` uses the keys of the first hash-map in `@data` as the header label for the columns

  ---

  - data : (atom) vector of content hash-maps."

  [data]
  ;(log/info "tabular-data-panel" @data)
  [table/table
   :width 500
   :data @data
   :max-rows 5])


(defn meta-tabular-data-panel
  "provides a simple tabular component (via `bh.rccst.ui-component.table`) to show the data presented
  in the Chart.

> Note: `table` uses the keys of the first hash-map in `@data` as the header label for the columns

  ---

  - data : (atom) atom wrapping data with metadata included"

  [data]
  ;(log/info "meta-tabular-data-panel" @data)
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
  (let [model    (u/subscribe-local widget-id path)]
    (fn []
      (let [headings (apply set (map keys (get @data :data)))
            btns     (mapv (fn [h] {:id h :label h}) headings)]
        ;(log/info "column-picker" data "//" widget-id "//" label "//" path)
        [rc/h-box :src (rc/at)
         :gap "5px"
         :children [[rc/box :src (rc/at) :align :start :child [:code label]]
                    [rc/horizontal-bar-tabs
                     :src (rc/at)
                     :model @model
                     :tabs btns
                     :style btns-style
                     :on-change #(u/dispatch-local widget-id path %)]]]))))


(defn boolean-config
  "lets the user turn on/of some component of the Chart

  ---

  - config : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - label : (string) tell the user which subcomponent this control is manipulating
  - path : (vector) path into `config` where the subcomponent 'inclusion' value is stored
  "
  [config label path]

  (let [checked? (u/subscribe-local config path)]
    (fn []
      [rc/checkbox :src (rc/at)
       :label (cond
                (and (string? label) (empty? label)) ""
                :else [rc/box :src (rc/at) :align :start :child [:code label]])
       :model @checked?
       :on-change #(u/dispatch-local config path %)])))


(defn slider-config
  ([widget-id min max step path]
   (let [model (u/subscribe-local widget-id path)]
     (fn []
       [rc/slider :src (rc/at)
        :model @model
        :width "100px"
        :min min :max max :step step
        :on-change #(u/dispatch-local widget-id path %)])))

  ([widget-id min max path]
   [slider-config widget-id min max 1 path]))


(defn text-config [widget-id label path]
  (let [model (u/subscribe-local widget-id path)]
    (fn [])
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


(defn enumerated-config
  "provides a multi-button control for setting a property fro a set of mutually-exclusive options

  ---

  - widget-id : (string/keyword) unique ID for this component
  - btns : (vector) define the button(s) that set the value(s).

  | key       | description                                                          |
  |:----------|:---------------------------------------------------------------------|
  | `:id`     | the value to set when the use click the corresponding button control |
  | `:label`  | the label to put on the button                                       |


  - label : (string) tell the user what property is being manipulated
  - path : (vector) path into `config` for the correct property

  "
  [widget-id btns label path]

  (let [model (u/subscribe-local widget-id path)]
    (fn []
      [rc/h-box :src (rc/at)
       :children [[rc/box :src (rc/at) :align :start :child [:code label]]
                  [rc/horizontal-bar-tabs
                   :src (rc/at)
                   :model @model
                   :tabs btns
                   :style btns-style
                   :on-change #(u/dispatch-local widget-id path %)]]])))


(defn orientation-config
  "lets the user configure the orientation of an axis. Which axis is defined by the arguments.

  ---

  - widget-id : (string/keyword) unique ID for this component
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

  (enumerated-config widget-id btns label path))


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
  (let [btns [{:id "auto" :label "auto"}
              {:id "linear" :label "linear"}
              {:id "pow" :label "pow"}
              {:id "sqrt" :label "sqrt"}
              {:id "log" :label "log"}]]
    (enumerated-config widget-id btns label path)))


(defn layout-config
  "lets the user change the layout of a 'legend'.
  Supports:

    `horizontal`  &  `linear`

  ---

  - widget-id : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - path : (vector) path into `config` where the scale for the layout is stored
  "
  [widget-id path]
  (let [btns [{:id "horizontal" :label "horizontal"}
              {:id "vertical" :label "vertical"}]]
    (enumerated-config widget-id btns ":layout" path)))


(defn align-config
  "lets the user change the alignment of a 'legend'.
  Supports:

    `left`  `center`  `right`

  ---

  - config : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - path : (vector) path into `config` where the scale for the layout is stored
  "
  [widget-id path]
  (let [btns [{:id "left" :label "left"}
              {:id "center" :label "center"}
              {:id "right" :label "right"}]]
    (enumerated-config widget-id btns ":align" path)))


(defn verticalAlign-config
  "lets the user change the vertical alignment of a 'legend'.
  Supports:

    `top`  `middle`  `bottom`

  ---

  - config : (atom) holds a hash-map of the actual configuration properties see [[config]].
  - path : (vector) path into `config` where the scale for the layout is stored
  "
  [widget-id path]
  (let [btns [{:id "top" :label "top"}
              {:id "middle" :label "middle"}
              {:id "bottom" :label "bottom"}]]
    (enumerated-config widget-id btns ":verticalAlign" path)))


(defn color-config [config-data label path & [position]]
  (let [d                (h/resolve-value config-data)
        showing?         (r/atom false)
        p                (or position :right-center)]

    ;(log/info "color-config" label "//" config-data "//" @d "//" path "//" @showing?)

    (fn []
      [rc/popover-anchor-wrapper :src (rc/at)
       :showing? @showing?
       :position p
       :anchor [rc/button :src (rc/at)
                :label label
                :style {:background-color (get-in @d path)
                        :color            (color/best-text-color
                                            (color/hex->rgba (get-in @d path)))}
                :on-click #(swap! showing? not)]
       :popover [rc/popover-content-wrapper :src (rc/at)
                 :close-button? false
                 :no-clip? false
                 :body [:> HexColorPicker {:color (get-in @d path)
                                           :on-change #(h/handle-change-path config-data path %)}]]])))


(defn color-config-text [widget-id label path & [position]]
  ;(log/info "color-config-text" widget-id "//" label "//" path)

  (let [model (u/subscribe-local widget-id path)]
    (fn []
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
        keys-path   (conj path-root :keys)
        chosen      (u/subscribe-local chart-id chosen-path)
        keys        (u/subscribe-local chart-id keys-path)
        btns        (->> @keys
                      (map (fn [k]
                             {:id k :label k})))]

    ;(log/info "option" @keys @chosen btns)

    (fn []
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


(defn non-gridded-chart-config [data widget-id]
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


(defn override [s ui tag]
  ;(log/info "override" s "///" ui "///" tag)
  (if (and
        (seq ui)
        (not (empty? ui))
        (contains? (into #{} (keys ui)) tag))
    (get ui tag)
    s))


(defn standard-chart-components [component-id ui]

  ;(log/info "standard-chart-components" component-id ui)

  (let [grid?                (u/subscribe-local component-id [:grid :include])
        grid-dash            (u/subscribe-local component-id [:grid :strokeDasharray :dash])
        grid-space           (u/subscribe-local component-id [:grid :strokeDasharray :space])
        grid-stroke          (u/subscribe-local component-id [:grid :stroke])

        x-axis?              (u/subscribe-local component-id [:x-axis :include])
        x-axis-dataKey       (u/subscribe-local component-id [:x-axis :dataKey])
        x-axis-orientation   (u/subscribe-local component-id [:x-axis :orientation])
        x-axis-scale         (u/subscribe-local component-id [:x-axis :scale])

        y-axis?              (u/subscribe-local component-id [:y-axis :include])
        y-axis-dataKey       (u/subscribe-local component-id [:y-axis :dataKey])
        y-axis-orientation   (u/subscribe-local component-id [:y-axis :orientation])
        y-axis-scale         (u/subscribe-local component-id [:y-axis :scale])

        tooltip?             (u/subscribe-local component-id [:tooltip :include])

        legend?              (u/subscribe-local component-id [:legend :include])
        legend-layout        (u/subscribe-local component-id [:legend :layout])
        legend-align         (u/subscribe-local component-id [:legend :align])
        legend-verticalAlign (u/subscribe-local component-id [:legend :verticalAlign])]

    [:<>
     (when (override @grid? ui :grid) [:> CartesianGrid {:strokeDasharray (strokeDasharray @grid-dash @grid-space)
                                                         :stroke          @grid-stroke}])

     (when (override @x-axis? ui :x-axis) [:> XAxis {:dataKey     @x-axis-dataKey
                                                     :orientation @x-axis-orientation
                                                     :scale       @x-axis-scale}])

     (when (override @y-axis? ui :y-axis) [:> YAxis {:dataKey     @y-axis-dataKey
                                                     :orientation @y-axis-orientation
                                                     :scale       @y-axis-scale}])

     (when (override @tooltip? ui :tooltip) [:> Tooltip])

     (when (override @legend? ui :legend) [:> Legend {:layout        @legend-layout
                                                      :align         @legend-align
                                                      :verticalAlign @legend-verticalAlign}])]))


(defn non-gridded-chart-components [component-id ui]
  (let [tooltip?             (u/subscribe-local component-id [:tooltip :include])
        legend?              (u/subscribe-local component-id [:legend :include])
        legend-layout        (u/subscribe-local component-id [:legend :layout])
        legend-align         (u/subscribe-local component-id [:legend :align])
        legend-verticalAlign (u/subscribe-local component-id [:legend :verticalAlign])]

    ;(log/info "non-gridded-chart-components" component-id ui)

    [:<>
     (when (override @tooltip? ui :tooltip) [:> Tooltip])

     (when (override @legend? ui :legend) [:> Legend {:layout        @legend-layout
                                                      :align         @legend-align
                                                      :verticalAlign @legend-verticalAlign}])]))

;; endregion


; workout the override logic for chart elements like grid, legend, etc.
(comment
  (def ui {:tooltip false})
  (def ui nil)
  (def ui "")
  (def ui {:grid false, :x-axis false, :y-axis false, :legend false, :tooltip false})
  (def tag :tooltip)
  (def tag :grid)
  (def tooltip? (r/atom true))
  (def grid? (r/atom true))
  (def s @tooltip?)
  (def s @grid?)

  (first ui)

  (if (and (seq ui) (not (empty? (first ui)))) true false)


  (if (and
        (seq ui)
        (not (empty? ui))
        (contains? (into #{} (keys ui)) tag))
    (get ui tag)
    s)


  (if nil true false)
  (or true nil)

  (override @tooltip? ui :tooltip)
  (override @grid? ui :grid)

  ())
