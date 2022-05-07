(ns bh.rccst.ui-component.atom.chart.funnel-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as data]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]

            ["recharts" :refer [ResponsiveContainer FunnelChart Funnel Cell LabelList
                                XAxis YAxis CartesianGrid Tooltip Brush]]))


(log/info "bh.rccst.ui-component.atom.chart.funnel-chart")


(def sample-data
  "the Funnel Chart works best with \"paired data\" so we return the paired-data from utils"
  (r/atom data/meta-tabular-data))


(defn local-config [data]
  (let [d      (get @data :data)
        fields (get-in @data [:metadata :fields])]

    (merge
      ; process options for :name
      (->> fields
        (filter (fn [[k v]] (= :string v)))
        keys
        ((fn [m]
           {:name {:keys m :chosen (first m)}})))

      ; process :name to map up the :colors
      (->> d
        (map-indexed (fn [idx entry]
                       {(ui-utils/path->keyword (:name entry))
                        {:name  (:name entry)
                         :color (nth (cycle color/default-stroke-fill-colors) idx)}}))
        (into {})
        (assoc {} :colors))

      ; process options for :value
      (->> fields
        (filter (fn [[k v]] (= :number v)))
        keys
        ((fn [m]
           {:value {:keys m :chosen (first m)}}))))))


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

  - component-id : (string) unique id of the chart
  "
  [component-id data]

  (merge
    ui-utils/default-pub-sub
    utils/default-config
    (ui-utils/config-tab-panel component-id)
    (local-config data)))


(defn- color-anchors
  "build the config ui-components needed for each of the funnel slices
  "
  [component-id]
  [:<>
   (doall
     (map (fn [[id color-data]]
            (let [text  (:name color-data)]
              ^{:key id} [utils/color-config-text component-id text [:colors id :color] :right-above]))
       @(ui-utils/subscribe-local component-id [:colors])))])


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
  - component-id : (string) unique id of the chart
  "

  [_ component-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config component-id]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option component-id ":name" [:name]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option component-id ":value" [:value]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/label :src (rc/at) :label "Funnel Colors"]
                          (color-anchors component-id)]]]])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - component-id : (string) unique identifier for this chart instance
  "
  [data component-id container-id ui]
  (let [isAnimationActive? (ui-utils/subscribe-local component-id [:isAnimationActive])
        subscriptions      (ui-utils/build-subs component-id (local-config data))]

    (fn [data component-id]
      ;(log/info "configurable-funnel-chart" @config)
      [:> ResponsiveContainer
       [:> FunnelChart {:label true}

        (utils/non-gridded-chart-components component-id ui)

        [:> Funnel {:dataKey           (ui-utils/resolve-sub subscriptions [:value :chosen])
                    :nameKey           (ui-utils/resolve-sub subscriptions [:name :chosen])
                    :label             true
                    :data              (get @data :data)
                    :isAnimationActive @isAnimationActive?}
         (doall
           (map-indexed
             (fn [idx {name :name}]
               ^{:key (str idx name)}
               [:> Cell {:key  (str "cell-" idx)
                         :fill (or (:color (ui-utils/resolve-sub subscriptions [:colors name]))
                                 (color/get-color 0))}])
             (get @data :data)))
         [:> LabelList {:position :right :fill "#000000" :stroke "none" :dataKey (ui-utils/resolve-sub subscriptions [:value :chosen])}]]]])))


(def source-code `[:> FunnelChart {:height 400 :width 500}
                   [:> Funnel {:dataKey           :value
                               :nameKey           "name"
                               :label             true
                               :data              @data
                               :isAnimationActive @isAnimationActive?}]])


(defn configurable-component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - :data : (atom) any data shown by the component's ui
  - :component-id : (string) name of this component\n
  - :container-id : (string) name of the container this chart is inside of
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

  ---

  - :data : (atom) any data shown by the component's ui
  - :component-id : (string) name of this component
  - :container-id : (string) name of the container this chart is inside of
  "
  [& {:keys [data component-id container-id ui]}]
  [c/base-chart
   :data data
   :config (config component-id data)
   :component-id component-id
   :container-id (or container-id "")
   :component-panel component-panel
   :ui ui])


(def meta-data {:component              component
                :configurable-component configurable-component
                :sources                {:data :source-type/meta-tabular}
                :pubs                   []
                :subs                   []})



