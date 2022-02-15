(ns bh.rccst.ui-component.atom.colored-pie-chart
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]
            [reagent.core :as r]

            ["recharts" :refer [PieChart Pie Cell]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]))


(def sample-data
  "the Pie Chart works best with \"paired data\" so we return the paired-data from utils"
  (r/atom utils/paired-data))


(defn config
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a pie-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. pie charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - chart-id : (string) unique id of the chart
  "
  [widget-id]
  (merge
    ui-utils/default-pub-sub
    utils/default-config
    {:tab-panel {:value     (keyword widget-id "config")
                 :data-path [:widgets (keyword widget-id) :tab-panel]}
     :colors (zipmap (map :name utils/paired-data)
               ["#ffff00" "#ff0000" "#00ff00"
                "#0000ff" "#009999" "#ff00ff"])}))


(defn- color-anchors
  "build the config ui-components needed for each of the pie slices
  "
  [widget-id]
  [:<>
   (doall
     (map (fn [[id _]]
            ^{:key id}[utils/color-config-text widget-id id [:colors id] :right-above])
       @(ui-utils/subscribe-local widget-id [:colors])))])


(defn- config-panel
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a colored-pie-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. colored pie charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - chart-id : (string) unique id of the chart
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


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - widget-id : (string) unique identifier for this specific widget instance
  "
  [data widget-id]
  (let [isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
        colors (ui-utils/subscribe-local widget-id [:colors])]

    (fn []
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
            @data))]])))


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
