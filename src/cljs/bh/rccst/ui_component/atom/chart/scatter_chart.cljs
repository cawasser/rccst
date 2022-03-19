(ns bh.rccst.ui-component.atom.chart.scatter-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.utils.example-data :as data]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            ["recharts" :refer [ResponsiveContainer ScatterChart Scatter Brush]]))


(def sample-data
  "the Scatter Chart works best with \"triplet data\" so we return the triplet-data from utils"
  (r/atom data/triplet-data))


(defn config
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a line-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. line charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - chart-id : (string) unique id of the chart
  "
  [component-id data]
  (->
    ui-utils/default-pub-sub
    (merge
      utils/default-config
      {:tab-panel {:value     (keyword component-id "config")
                   :data-path [:widgets (keyword component-id) :tab-panel]}
       :brush     false})
    (assoc-in [:x-axis :dataKey] :x)
    (assoc-in [:y-axis :dataKey] :y)
    (assoc-in [:fill :color] "#8884d8")))


(defn config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.
  - config : (atom) holds all the configuration settings made by the user
  "
  [data component-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data component-id]
              [rc/line :size "2px"]
              [utils/boolean-config component-id ":brush" [:brush]]
              [rc/line :size "2px"]
              [utils/color-config-text component-id ":fill" [:fill :color] :above-right]]])


(def source-code `[:> ScatterChart {:width 400 :height 400}])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - widget-id : (string) unique identifier for this specific widget instance
  "
  [data component-id container-id ui]
  (let [scatter-dot-fill   (ui-utils/subscribe-local component-id [:fill :color])
        x-axis?            (ui-utils/subscribe-local component-id [:x-axis :include])
        x-axis-dataKey     (ui-utils/subscribe-local component-id [:x-axis :dataKey])
        y-axis?            (ui-utils/subscribe-local component-id [:y-axis :include])
        y-axis-dataKey     (ui-utils/subscribe-local component-id [:y-axis :dataKey])
        isAnimationActive? (ui-utils/subscribe-local component-id [:isAnimationActive])
        brush?             (ui-utils/subscribe-local component-id [:brush])]

    (fn [data component-id container-id ui]
      ;(log/info "configurable-Scatter-chart" @config @data)

      [:> ResponsiveContainer
       [:> ScatterChart

        (utils/standard-chart-components component-id ui)

        (when @brush? [:> Brush])

        ;; TODO: looks like we need to add more attributes to x- & y-axis
        ;;
        ;(when @x-axis? [:> XAxis {:type "number" :dataKey @x-axis-dataKey :name "stature" :unit "cm"}])
        ;(when @y-axis? [:> YAxis {:type "number" :dataKey @y-axis-dataKey :name "weight" :unit "kg"}])

        [:> Scatter {:name              "tempScatter" :data @data
                     :isAnimationActive @isAnimationActive?
                     :fill              @scatter-dot-fill}]]])))


(defn configurable-component
  "the chart to draw, taking cues from the settings of the configuration panel

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - :data : (atom) any data shown by the component's ui
  - :component -id : (string) name of this chart
  - :container-id : (string) name of the container this chart is inside of
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
  - :component -id : (string) name of this chart
  - :container-id : (string) name of the container this chart is inside of
  "
  ([& {:keys [data component-id container-id]}]
   [c/base-chart
    :data data
    :config (config component-id data)
    :component-id component-id
    :container-id (or container-id "")
    :component-panel component-panel]))


(def meta-data {:component              component
                :configurable-component configurable-component
                :sources                {:data :source-type/meta-tabular}
                :pubs                   []
                :subs                   []})

