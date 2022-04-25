(ns bh.rccst.ui-component.atom.chart.bar-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as data]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            ["recharts" :refer [ResponsiveContainer BarChart Bar Brush]]))

(log/info "bh.rccst.ui-component.atom.chart.bar-chart")

(def sample-data
  "the Bar Chart works best with \"tabular data\" so we return the tabular-data from utils,
  and we mix-in a fourth column just to show how it can be done"
  (let [source data/meta-tabular-data
        data   (get source :data)
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

  ;(log/info "local-data" data)

  (merge
    {:brush false}
    (->> (get-in @data [:metadata :fields])
      (filter (fn [[k v]] (= :number v)))
      keys
      (map-indexed (fn [idx a]
                     {a {:include true
                         :fill    (color/get-color idx)
                         :stackId ""}}))
      (into {}))))


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
  - data : (atom) metadata wrapped data  to display
  "
  [chart-id data]

  ;(log/info "config" chart-id "//" data)

  (-> ui-utils/default-pub-sub
    (merge
      utils/default-config
      {:tab-panel {:value     (keyword chart-id "config")
                   :data-path [:widgets (keyword chart-id) :tab-panel]}}
      (local-config data))
    (assoc-in [:x-axis :dataKey] :name)
    ; TODO: this should be produced by a function that processes the data
    (assoc-in [:sub] [[:brush]
                      [:uv :include] [:uv :stroke] [:uv :fill]
                      [:pv :include] [:pv :stroke] [:pv :fill]])))


(defn- bar-config [chart-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config chart-id label (conj path :include)]
              [utils/color-config chart-id ":fill" (conj path :fill) position]
              [utils/text-config chart-id ":stackId" (conj path :stackId)]]])


(defn- make-bar-config [chart-id data]
  ;(log/info "make-bar-config" chart-id "//" data)

  (->> (get-in @data [:metadata :fields])
    (filter (fn [[k v]] (= :number v)))
    keys
    (map-indexed (fn [idx a]
                   [bar-config chart-id a [a] :above-right]))
    (into [])))


(defn config-panel
  "the panel of configuration controls

  ---

  - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.\n  - config : (atom) holds all the configuration settings made by the user
  "
  [data chart-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "400px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data chart-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :width "400px"
               :style ui-utils/h-wrap
               :gap "10px"
               :children (make-bar-config chart-id data)]
              [rc/line :src (rc/at) :size "2px"]
              [utils/boolean-config chart-id ":brush?" [:brush]]]])


(def source-code '[:> BarChart {:width 400 :height 400 :data (get @data :data)}])


(defn- make-bar-display [chart-id data subscriptions isAnimationActive?]
  ;(log/info "make-bar-display" data "//" @data "//" @isAnimationActive?
  ;  "//" subscriptions)

  (->> (get-in @data [:metadata :fields])
    (filter (fn [[_ v]] (= :number v)))
    keys
    (map (fn [a]
           (if (ui-utils/resolve-sub subscriptions [a :include])
             [:> Bar (merge {:type              "monotone" :dataKey a
                             :isAnimationActive @isAnimationActive?
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
  [data component-id container-id ui]

  ;(log/info "component-panel" component-id "//" container-id "//" data)

  (let [d                  (h/resolve-value data)
        container          (ui-utils/subscribe-local component-id [:container])
        isAnimationActive? (ui-utils/subscribe-local component-id [:isAnimationActive])
        override-subs      @(ui-utils/subscribe-local component-id [:sub])
        l-c                (local-config d)
        local-subs         (ui-utils/build-subs component-id l-c)
        subscriptions      (ui-utils/override-subs container-id local-subs override-subs)]

    ;(log/info "component-panel" component-id "//" data
    ;  "// resolved" @d
    ;  "// l-c" l-c
    ;  "// local-subs" local-subs
    ;  "// subs" subscriptions)

    (fn []
      (if (empty? @d)
        [rc/alert-box :src (rc/at)
         :alert-type :info
         :style {:width "100%"}
         :heading "Waiting for data"]

        [:> ResponsiveContainer
         [:> BarChart {:data (get @d :data)}

          (utils/standard-chart-components component-id ui)

          (when (ui-utils/resolve-sub subscriptions [:brush]) [:> Brush])

          (make-bar-display component-id d subscriptions isAnimationActive?)]]))))



(comment
  (do
    (def component-id ":chart-remote-data-demo.widget.ui.bar-chart")
    (def container-id ":chart-remote-data-demo.widget")
    (def data [:bh.rccst.subs/source :source/measurements])

    (def d (h/resolve-value data))
    (def container (ui-utils/subscribe-local component-id [:container]))
    (def isAnimationActive? (ui-utils/subscribe-local component-id [:isAnimationActive]))
    (def override-subs @(ui-utils/subscribe-local component-id [:sub]))
    (def local-subs (ui-utils/build-subs component-id (local-config d)))
    (def subscriptions (ui-utils/override-subs container-id
                         local-subs override-subs)))


  ())

(defn configurable-component
  "the chart to draw, taking cues from the settings of the configuration panel

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - data : (atom) any data shown by the component's ui
  - container-id : (string) name of the container this chart is inside of
  "
  [& {:keys [data component-id container-id ui]}]

  [c/base-chart
   :data data
   :config (config component-id data)
   :component-id component-id
   :container-id (or container-id "")
   :data-panel utils/meta-tabular-data-panel
   :config-panel config-panel
   :component-panel component-panel
   :ui ui])


(defn component
  "the chart to draw. this variant does NOT provide a configuration panel

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - data : (atom) any data shown by the component's ui
  - container-id : (string) name of the container this chart is inside of
  "
  [& {:keys [data component-id container-id ui]}]

  (let [d (h/resolve-value data)]

    ;(log/info "component" data "//" d "//" @d)

    [c/base-chart
     :data data
     :config (config component-id d)
     :component-id component-id
     :container-id (or container-id "")
     :component-panel component-panel
     :ui ui]))


(def meta-data {:rechart/bar {:component              component
                              :configurable-component configurable-component
                              :ports                  {:data :port/sink}
                              :sources                {:data :source-type/meta-tabular}
                              :pubs                   []
                              :subs                   []}})




(comment

  (def d (re-frame/subscribe [:bh.rccst.subs/source :source/measurements]))

  @d

  ())


(comment
  (do
    (def chart-id "bar-chart-demo/bar-chart")
    (def data sample-data)
    (def subscriptions (ui-utils/build-subs chart-id (local-config data)))
    (def isAnimationActive? (r/atom true)))

  (make-bar-display chart-id data subscriptions isAnimationActive?)


  (->> (get-in @data [:metadata :fields])
    (filter (fn [[_ v]] (= :number v)))
    keys
    (map (fn [a]
           (if (ui-utils/resolve-sub subscriptions [a :include])
             [:> Bar (merge {:type              "monotone" :dataKey a
                             :isAnimationActive @isAnimationActive?
                             :fill              (ui-utils/resolve-sub subscriptions [a :fill])}
                       (when (seq (ui-utils/resolve-sub subscriptions [a :stackId]))
                         {:stackId (ui-utils/resolve-sub subscriptions [a :stackId])}))]
             [])))
    (remove empty?)
    (into [:<>]))

  ())