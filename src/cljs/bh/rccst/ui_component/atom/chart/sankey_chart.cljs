(ns bh.rccst.ui-component.atom.chart.sankey-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]

            ["recharts" :refer [Sankey Tooltip Layer Rectangle]]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(def sample-data
  "the Sankey Chart works best with \"directed acyclic graph data\" so we return the dag-data from utils"
  (r/atom utils/dag-data))


(defn config
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a line-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. line charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - component-id : (string) unique id of the chart
  "
  [component-id data]
  (merge
    ui-utils/default-pub-sub
    {:tab-panel {:value     (keyword component-id "config")
                 :data-path [:widgets (keyword component-id) :tab-panel]}
     :tooltip   {:include true}
     :node      {:fill "#77c878" :stroke "#000000"}
     :link      {:stroke "#77c878" :curve 0.5}}))


(defn- config-panel
  "the panel of configuration controls

  ---

  - _ : (ignored)
  - component-id : (string) unique identifier for this chart instance
  "
  [_ component-id]
  [rc/v-box :src (rc/at)
   :width "400px"
   :height "500px"
   :gap "5px"
   :children [[utils/tooltip component-id]
              [rc/line :size "2px"]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[utils/color-config-text component-id "node fill" [:node :fill] :right-below]
                          [utils/color-config-text component-id "node stroke" [:node :stroke] :right-below]]]
              [rc/line :size "2px"]
              [utils/color-config-text component-id "link stroke" [:link :stroke] :right-below]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[utils/text-config component-id ":curve" [:link :curve]]
                          [utils/slider-config component-id 0 1 0.1 [:link :curve]]]]]])


(def source-code '[:> Sankey
                   {:width         500 :height 400
                    :node          (partial complex-node 500 @fill)
                    :data          @data
                    :margin        {:top 20 :bottom 20 :left 20 :right 20}
                    :nodeWidth     10
                    :nodePadding   60
                    :linkCurvature @curve
                    :iterations    64
                    :link          {:stroke @stroke}}
                   (when @tooltip? [:> Tooltip])])


(defn- complex-node
  "build the reagent/react components (as hiccup) needed to draw the `node` parts (rectangles)
  and labels of the diagram.

  ---

  - containerWidth : (number) with of the container, used to determine if the label shoule be ot the left or right of the rectangle
  - fill : (color) color to fill the rectangle
  - stroke : (color) color to use as the outline (stroke) of the rectangle
  - props : (has-map) additional props sent to the reagent/react component by the diagram itself

> See [here](https://cljdoc.org/d/reagent/reagent/1.1.0/doc/tutorials/react-features#hooks)
> for details on how the Reagent/React interop work for this
"
  [containerWidth fill stroke props]
  (let [{x                           "x"
         y                           "y"
         width                       "width"
         height                      "height"
         index                       "index"
         {name "name" value "value"} "payload"} (js->clj props)
        isOut (< containerWidth (+ x width 30 6))]
    (r/as-element
      [:> Layer {:key (str "CustomNode$" index)}
       [:> Rectangle {:x x :y y :width width :height height :fill fill :stroke stroke}]
       [:text {:textAnchor (if isOut "end" "start")
               :x          (if isOut (- x 6) (+ x width 6))
               :y          (+ y (/ height 2))
               :fontSize   14
               :stroke     "#333"}
        name]
       [:text {:textAnchor    (if isOut "end" "start")
               :x             (if isOut (- x 6) (+ x width 6))
               :y             (+ y 13 (/ height 2))
               :fontSize      12
               :stroke        "#333"
               :strokeOpacity 0.5}
        (str value "k")]])))


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - chart-id : (string) unique identifier for this chart instance within this container
  - container-id : (string) name of the container this chart is inside of
  "
  [data component-id]
  (let [tooltip? (ui-utils/subscribe-local component-id [:tooltip :include])
        node-fill (ui-utils/subscribe-local component-id [:node :fill])
        node-stroke (ui-utils/subscribe-local component-id [:node :stroke])
        link-stroke (ui-utils/subscribe-local component-id [:link :stroke])
        curve (ui-utils/subscribe-local component-id [:link :curve])]

    (fn []
      [:> Sankey
       {:width         500 :height 400
        :node          (partial complex-node 500 @node-fill @node-stroke)
        :data          @data
        :margin        {:top 20 :bottom 20 :left 20 :right 20}
        :nodeWidth     10
        :nodePadding   60
        :linkCurvature @curve
        :iterations    64
        :link          {:stroke @link-stroke}}
       (when @tooltip? [:> Tooltip])])))


(defn configurable-component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - chart-id : (string) unique identifier of this chart insatnce within this container
  - container-id : (string) name of the container this chart is inside of
  "
  ([& {:keys [data component-id container-id]}]
   [c/base-chart
    :data data
    :config (config component-id data)
    :component-id component-id
    :container-id (or container-id "")
    :data-panel utils/dag-data-panel
    :config-panel config-panel
    :component-panel component-panel]))


(defn component
  "the chart to draw. this variant does NOT provide a configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - component-id : (string) unique identifier of this chart instance within this container
  - container-id : (string) name of the container this chart is inside of
  "
  ([& {:keys [data component-id container-id]}]
   [c/base-chart
    :data data
    :config (config component-id data)
    :component-id component-id
    :container-id (or container-id "")
    :component-panel component-panel]))


(comment
  (def chart-id "sankey-chart-demo")

  ())