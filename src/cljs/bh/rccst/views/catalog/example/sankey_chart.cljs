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
                     :link    {:stroke "#77c878"}}))


(defn- config-panel [config]
  [rc/v-box :src (rc/at)
   :height "500px"
   :gap "5px"
   :children [[utils/tooltip config]
              [rc/line :size "2px"]
              [utils/color-config-text config "link stroke" [:link :stroke]]]])


(defn- complex-node [props]
  (let [{x                           "x" y "y"
         width                       "width" height "height"
         index                       "index"
         {name "name" value "value"} "payload"
         containerWidth              "containerWidth"} (js->clj props)
        isOut (< containerWidth (+ x width 6))]
    (r/as-element
      [:> Layer {:key (str "CustomNode$" index)}
       [:> Rectangle {:x x :y y :width width :height height :fill "#77c878"}]
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
        stroke (reaction (get-in @config [:link :stroke]))]
    (fn []
      [:> Sankey
       {:width 400 :height 400
        :node  complex-node
        :data  @data
        :link  {:stroke @stroke}}
       (when @tooltip? [:> Tooltip])])))


(defn example []
  (utils/init-config-panel "sankey-chart-demo")

  (let [data (r/atom utils/dag-data)]
    (bcu/configurable-demo
      "Sankey Chart"
      "A simple Sankey Chart built using [Recharts](https://recharts.org/en-US/api/Sankey)"
      [:sankey-chart-demo/config :sankey-chart-demo/data :sankey-chart-demo/tab-panel :sankey-chart-demo/selected-tab]
      [:div "Dummy Data here"]                              ;[utils/tabular-data-panel data]
      [config-panel config]
      [component data config]
      '[])))
