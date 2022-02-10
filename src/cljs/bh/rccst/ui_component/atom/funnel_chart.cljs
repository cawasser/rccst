(ns bh.rccst.ui-component.atom.funnel-chart
  (:require ["recharts" :refer [FunnelChart Funnel Cell LabelList XAxis YAxis CartesianGrid Tooltip Brush]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [re-com.core :as rc]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.icons :as icons]
            [taoensso.timbre :as log]))



; region ; configuration params

(defn config [widget-id]
        (merge utils/default-config
               {:tab-panel {:value     (keyword widget-id "config")
                            :data-path [:widgets (keyword widget-id) :tab-panel]}
                :colors (zipmap (map :name utils/paired-data)
                                ["#8884d8" "#83a6ed" "#8dd1e1"
                                 "#82ca9d" "#a4de6c" "#d7e62b"])}))
;(assoc-in [:x-axis :dataKey] :x)
;(assoc-in [:y-axis :dataKey] :y)
;(assoc-in [:fill :color] "#8884d8"))))

(defn- color-anchors [widget-id]
       [:<>
        (doall
          (map (fn [[id _]]
                   ^{:key id}[utils/color-config-text widget-id id [:colors id] :right-above])
               @(ui-utils/subscribe-local widget-id [:colors])))])

;; endregion

;; region ; config and component panels

(defn config-panel
       "the panel of configuration controls

       ---

       - config : (atom) holds all the configuration settings made by the user
       "
       [_ widget-id]

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
                               (color-anchors widget-id)]]]])


(defn component
       "the chart to draw, taking cues from the settings of the configuration panel

       ---

       - data : (atom) any data shown by the component's ui
       - config : (atom) configuration settings made by the user using the config-panel, see [[config]].
       "
       [data widget-id]
       (let [colors (ui-utils/subscribe-local widget-id [:colors])
             isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])]

            (fn []
                ;(log/info "configurable-funnel-chart" @config)
                [c/chart
                  [:> FunnelChart {:height 400 :width 500}
                   (utils/non-gridded-chart-components widget-id)
                   [:> Funnel {:dataKey :value
                               :nameKey "name"
                               :label true
                               :data @data
                               :isAnimationActive @isAnimationActive?}
                    (doall
                      (map-indexed
                        (fn [idx {name :name}]
                            ^{:key (str idx name)}
                            [:> Cell {:key (str "cell-" idx)
                                      :fill (get @colors name)}])
                        @data))
                    [:> LabelList {:position :right :fill "#000000" :stroke "none" :dataKey :name}]]]])))

;; endregion

