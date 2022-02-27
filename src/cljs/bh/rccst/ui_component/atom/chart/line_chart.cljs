(ns bh.rccst.ui-component.atom.chart.line-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            ["recharts" :refer [LineChart Line Brush]]
            [reagent.core :as r]
            [re-com.core :as rc]))


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
                     {a {:include true
                         :stroke  (ui-utils/get-color idx)
                         :fill    (ui-utils/get-color idx)}}))
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
    ;; TODO: this should be produced by a function that processes the data
    ;; or passed in?
    ;; or looked up from metadata?
    (assoc-in [:sub] [[:brush]
                      [:uv :include] [:uv :stroke] [:uv :fill]
                      [:pv :include] [:pv :stroke] [:pv :fill]])))


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
  [data component-id container-id]

  ;(log/info "component-panel" chart-id "///" @(ui-utils/subscribe-local chart-id [:container]))

  (let [container @(ui-utils/subscribe-local component-id [:container])
        isAnimationActive? (ui-utils/subscribe-local component-id [:isAnimationActive])
        override-subs @(ui-utils/subscribe-local component-id [:sub])
        local-subs (ui-utils/build-subs component-id (local-config data))
        subscriptions (ui-utils/override-subs container-id local-subs override-subs)]

    (fn []
      ;[:div "line Chart"]
      [:> LineChart {:width 400 :height 400 :data (get @data :data)}

       (utils/standard-chart-components component-id)

       (when (ui-utils/resolve-sub subscriptions [:brush]) [:> Brush])

       (make-line-display component-id data subscriptions isAnimationActive?)])))


(def source-code '[:> LineChart {:width 400 :height 400 :data @data}])


(defn configurable-component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - :data : (atom) any data shown by the component's ui
  - :component-id : (string) name of this component\n
  - :container-id : (string) name of the container this chart is inside of
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

  ---

  - :data : (atom) any data shown by the component's ui
  - :component-id : (string) name of this component
  - :container-id : (string) name of the container this chart is inside of
  "
  ([& {:keys [data component-id container-id]}]
   ;(log/info "line-chart component" container-id)
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





; subscriptions
(comment
  (def data sample-data)
  (def chart-id "line-chart-demo/line-chart")
  (def container-id "line-chart-demo")
  (def chart-id "multi-chart-demo/multi-chart/line-chart")
  (def container-id "multi-chart-demo/multi-chart")

  (get-in @re-frame.db/app-db
    [:widgets
     (keyword "multi-chart-demo/multi-chart/line-chart")
     :sub])


  (ui-utils/subscribe-local chart-id [:sub])


  (def c (config chart-id data))
  (get-in c [:tab-panel :value])

  (def subscriptions
    (ui-utils/build-container-subs container-id (local-config data)))

  (def subscriptions
    (ui-utils/build-subs container-id (local-config data)))


  ())


; playing with building keywords
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



; override local subscriptions with ones from the container
(comment
  (do
    (def data sample-data)
    (def component-id "multi-chart-demo/multi-chart/line-chart")
    (def container-id "multi-chart-demo/multi-chart")
    (def override-subs @(ui-utils/subscribe-local component-id [:sub]))
    (def local-subs (ui-utils/build-subs component-id (local-config data))))

  (def path (first override-subs))

  (ui-utils/subscribe-to-container container-id path)


  (->> override-subs
    (map (fn [path]
           (println "override-subs" container-id path)
           {path (ui-utils/subscribe-to-container container-id path)}))
    (apply merge local-subs))

  (->> override-subs
    (map (fn [path]
           (println "override-subs" container-id path)
           {path (ui-utils/subscribe-to-container container-id path)}))
    (appl assoc local-subs))


  (def subscriptions (ui-utils/override-subs container-id local-subs override-subs))

  (deref (get subscriptions [:brush]))

  (ui-utils/resolve-sub subscriptions [:brush])

  ())

