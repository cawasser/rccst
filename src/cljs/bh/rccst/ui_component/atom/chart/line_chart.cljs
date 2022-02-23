(ns bh.rccst.ui-component.atom.chart.line-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]

            ["recharts" :refer [LineChart Line Brush]]
            [re-com.core :as rc]

            [reagent.core :as r]
            [taoensso.timbre :as log]))


(def sample-data
  "the Line Chart works best with \"tabular data\" so we return the tabular-data from utils"
  (r/atom utils/meta-tabular-data))


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
                     ;(log/info "line color" idx a (ui-utils/get-color idx))
                     {a {:include true :stroke (ui-utils/get-color idx) :fill (ui-utils/get-color idx)}}))
      (into {}))))


(defn- config
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
  - data : (atom) data and meta-data for the chart
  "
  [chart-id data]
  (-> ui-utils/default-pub-sub
    (merge
      utils/default-config
      {:type      "line-chart"
       :tab-panel {:value     (keyword chart-id "config")
                   :data-path [:widgets (keyword chart-id) :tab-panel]}}
      (local-config data))
    (assoc-in [:x-axis :dataKey] (get-in @data [:metadata :id]))
    (assoc-in [:pub] :name)
    (assoc-in [:sub] :something-selected)))


(defn- line-config [widget-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config widget-id label (conj path :include)]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[utils/color-config widget-id ":stroke" (conj path :stroke) position]
                          [utils/color-config widget-id ":fill" (conj path :fill) position]]]]])


(defn- make-line-config [chart-id data]
  (->> (get-in @data [:metadata :fields])
    (filter (fn [[k v]] (= :number v)))
    keys
    (map-indexed (fn [idx a]
                   [line-config chart-id a [a] :above-right]))
    (into [])))


(defn- config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.
  - chart-id : (string) unique identifier for this chart instance
  "
  [data chart-id]

  ;(log/info "config-panel" @data chart-id)

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
               :children (make-line-config chart-id data)]
              [rc/line :src (rc/at) :size "2px"]
              [utils/boolean-config chart-id ":brush?" [:brush]]]])


(defn- make-line-display [chart-id data subscriptions isAnimationActive?]
  (->> (get-in @data [:metadata :fields])
    (filter (fn [[_ v]] (= :number v)))
    keys
    (map (fn [a]
           (if (ui-utils/resolve-sub subscriptions [a :include])
             [:> Line {:type              "monotone" :dataKey a
                       :isAnimationActive @isAnimationActive?
                       :stroke            (ui-utils/resolve-sub subscriptions [a :stroke])
                       :fill              (ui-utils/resolve-sub subscriptions [a :fill])}]
             [])))
    (remove empty?)
    (into [:<>])))


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - data : (atom) any data shown by the component's ui
  - chart-id : (string) unique identifier for this chart instance within this container
  - container-id : (string) name of the container this chart is inside of
  "
  [data chart-id]

  ;(log/info "component-panel" @data chart-id)

  (let [container (ui-utils/subscribe-local chart-id [:container])
        isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])
        subscriptions (ui-utils/build-subs chart-id (local-config data))]

    (fn []
      ; TODO: more refactoring!!!!
      (ui-utils/publish-to-container @container [chart-id :brush] (ui-utils/resolve-sub subscriptions [:brush]))
      (ui-utils/publish-to-container @container [chart-id :uv] (ui-utils/resolve-sub subscriptions [:uv :include]))
      (ui-utils/publish-to-container @container [chart-id :pv] (ui-utils/resolve-sub subscriptions [:pv :include]))
      (ui-utils/publish-to-container @container [chart-id :amt] (ui-utils/resolve-sub subscriptions [:amt :include]))

      [:> LineChart {:width 400 :height 400 :data (get @data :data)}

       (utils/standard-chart-components chart-id)

       (when (ui-utils/resolve-sub subscriptions [:brush]) [:> Brush])

       (make-line-display chart-id data subscriptions isAnimationActive?)])))


(def source-code '[:> LineChart {:width 400 :height 400 :data @data}])


(defn configurable-component
  ([data component-id]
   [configurable-component data component-id ""])

  ([data component-id container-id]
   [c/base-chart
    :data data
    :config (config component-id data)
    :component-id component-id
    :container-id container-id
    :data-panel utils/meta-tabular-data-panel
    :config-panel config-panel
    :component-panel component-panel]))


(defn component
  ([data component-id]
   [component data component-id ""])


  ([data component-id container-id]
   [c/base-chart
    :data data
    :config (config component-id data)
    :component-id component-id
    :container-id container-id
    :component-panel component-panel]))

(comment
  (def data sample-data)
  (def chart-id "line-chart-demo/line-chart")

  (def subscriptions (ui-utils/build-subs chart-id (local-config data)))


  ())


(comment
  (def data sample-data)

  (def p1 {:name :string, :uv :number, :pv :number, :amt :number, :owner :string})

  (filter (fn [[k v]] (= :number v)) p1)

  (->> (get-in @data [:metadata :fields])
    (filter (fn [[k v]] (= :number v)))
    keys)

  (keyword :line-1)
  (keyword (clojure.string/replace "line 1" #" " "-"))

  (clojure.string/replace :line-1 #" " "-")

  (def k '(:uv :pv :amt))
  (map-indexed (fn [idx a]
                 {a {:include true :stroke "#8884d8" :fill "#8884d8"}})
    k)


  (map-indexed (fn [idx a]
                 {(keyword (clojure.string/replace a #" " "-"))
                  {:include true :stroke "#8884d8" :fill "#8884d8"}})
    k)

  ())
