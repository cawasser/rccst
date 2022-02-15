(ns bh.rccst.ui-component.atom.radial-bar-chart
  (:require [taoensso.timbre :as log]
            ["recharts" :refer [BarChart Bar Brush]]
            [re-com.core :as rc]
            [reagent.core :as r]

            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]))


(def sample-data
  "the Bar Chart works best with \"tabular data\" so we return the tabular-data from utils,
  and we mix-in a fourth column just to show how it can be done"
  (r/atom (mapv (fn [d] (assoc d :d (rand-int 5000))) utils/tabular-data)))


(defn config
      "constructs the configuration panel for the chart's configurable properties. This is specific to
      this being a bar-chart component (see [[local-config]]).

      Merges together the configuration needed for:

      1. bar charts
      2. pub/sub between components of a container
      3. `default-config` for all Rechart-based types
      4. the `tab-panel` for view/edit configuration properties and data
      5. sets properties of the default-config (local config properties are just set inside [[local-config]])
      6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

      ---

      - chart-id : (string) unique id of the chart
      "
      [widget-id]
      (-> ui-utils/default-pub-sub
          (merge
            utils/default-config
            {:tab-panel {:value     (keyword widget-id "config")
                         :data-path [:widgets (keyword widget-id) :tab-panel]}
             :brush     false
             :bar-uv    {:include true :fill "#ff0000" :stackId ""}
             :bar-pv    {:include true :fill "#00ff00" :stackId ""}
             :bar-amt   {:include false :fill "#0000ff" :stackId ""}
             :bar-d     {:include false :fill "#0f0f0f" :stackId ""}})
          (assoc-in [:x-axis :dataKey] :name)))


(defn- bar-config [widget-id label path position]
       [rc/v-box :src (rc/at)
        :gap "5px"
        :children [[utils/boolean-config widget-id label (conj path :include)]
                   [utils/color-config widget-id ":fill" (conj path :fill) position]
                   [utils/text-config widget-id ":stackId" (conj path :stackId)]]])


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
       :children [[utils/standard-chart-config data widget-id]
                  [rc/line :src (rc/at) :size "2px"]
                  [rc/h-box :src (rc/at)
                   :gap "10px"
                   :children [[bar-config widget-id "bar (uv)" [:bar-uv] :above-right]
                              [bar-config widget-id "bar (pv)" [:bar-pv] :above-center]
                              [bar-config widget-id "bar (amt)" [:bar-amt] :above-center]
                              [bar-config widget-id "bar (d)" [:bar-d] :above-left]]]
                  [rc/line :src (rc/at) :size "2px"]
                  [utils/boolean-config widget-id ":brush?" [:brush]]]])


(def source-code "dummy Bar Chart Code")


(defn- component-panel
       "the chart to draw, taking cues from the settings of the configuration panel

       ---

       - data : (atom) any data used by the component's ui
       - widget-id : (string) unique identifier for this specific widget
       "
       [data widget-id]
       (let [container (ui-utils/subscribe-local widget-id [:container])

             bar-uv? (ui-utils/subscribe-local widget-id [:bar-uv :include])
             bar-uv-fill (ui-utils/subscribe-local widget-id [:bar-uv :fill])
             bar-uv-stackId (ui-utils/subscribe-local widget-id [:bar-uv :stackId])
             bar-pv? (ui-utils/subscribe-local widget-id [:bar-pv :include])
             bar-pv-fill (ui-utils/subscribe-local widget-id [:bar-pv :fill])
             bar-pv-stackId (ui-utils/subscribe-local widget-id [:bar-pv :stackId])
             bar-amt? (ui-utils/subscribe-local widget-id [:bar-amt :include])
             bar-amt-fill (ui-utils/subscribe-local widget-id [:bar-amt :fill])
             bar-amt-stackId (ui-utils/subscribe-local widget-id [:bar-amt :stackId])
             bar-d? (ui-utils/subscribe-local widget-id [:bar-d :include])
             bar-d-fill (ui-utils/subscribe-local widget-id [:bar-d :fill])
             bar-d-stackId (ui-utils/subscribe-local widget-id [:bar-d :stackId])
             isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
             brush? (ui-utils/subscribe-local widget-id [:brush])]

            (fn []
                [:> BarChart {:width 400 :height 400 :data @data}

                 (utils/standard-chart-components widget-id)

                 (when @brush? [:> Brush])

                 (when @bar-uv? [:> Bar (merge {:type              "monotone" :dataKey :uv
                                                :isAnimationActive @isAnimationActive?
                                                :fill              @bar-uv-fill}
                                               (when (seq @bar-uv-stackId) {:stackId @bar-uv-stackId}))])

                 (when @bar-pv? [:> Bar (merge {:type              "monotone" :dataKey :pv
                                                :isAnimationActive @isAnimationActive?
                                                :fill              @bar-pv-fill}
                                               (when (seq @bar-pv-stackId) {:stackId @bar-pv-stackId}))])

                 (when @bar-amt? [:> Bar (merge {:type              "monotone" :dataKey :amt
                                                 :isAnimationActive @isAnimationActive?
                                                 :fill              @bar-amt-fill}
                                                (when (seq @bar-amt-stackId) {:stackId @bar-amt-stackId}))])

                 (when @bar-d? [:> Bar (merge {:type              "monotone" :dataKey :d
                                               :isAnimationActive @isAnimationActive?
                                               :fill              @bar-d-fill}
                                              (when (seq @bar-d-stackId) {:stackId @bar-d-stackId}))])])))


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
                 :config-panel config-panel
                 :component component-panel]))))
