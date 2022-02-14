(ns bh.rccst.ui-component.atom.funnel-chart
  (:require ["recharts" :refer [FunnelChart Funnel Cell LabelList XAxis YAxis CartesianGrid Tooltip Brush]]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.buttons :as buttons]
            [woolybear.ad.icons :as icons]))


(def sample-data
  "the Funnel Chart works best with \"paired data\" so we return the paired-data from utils"
  (r/atom utils/paired-data))


(defn config
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a line-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. funnel charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - chart-id : (string) unique id of the chart
  "
  [chart-id]
  (merge utils/default-config
    {:tab-panel {:value     (keyword chart-id "config")
                 :data-path [:widgets (keyword chart-id) :tab-panel]}
     :colors    (zipmap (map :name utils/paired-data)
                  ["#8884d8" "#83a6ed" "#8dd1e1"
                   "#82ca9d" "#a4de6c" "#d7e62b"])}))


(defn- color-anchors
  "build the config ui-components needed for each of the funnel slices
  "
  [chart-id]
  [:<>
   (doall
     (map (fn [[id _]]
            ^{:key id} [utils/color-config-text chart-id id [:colors id] :right-above])
       @(ui-utils/subscribe-local chart-id [:colors])))])


(defn config-panel
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a funnel-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. funnel charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - _ : (unused) this is where the container will pass in the data, unused by the funner chart's config
  - chart-id : (string) unique id of the chart
  "

  [_ chart-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config chart-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/label :src (rc/at) :label "Funnel Colors"]
                          (color-anchors chart-id)]]]])


(defn component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - chart-id : (string) unique identifier for this chart instance
  "
  [data chart-id]
  (let [colors (ui-utils/subscribe-local chart-id [:colors])
        isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])]

    (fn []
      ;(log/info "configurable-funnel-chart" @config)
      [c/chart
       [:> FunnelChart {:height 400 :width 500}
        (utils/non-gridded-chart-components chart-id)
        [:> Funnel {:dataKey           :value
                    :nameKey           "name"
                    :label             true
                    :data              @data
                    :isAnimationActive @isAnimationActive?}
         (doall
           (map-indexed
             (fn [idx {name :name}]
               ^{:key (str idx name)}
               [:> Cell {:key  (str "cell-" idx)
                         :fill (get @colors name)}])
             @data))
         [:> LabelList {:position :right :fill "#000000" :stroke "none" :dataKey :name}]]]])))


