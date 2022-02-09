(ns bh.rccst.views.catalog.example.funnel-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            [woolybear.packs.tab-panel :as tab-panel]

            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.ui-component.atom.chart.utils :as utils]

            ["recharts" :refer [FunnelChart Funnel Cell LabelList XAxis YAxis CartesianGrid Tooltip]]))

; region ; configuration params

(def config (r/atom (merge utils/default-config
                        {:legend            {:include       false}
                         :colors (zipmap (map :name utils/paired-data)
                                         ["#8884d8" "#83a6ed" "#8dd1e1"
                                          "#82ca9d" "#a4de6c" "#d7e62b"])})))
                        ;(assoc-in [:x-axis :dataKey] :x)
                        ;(assoc-in [:y-axis :dataKey] :y)
                        ;(assoc-in [:fill :color] "#8884d8"))))

(defn- color-anchors [config]
       [:<>
        (doall
          (map (fn [[id _]]
                   ^{:key id}[utils/color-config-text config id [:colors id] :right-above])
               (:colors @config)))])

;; endregion

;; region ; config and component panels

(defn- config-panel
       "the panel of configuration controls

       ---

       - config : (atom) holds all the configuration settings made by the user
       "
       [data config]

       [rc/v-box :src (rc/at)
        :gap "10px"
        :width "100%"
        :style {:padding          "15px"
                :border-top       "1px solid #DDD"
                :background-color "#f7f7f7"}
        :children [[utils/non-gridded-chart-config config]
                   [rc/line :src (rc/at) :size "2px"]
                   [rc/v-box :src (rc/at)
                    :gap "5px"
                    :children [[rc/label :src (rc/at) :label "Funnel Colors"]
                               (color-anchors config)]]]])


(defn- component
       "the chart to draw, taking cues from the settings of the configuration panel

       ---

       - data : (atom) any data shown by the component's ui
       - config : (atom) configuration settings made by the user using the config-panel, see [[config]].
       "
       [data config]
       (let [tooltip? (reaction (:tooltip @config))
             isAnimationActive? (reaction (:isAnimationActive @config))]

            (fn []
                ;(log/info "configurable-funnel-chart" @config)

                [:> FunnelChart {:height 400 :width 500}
                 (utils/non-gridded-chart-components config)
                 [:> Funnel {:dataKey :value :data @data :isAnimationActive @isAnimationActive?}
                  (doall
                    (map-indexed (fn [idx {name :name}]
                                     [:> Cell {:key (str "cell-" idx) :fill (get-in @config [:colors name])}])
                                 @data))
                  [:> LabelList {:position :right :fill "#000000" :stroke "none" :dataKey :name}]]])))

;; endregion

(defn example []
      (utils/init-config-panel "funnel-chart-demo")

      (let [data (r/atom utils/paired-data)]
           (bcu/configurable-demo
             "Funnel Chart"
             "Funnel Chart with different colors for each chunk.  This requires embedding `Cell` elements inside the `Funnel` element"
             [:funnel-chart-demo/config :funnel-chart-demo/data :funnel-chart-demo/tab-panel :funnel-chart-demo/value]
             [utils/tabular-data-panel data]
             [config-panel data config]
             [component data config]
             '[:> FunnelChart {:height 400 :width 500}
               (utils/non-gridded-chart-components config)
               [:> Funnel {:dataKey :value :data @data :isAnimationActive @isAnimationActive?}
                (doall
                  (map-indexed (fn [idx {name :name}]
                                   [:> Cell {:key (str "cell-" idx) :fill (get-in @config [:colors name])}])
                               @data))
                [:> LabelList {:position :right :fill "#000000" :stroke "none" :dataKey :name}]]])))