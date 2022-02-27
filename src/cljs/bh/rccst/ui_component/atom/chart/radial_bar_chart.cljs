(ns bh.rccst.ui-component.atom.chart.radial-bar-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            ["recharts" :refer [RadialBarChart RadialBar Legend Tooltip]]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(def sample-data (r/atom [{:name "18-24", :uv 31.47, :pv 2400, :fill "#8884d8"}
                          {:name "25-29", :uv 26.69, :pv 4567, :fill "#83a6ed"}
                          {:name "30-34", :uv -15.69, :pv 1398, :fill "#8dd1e1"}
                          {:name "35-39", :uv 8.22, :pv 9800, :fill "#82ca9d"}
                          {:name "40-49", :uv -8.63, :pv 3908, :fill "#a4de6c"}
                          {:name "50+", :uv -2.63, :pv 4800, :fill "#d0ed57"}
                          {:name "unknow", :uv 6.67, :pv 4800, :fill "#ffc658"}]))


(def source-code "dummy Radial Bar Chart Code")


(defn config
  "constructs the configuration data structure for the widget. This is specific to this being a radar-chart component.

  ---

  - widget-id : (string) id of the widget, in this specific case
  "
  [component-id data]
  (-> ui-utils/default-pub-sub
    (merge
      utils/default-config
      {:tab-panel {:value     (keyword component-id "config")
                   :data-path [:widgets (keyword component-id) :tab-panel]}
       :radial-uv {:include    true :minAngle 15
                   :label      {:fill "#666", :position "insideStart"}
                   :background {:clockWise true}
                   :dataKey    :uv}})))


(defn- radial-config [widget-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config widget-id label (conj path :include)]]])


(defn config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.\n  - config : (atom) holds all the configuration settings made by the user
  "
  [data component-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-components component-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[radial-config component-id "uv" [:radial-uv] :above-right]]]]])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - widget-id : (string) unique identifier for this specific widget
  "
  [data component-id]
  (let [container (ui-utils/subscribe-local component-id [:container])
        radial-uv? (ui-utils/subscribe-local component-id [:radial-uv :include])]

    (fn []
      [:> RadialBarChart {:width       400
                          :height      400
                          :innerRadius "10%"
                          :outerRadius "80%"
                          :data        @data
                          :startAngle  180
                          :endAngle    0}

       ;(utils/non-gridded-chart-components widget-id)

       (when @radial-uv? [:> RadialBar {:minAngle   15
                                        :label      {:fill "#666", :position "insideStart"}
                                        :background {:clockWise true}
                                        :dataKey    :uv}])
       [:> Legend {:iconSize 10 :width 120 :height 140 :layout "vertical" :verticalAlign "middle" :align "right"}]
       [:> Tooltip]])))


(defn configurable-component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - :component-id : (string) name of this chart\n  - container-id : (string) name of the container this chart is inside of
  "
  ([& {:keys [data component-id container-id]}]
   [c/base-chart
    :data data
    :config (config component-id data)
    :component-id component-id
    :container-id (or container-id "")
    :data-panel utils/tabular-data-panel
    :config-panel config-panel
    :component-panel component-panel]))


(defn component
  "the chart to draw. this variant does NOT provide a configuration panel

  ---

  - :data : (atom) any data shown by the component's ui
  - :component-id : (string) name of this chart
  - :container-id : (string) name of the container this chart is inside of
  "
  ([& {:keys [data component-id container-id]}]
   [c/base-chart
    :data data
    :config (config component-id data)
    :component-id component-id
    :container-id (or container-id "")
    :component-panel component-panel]))


(def meta-data {:component component
                :configurable-component configurable-component
                :sources {:data :source-type/meta-tabular}
                :pubs []
                :subs []})

