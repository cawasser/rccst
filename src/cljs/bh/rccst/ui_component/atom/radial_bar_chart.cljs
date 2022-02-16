(ns bh.rccst.ui-component.atom.radial-bar-chart
  (:require [taoensso.timbre :as log]
            ["recharts" :refer [RadialBarChart RadialBar Legend Tooltip]]
            [re-com.core :as rc]
            [reagent.core :as r]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]))

(def sample-data (r/atom [{:name "18-24",  :uv 31.47, :pv 2400, :fill "#8884d8"}
                          {:name "25-29",  :uv 26.69, :pv 4567, :fill "#83a6ed"}
                          {:name "30-34",  :uv -15.69, :pv 1398, :fill "#8dd1e1"}
                          {:name "35-39",  :uv 8.22, :pv 9800, :fill "#82ca9d"}
                          {:name "40-49",  :uv -8.63, :pv 3908, :fill "#a4de6c"}
                          {:name "50+",    :uv -2.63, :pv 4800, :fill "#d0ed57"}
                          {:name "unknow", :uv 6.67, :pv 4800, :fill "#ffc658"}]))


(def source-code "dummy Radial Bar Chart Code")

(defn config
      "constructs the configuration data structure for the widget. This is specific to this being a radar-chart component.

      ---

      - widget-id : (string) id of the widget, in this specific case
      "
      [widget-id]
      (-> ui-utils/default-pub-sub
          (merge
            utils/default-config
            {:tab-panel {:value     (keyword widget-id "config")
                         :data-path [:widgets (keyword widget-id) :tab-panel]}
             :radial-uv   {:include true :minAngle 15
                           :label {:fill "#666", :position "insideStart"}
                           :background {:clockWise true}
                           :dataKey :uv}})))

(defn- radial-config [widget-id label path position]
       [rc/v-box :src (rc/at)
        :gap "5px"
        :children [[utils/boolean-config widget-id label (conj path :include)]]])

(defn config-panel
      "the panel of configuration controls

      ---

      - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.\n  - config : (atom) holds all the configuration settings made by the user
      "
      [data widget-id]

      [rc/v-box :src (rc/at)
       :gap "10px"
       :width "100%"
       :style {:padding          "15px"
               :border-top       "1px solid #DDD"
               :background-color "#f7f7f7"}
       :children [[utils/non-gridded-chart-components widget-id]
                  [rc/line :src (rc/at) :size "2px"]
                  [rc/h-box :src (rc/at)
                   :gap "10px"
                   :children [[radial-config widget-id "uv" [:radial-uv] :above-right]]]]])

(defn- component-panel
       "the chart to draw, taking cues from the settings of the configuration panel

       ---

       - data : (atom) any data used by the component's ui
       - widget-id : (string) unique identifier for this specific widget
       "
       [data widget-id]
       (let [container (ui-utils/subscribe-local widget-id [:container])
             radial-uv? (ui-utils/subscribe-local widget-id [:radial-uv :include])]




            (fn []
                [:> RadialBarChart {:width 400
                                    :height 400
                                    :innerRadius "10%"
                                    :outerRadius "80%"
                                    :data @data
                                    :startAngle 180
                                    :endAngle 0}

                 ;(utils/non-gridded-chart-components widget-id)


                 (when @radial-uv? [:> RadialBar {:minAngle 15
                                                  :label {:fill "#666", :position "insideStart"}
                                                  :background {:clockWise true}
                                                  :dataKey :uv}])
                 [:> Legend {:iconSize 10 :width 120 :height 140 :layout "vertical" :verticalAlign "middle" :align "right"}]
                 [:> Tooltip]])))

(defn component
      "the chart to draw, taking cues from the settings of the configuration panel

      the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
      can be placed inside the same outer container/composite

      ---

      - data : (atom) any data shown by the component's ui
      - container-id : (string) name of the container this chart is inside of
      "
      ([data component-id]
       [component data component-id ""])


      ([data component-id container-id]

       (let [id (r/atom nil)]

            (fn []
                (when (nil? @id)
                      (reset! id component-id)
                      (ui-utils/init-widget @id (config @id))
                      (ui-utils/dispatch-local @id [:container] container-id))

                ;(log/info "component" @id)

                [c/configurable-chart
                 :data data
                 :id @id
                 :data-panel utils/tabular-data-panel
                 :config-panel config-panel
                 :component component-panel]))))



