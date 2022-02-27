(ns bh.rccst.ui-component.atom.chart.colored-pie-chart
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]
            [reagent.core :as r]

            ["recharts" :refer [PieChart Pie Cell]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]))


(def sample-data
  "the Pie Chart works best with \"paired data\" so we return the paired-data from utils"
  (r/atom utils/meta-tabular-data))


(defn local-config [data]
  (let [d (get @data :data)
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
        (map :name)
        (#(zipmap % ui-utils/default-stroke-fill-colors))
        (assoc {} :colors))

      ; process options for :value
      (->> fields
        (filter (fn [[k v]] (= :number v)))
        keys
        ((fn [m]
           {:value {:keys m :chosen (first m)}}))))))


(comment
  (def data sample-data)
  (def d (get @data :data))

  (->> d
    (map :name ,)
    (#(zipmap % ui-utils/default-stroke-fill-colors) ,)
    (assoc {} :colors ,))

  (as-> d x
    (map :name x)
    (zipmap x ui-utils/default-stroke-fill-colors)
    (assoc {} :colors x))


  ())


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
  - data : (atom) metadata wrapped data to display
  "
  [chart-id data]
  (merge
    ui-utils/default-pub-sub
    utils/default-config
    (ui-utils/config-tab-panel chart-id)
    (local-config data)))


(defn- color-anchors
  "build the config ui-components needed for each of the pie slices
  "
  [chart-id]
  [:<>
   (doall
     (map (fn [[id _]]
            ^{:key id}[utils/color-config-text chart-id id [:colors id] :right-above])
       @(ui-utils/subscribe-local chart-id [:colors])))])


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
  [_ chart-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "400px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config chart-id]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option chart-id ":name" [:name]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option chart-id ":value" [:value]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/label :src (rc/at) :label "Pie Colors"]
                          (color-anchors chart-id)]]]])


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
  [data chart-id]
  (let [isAnimationActive? (ui-utils/subscribe-local chart-id [:isAnimationActive])
        subscriptions (ui-utils/build-subs chart-id (local-config data))]

    (fn [data chart-id]
      [:> PieChart {:width 400 :height 400 :label true}

       (utils/non-gridded-chart-components chart-id)

       [:> Pie {:dataKey (ui-utils/resolve-sub subscriptions [:value :chosen])
                :nameKey (ui-utils/resolve-sub subscriptions [:name :chosen])
                :data (get @data :data)
                :label true
                :isAnimationActive @isAnimationActive?}
        (doall
          (map-indexed
            (fn [idx {name :name}]
              ^{:key (str idx name)}
              [:> Cell {:key (str "cell-" idx)
                        :fill (or (ui-utils/resolve-sub subscriptions [:colors name])
                                (ui-utils/get-color 0))}])
            (get @data :data)))]])))


(defn configurable-component
  "the chart to draw, taking cues from the settings of the configuration panel

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - :data : (atom) any data shown by the component's ui
  - :component-id : (string) name of the component itself
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

  the component creates its own ID (a random-uuid) to hold the local state. This way multiple charts
  can be placed inside the same outer container/composite

  ---

  - data : (atom) any data shown by the component's ui
  - container-id : (string) name of the container this chart is inside of
  "
  ([& {:keys [data component-id container-id]}]
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



(comment
  (do
    (def data sample-data)
    (def chart-id "colored-pie-chart-demo/colored-pie-chart")
    (def subscriptions (ui-utils/build-subs chart-id (local-config data))))

  (ui-utils/resolve-sub subscriptions [:colors "Group A"])

  ())

