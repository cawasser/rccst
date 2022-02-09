(ns bh.rccst.ui-component.atom.colored-pie-chart
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]

            ["recharts" :refer [PieChart Pie Cell]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]))


(defn config [widget-id]
  (merge utils/default-config
    {:tab-panel {:value     (keyword widget-id "config")
                 :data-path [:widgets (keyword widget-id) :tab-panel]}
     :colors (zipmap (map :name utils/paired-data)
               ["#ffff00" "#ff0000" "#00ff00"
                "#0000ff" "#009999" "#ff00ff"])}))


(defn- color-anchors [widget-id]
  [:<>
   (doall
     (map (fn [[id _]]
            ^{:key id}[utils/color-config-text widget-id id [:colors id] :right-above])
       @(ui-utils/subscribe-local widget-id [:colors])))])


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
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config widget-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/label :src (rc/at) :label "Pie Colors"]
                          (color-anchors widget-id)]]]])


(def source-code '[:> PieChart {:width 400 :height 400}
                   [:> Tooltip]
                   [:> Legend]
                   [:> Pie {:dataKey "value" :data @data}]])


(defn component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - widget-id : (string) unique identifier for this specific widget instance
  "
  [data widget-id]
  (let [isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
        colors (ui-utils/subscribe-local widget-id [:colors])]

    (fn []
      [c/chart
       [:> PieChart {:width 400 :height 400 :label true}

        (utils/non-gridded-chart-components widget-id)

        [:> Pie {:dataKey "value"
                 :nameKey "name"
                 :data @data
                 :label true
                 :isAnimationActive @isAnimationActive?}
         (doall
           (map-indexed
             (fn [idx {name :name}]
               ^{:key (str idx name)}
               [:> Cell {:key (str "cell-" idx)
                         :fill (get @colors name)}])
             @data))]]])))

