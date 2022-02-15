(ns bh.rccst.ui-component.atom.treemap-chart
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]
            [reagent.core :as r]

            ["recharts" :refer [Treemap]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]))


(def sample-data
  "the Treemap Chart works best with \"hierarchical data\" so we return the hierarchy-data from utils"
  (r/atom utils/hierarchy-data))


(def default-ratio (/ 4 3))
(def default-stroke "#ffffff")
(def default-fill "#8884d8")

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
  [chart-id]
  (merge
    ui-utils/default-pub-sub
    {:tab-panel {:value     (keyword chart-id "config")
                 :data-path [:widgets (keyword chart-id) :tab-panel]}
     :isAnimationActive true
     :ratio  {:include true
              :n 4
              :d 3}
     :stroke  {:color "#ffffff"}
     :fill {:color "#8884d8"}}))


(defn- ratio-config
  "builds the ui components needed to configure the `ration` property of the treemap

> See also:
>
> [Recharts/treemap](https://recharts.org/en-US/api/Treemap)
"
  [chart-id]
  [rc/h-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config chart-id ":ratio" [:ratio :include]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[utils/slider-config chart-id 1 10 [:ratio :n]]
                          [utils/slider-config chart-id 1 10 [:ratio :d]]]]]])


(defn- config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.
  - chart-id : (string) unique identifier for this chart instance
  "
  [_ chart-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "400px"
   :height "500px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/isAnimationActive chart-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/v-box :src (rc/at)
               :gap "10px"
               :children [[ratio-config chart-id]
                          [utils/color-config-text chart-id ":stroke" [:stroke :color]]
                          [utils/color-config-text chart-id ":fill" [:fill :color]]]]]])


(def source-code `[:> Treemap
                   {:width 400 :height 400
                    :data @data
                    :dataKey "size"
                    :isAnimationActive true
                    :ratio (/ 4 3)
                    :stroke "#ffffff"
                    :fill "#8884d8"}])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - widget-id : (string) unique identifier for this widget instance
  "
  [data chart-id]
  (let [;ratio (ui-utils/subscribe-local widget-id [:ratio :include])
        isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])
        stroke (ui-utils/subscribe-local chart-id [:stroke :color])
        fill (ui-utils/subscribe-local chart-id [:fill :color])]

    (fn []
      [:> Treemap
       {:width 400 :height 400
        :data @data
        :dataKey "size"
        :isAnimationActive @isAnimationActive?
        :ratio default-ratio
        :stroke @stroke
        :fill @fill}])))


(defn component
  "the chart to draw, taking cues from the settings of the configuration panel

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - data : (atom) any data shown by the component's ui
  - chart-id : (string) unique identifier for this chart instance within this container
  - container-id : (string) name of the container this chart is inside of
  "
  ([data chart-id]
   [component data chart-id ""])


  ([data chart-id container-id]

   (let [id (r/atom nil)]

     (fn []
       (when (nil? @id)
         (reset! id chart-id)
         (ui-utils/init-widget @id (config @id))
         (ui-utils/dispatch-local @id [:container] container-id))

       ;(log/info "component" @id)

       [c/configurable-chart
        :data data
        :id @id
        :data-panel utils/hierarchy-data-panel
        :config-panel config-panel
        :component component-panel]))))


(comment
  (def chart-id "treemap-chart-demo")

  ())