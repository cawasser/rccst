(ns bh.rccst.views.catalog.example.sankey-chart
  (:require [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]
            ["recharts" :refer [Sankey Tooltip Rectangle]]
            [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]

            [bh.rccst.events :as events]
            [bh.rccst.views.catalog.utils :as bcu]

            [bh.rccst.views.catalog.example.chart.utils :as utils]))


(def config (r/atom {:tooltip {:include true}
                     :link {:stroke "#77c878"}}))


(defn- config-panel [config]
  [rc/v-box :src (rc/at)
   :height "500px"
   :gap "5px"
   :children [[utils/tooltip config]
              [rc/line :size "2px"]
              [utils/color-config-text config "link stroke" [:link :stroke]]]])


(defn- custom-node [props]
  (log/info "sankey node" (js->clj props))

  (let [{x "x" y "y" width "width" height "height"} (js->clj props)]
    [:> Rectangle {:x x :y y :width width :height height :fill "#0089DD"}]))


(comment
  (def props {"x" 385, "y" 4.9999999999999325,
              "width" 10, "height" 301.60570754317644,
              "index" 4,
              "payload" {"targetNodes" [],
                         "sourceNodes" [2],
                         "x" 380, "dx" 10,
                         "sourceLinks" [3],
                         "name" "Lost",
                         "value" 291741,
                         "targetLinks" [],
                         "y" -6.750155989720952e-14,
                         "depth" 2,
                         "dy" 301.60570754317644}})

  (let [{x "x" y "y" width "width" height "height"} (js->clj props)]
    {:x x :y y :width width :height height})


  ())

(defn- component [data config]
  (let [tooltip? (reaction (get-in @config [:tooltip :include]))
        stroke (reaction (get-in @config [:link :stroke]))]
    (fn []
      [:> Sankey
       {:width 400 :height 400
        ;:node custom-node
        :data @data
        :link {:stroke @stroke}}
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
