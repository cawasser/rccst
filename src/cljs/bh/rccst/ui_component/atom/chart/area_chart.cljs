(ns bh.rccst.ui-component.atom.chart.area-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            ["recharts" :refer [AreaChart Area Brush]]

            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(def sample-data
  "the Area Chart works best with \"tabular data\" so we return the tabular-data from utils,
  and we mix-in a fourth column just to show how it can be done"
  (let [source utils/meta-tabular-data
        data (get source :data)
        fields (get-in source [:metadata :fields])]
    (-> source
      (assoc
        :data
        (mapv (fn [d] (assoc d :d (rand-int 5000))) data))
      (assoc-in
        [:metadata :fields]
        (assoc fields :d :number))
      r/atom)))


(defn local-config
  "provides both the definition and the initial default values for various properties that
  allow user to customize the visualization of the chart.

  ---

   - data : (atom) atom containing the data and metadata for this chart

> See Also:
>
> [Recharts/line-chart](https://recharts.org/en-US/api/LineChart)
> [tabular-data]()
  "
  [data]
  (merge
    {:brush false}
    (->> (get-in @data [:metadata :fields])
      (filter (fn [[k v]] (= :number v)))
      keys
      (map-indexed (fn [idx a]
                     {a {:include true
                         :fill    (ui-utils/get-color idx)
                         :stroke    (ui-utils/get-color idx)
                         :stackId ""}}))
      (into {}))))


(defn config
  "constructs the configuration panel for the chart's configurable properties. This is specific to
  this being a line-chart component (see [[local-config]]).

  Merges together the configuration needed for:

  1. area charts
  2. pub/sub between components of a container
  3. `default-config` for all Rechart-based types
  4. the `tab-panel` for view/edit configuration properties and data
  5. sets properties of the default-config (local config properties are just set inside [[local-config]])
  6. sets meta-data for properties this component publishes (`:pub`) or subscribes (`:sub`)

  ---

  - chart-id : (string) unique id of the chart
  - data : (atom) metadata wrapped data  to display
  "
  [chart-id data]
  (-> ui-utils/default-pub-sub
    (merge
      utils/default-config
      {:tab-panel {:value     (keyword chart-id "config")
                   :data-path [:widgets (keyword chart-id) :tab-panel]}}
      (local-config data))
    (assoc-in [:x-axis :dataKey] (get-in @data [:metadata :id]))))


(defn- area-config
  "construct ui-component(s) for setting the properties of an 'area', specifically:

  - `:fill`
  - `:stroke`
  - `:stackId`

  ---

  - chart-id : (string) unique identifier for his chart instance
  - label : (string) label to use for this `area`
  - path : (vector of keywords) path to the data inside the config data structure
  - position : (keyword) position to place the re-com/popover that shows the `color-picker`

> See also:
>
> [Re-com/popover](https://re-com.day8.com.au/#/popovers)
  "
  [chart-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config chart-id label (conj path :include)]
              [utils/color-config chart-id ":fill" (conj path :fill) :right-above]
              [utils/color-config chart-id ":stroke" (conj path :stroke) :right-above]
              [utils/text-config chart-id ":stackId" (conj path :stackId)]]])


(defn- make-area-config [chart-id data]
  (->> (get-in @data [:metadata :fields])
    (filter (fn [[k v]] (= :number v)))
    keys
    (map-indexed (fn [idx a]
                   [area-config chart-id a [a] :right-center]))
    (into [])))


(defn- config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.\n  - config : (atom) holds all the configuration settings made by the user
  "
  [data chart-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data chart-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :width "400px"
               :style ui-utils/h-wrap
               :children (make-area-config chart-id data)]
              [rc/line :src (rc/at) :size "2px"]
              [utils/boolean-config chart-id ":brush?" [:brush]]]])


(def source-code "dummy area Chart Code")


(defn- make-area-display [chart-id data subscriptions isAnimationActive?]
  (->> (get-in @data [:metadata :fields])
    (filter (fn [[_ v]] (= :number v)))
    keys
    (map (fn [a]
           (if (ui-utils/resolve-sub subscriptions [a :include])
             [:> Area (merge {:type              "monotone" :dataKey a
                              :isAnimationActive @isAnimationActive?
                              :stroke            (ui-utils/resolve-sub subscriptions [a :stroke])
                              :fill              (ui-utils/resolve-sub subscriptions [a :fill])}
                        (when (seq (ui-utils/resolve-sub subscriptions [a :stackId]))
                          {:stackId (ui-utils/resolve-sub subscriptions [a :stackId])}))]
             [])))
    (remove empty?)
    (into [:<>])))


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - widget-id : (string) unique identifier for this specific widget
  "
  [data chart-id]
  (let [container (ui-utils/subscribe-local chart-id [:container])
        isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])
        subscriptions (ui-utils/build-subs chart-id (local-config data))]

    (fn []
      ;[c/chart
      [:> AreaChart {:width 400 :height 400 :data (get @data :data)}

       (utils/standard-chart-components chart-id)

       (when (ui-utils/resolve-sub subscriptions [:brush]) [:> Brush])

       (make-area-display chart-id data subscriptions isAnimationActive?)])))


(defn configurable-component
  "the chart to draw, taking cues from the settings of the configuration panel

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - data : (atom) any data shown by the component's ui
  - component-id : (string) unique id of this chart within its container
  - container-id : (string, optional) name of the container this chart is inside of
  "
  ([& {:keys [data component-id container-id]}]
   [c/base-chart
    :data data
    :config (config component-id data)
    :component-id component-id
    :container-id (or container-id "")
    :data-panel utils/meta-tabular-data-panel
    :config-panel config-panel
    :component-panel component-panel]))


(defn component
  "the chart to draw. this variant does NOT provide a configuration panel

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - data : (atom) any data shown by the component's ui
  - component-id : (string) unique id of this chart within its container
  - container-id : (string, optional) name of the container this chart is inside of
  "
  ([& {:keys [data component-id container-id]}]
   [c/base-chart
    :data data
    :config (config component-id data)
    :component-id component-id
    :container-id container-id
    :component-panel component-panel]))


(comment
  (do
    (def chart-id "area-chart-demo/area-chart")
    (def data sample-data)
    (def subscriptions (ui-utils/build-subs chart-id (local-config data)))
    (def isAnimationActive? (r/atom true)))

  (make-area-display chart-id data subscriptions isAnimationActive?)


  (->> (get-in @data [:metadata :fields])
    (filter (fn [[_ v]] (= :number v)))
    keys
    (map (fn [a]
           (if (ui-utils/resolve-sub subscriptions [a :include])
             [:> Area (merge {:type              "monotone" :dataKey a
                              :isAnimationActive @isAnimationActive?
                              :fill              (ui-utils/resolve-sub subscriptions [a :fill])}
                        (when (seq (ui-utils/resolve-sub subscriptions [a :stackId]))
                          {:stackId (ui-utils/resolve-sub subscriptions [a :stackId])}))]
             [])))
    (remove empty?)
    (into [:<>]))

  ())



(def meta-data {:component component
                :configurable-component configurable-component
                :sources {:data :source-type/meta-tabular}
                :pubs []
                :subs []})
