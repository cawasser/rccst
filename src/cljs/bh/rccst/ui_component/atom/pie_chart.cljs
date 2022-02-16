(ns bh.rccst.ui-component.atom.pie-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]

            ["recharts" :refer [PieChart Pie]]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


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
  [chart-id]
  (-> ui-utils/default-pub-sub
    (merge
      utils/default-config
      {:tab-panel {:value     (keyword chart-id "config")
                   :data-path [:widgets (keyword chart-id) :tab-panel]}
       :fill      "#8884d8"})))


(defn- config-panel
  "the panel of configuration controls

  ---

  - _ (ignored)
  - chart-id : (string) unique identifier for this specific widget instance

  "
  [_ chart-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "500px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config chart-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[utils/color-config-text chart-id ":fill" [:fill] :above-right]]]]])


(def source-code '[:> PieChart {:width 400 :height 400}
                   [:> Tooltip]
                   [:> Legend]
                   [:> Pie {:dataKey "value" :data @data :fill "#8884d8"}]])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - widget-id : (string) unique identifier for this specific widget instance
  "
  [data chart-id]
  (let [isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])
        fill (ui-utils/subscribe-local chart-id [:fill])]

    (fn []
       [:> PieChart {:width 400 :height 400 :label true}

        (utils/non-gridded-chart-components chart-id)

        [:> Pie {:dataKey           "value"
                 :name-Key          "name"
                 :data              @data
                 :fill              @fill
                 :label             true
                 :isAnimationActive @isAnimationActive?}]])))

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

         (fn [] (when (nil? @id)
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

