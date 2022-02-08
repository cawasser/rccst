(ns bh.rccst.ui-component.atom.scatter-chart
  (:require ["recharts" :refer [ScatterChart Scatter XAxis YAxis CartesianGrid Legend Tooltip Brush]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.views.catalog.example.chart.utils :as utils]
            [re-com.core :as rc]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.icons :as icons]
            [taoensso.timbre :as log]))


; region ; configuration params

(defn config [widget-id]
       (-> utils/default-config
            (merge {:tab-panel {:value     (keyword widget-id "config")
                               :data-path [:widgets (keyword widget-id) :tab-panel]}
                    :brush     false})
                (assoc-in [:x-axis :dataKey] :x)
                (assoc-in [:y-axis :dataKey] :y)
                (assoc-in [:fill :color] "#8884d8")))

;; endregion

;; region ; config and component panels

(defn config-panel
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
        :children [[utils/standard-chart-config data widget-id]
                   [utils/color-config-text widget-id ":fill" [:fill :color] :above-right]]])


(defn component
       "the chart to draw, taking cues from the settings of the configuration panel

       ---

       - data : (atom) any data shown by the component's ui
       - config : (atom) configuration settings made by the user using the config-panel, see [[config]].
       "
       [data widget-id]
       (let [scatter-dot-fill (ui-utils/subscribe-local widget-id [:fill :color])
             isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
             brush? (ui-utils/subscribe-local widget-id [:brush])]

            (fn []
                ;(log/info "configurable-Scatter-chart" @config @data)

                [rc/v-box :src (rc/at)
                 :gap "2px"
                 :children [[buttons/button
                             {:on-click #(log/info "open config panel")}
                             [icons/icon {:icon "edit"} "Edit"]]
                              [:> ScatterChart {:width 400 :height 400}

                               (utils/standard-chart-components widget-id)

                               (when @brush? [:> Brush])

                               [:> Scatter {:name "tempScatter" :data @data
                                            :isAnimationActive @isAnimationActive?
                                            :fill @scatter-dot-fill}]]]])))

;; endregion