(ns bh.rccst.ui-component.atom.pie-chart
  (:require ["recharts" :refer [PieChart Pie]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.views.catalog.example.chart.utils :as utils]
            [re-com.core :as rc]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.icons :as icons]

            [taoensso.timbre :as log]))


(defn config [widget-id]
  (merge utils/default-config
    {:tab-panel {:value     (keyword widget-id "config")
                 :data-path [:widgets (keyword widget-id) :tab-panel]}
     :fill "#8884d8"}))


(defn config-panel
  "the panel of configuration controls

  ---

  - _ (ignored)
  - widget-id : (string) unique identifier for this specific widget instance

  "
  [_ widget-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "500px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config widget-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[utils/color-config-text widget-id ":fill" [:fill] :above-right]]]]])


(def source-code '[:> PieChart {:width 400 :height 400}
                   [:> Tooltip]
                   [:> Legend]
                   [:> Pie {:dataKey "value" :data @data :fill "#8884d8"}]])


(defn component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - widget-id : (string) unique identifier for this specific widget instance
  "
  [data widget-id]
  (let [isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
        fill (ui-utils/subscribe-local widget-id [:fill])]

    (fn []
      [rc/v-box :src (rc/at)
       :gap "2px"
       :children [[buttons/button
                   {:on-click #(log/info "open config panel")}
                   [icons/icon {:icon "edit"} "Edit"]]
                  [:> PieChart {:width 400 :height 400 :label true}

                   (utils/non-gridded-chart-components widget-id)

                   [:> Pie {:dataKey "value"
                            :name-Key "name"
                            :data @data
                            :fill @fill
                            :label true
                            :isAnimationActive @isAnimationActive?}]]]])))


(comment
  (def widget-id "pie-chart-demo")

  ())