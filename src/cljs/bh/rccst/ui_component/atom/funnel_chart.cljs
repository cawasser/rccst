(ns bh.rccst.ui-component.atom.funnel-chart
  (:require ["recharts" :refer [FunnelChart Funnel Cell LabelList XAxis YAxis CartesianGrid Tooltip Brush]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.views.catalog.example.chart.utils :as utils]
            [re-com.core :as rc]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.icons :as icons]
            [taoensso.timbre :as log]))



; region ; configuration params

(defn config [widget-id]
      (-> utils/default-config
        (merge {:legend            {:include       false}
                :brush     false
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
       [data widget-id]

       [rc/v-box :src (rc/at)
        :gap "10px"
        :width "100%"
        :style {:padding          "15px"
                :border-top       "1px solid #DDD"
                :background-color "#f7f7f7"}
        :children [[utils/non-gridded-chart-config widget-id]
                   [rc/line :src (rc/at) :size "2px"]
                   [rc/v-box :src (rc/at)
                    :gap "5px"
                    :children [[rc/label :src (rc/at) :label "Funnel Colors"]
                               (color-anchors (config widget-id))]]]])


(defn- component
       "the chart to draw, taking cues from the settings of the configuration panel

       ---

       - data : (atom) any data shown by the component's ui
       - config : (atom) configuration settings made by the user using the config-panel, see [[config]].
       "
       [data widget-id]
       (let [tooltip? (ui-utils/subscribe-local widget-id [:tooltip])
             funnel-fill (ui-utils/subscribe-local widget-id [:colors :name])
             isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
             brush? (ui-utils/subscribe-local widget-id [:brush])]

            (fn []
                ;(log/info "configurable-funnel-chart" @config)

                [:> FunnelChart {:height 400 :width 500}
                 (utils/non-gridded-chart-components widget-id)
                 (when @brush? [:> Brush])
                 [:> Funnel {:dataKey :value :data @data :isAnimationActive @isAnimationActive?}
                  (doall
                    (map-indexed (fn [idx {name :name}]
                                     [:> Cell {:key (str "cell-" idx) :fill @funnel-fill}])
                                 @data))
                  [:> LabelList {:position :right :fill "#000000" :stroke "none" :dataKey :name}]]])))

;; endregion

