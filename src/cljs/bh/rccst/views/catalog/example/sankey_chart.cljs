(ns bh.rccst.views.catalog.example.sankey-chart
  (:require [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]
            ["recharts" :refer [Sankey Tooltip Layer Rectangle]]
            [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]

            [bh.rccst.events :as events]
            [bh.rccst.views.catalog.utils :as bcu]

            [bh.rccst.views.catalog.example.chart.utils :as utils]))


(def config (r/atom {:tooltip {:include true}
                     :node    {:fill "#77c878"}
                     :link    {:stroke "#77c878"
                               :curve 0.5}}))


(defn- config-panel [config]
  [rc/v-box :src (rc/at)
   :height "500px"
   :gap "5px"
   :children [[utils/tooltip config]
              [rc/line :size "2px"]
              [utils/color-config-text config "node fill" [:node :fill] :right-below]
              [rc/line :size "2px"]
              [utils/color-config-text config "link stroke" [:link :stroke] :right-below]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[utils/text-config config ":curve" [:link :curve]]
                          [utils/slider-config config 0 1 0.1 [:link :curve]]]]]])


(defn- complex-node
  "
> See [here](https://cljdoc.org/d/reagent/reagent/1.1.0/doc/tutorials/react-features#hooks)
> for details on how the Reagent/React interop work for this
"
  [containerWidth fill props]
  (let [{x                           "x" y "y"
         width                       "width" height "height"
         index                       "index"
         {name "name" value "value"} "payload"} (js->clj props)
        isOut (< containerWidth (+ x width 30 6))]
    (r/as-element
      [:> Layer {:key (str "CustomNode$" index)}
       [:> Rectangle {:x x :y y :width width :height height :fill fill}]
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


(defn- component [data config]
  (let [tooltip? (reaction (get-in @config [:tooltip :include]))
        stroke (reaction (get-in @config [:link :stroke]))
        fill (reaction (get-in @config [:node :fill]))
        curve (reaction (get-in @config [:link :curve]))]
    (fn []
      [:> Sankey
       {:width         500 :height 400
        :node          (partial complex-node 500 @fill)
        :data          @data
        :margin        {:top 20 :bottom 20 :left 20 :right 20}
        :nodeWidth     10 :nodePadding 60
        :linkCurvature @curve
        :iterations    64
        :link          {:stroke @stroke}}
       (when @tooltip? [:> Tooltip])])))


(defn example []
  (utils/init-config-panel "sankey-chart-demo")

  (let [data (r/atom utils/dag-data)]
    (bcu/configurable-demo
      "Sankey Chart"
      "A simple Sankey Chart built using [Recharts](https://recharts.org/en-US/api/Sankey)

> Note: the API page for Sankey is woefully incomplete, it does NOT explain how to build the
\"custom\" node or link components used in the example. You need to look at the [demo source](https://github.com/recharts/recharts/blob/master/demo/component/Sankey.tsx)
***AND*** the source for the [custom node](https://github.com/recharts/recharts/blob/master/demo/component/DemoSankeyNode.tsx) to understand how this all works."
      [:sankey-chart-demo/config :sankey-chart-demo/data :sankey-chart-demo/tab-panel :sankey-chart-demo/selected-tab]
      [:div "Dummy Data here"]                              ;[utils/tabular-data-panel data]
      [config-panel config]
      [component data config]
      '[])))
