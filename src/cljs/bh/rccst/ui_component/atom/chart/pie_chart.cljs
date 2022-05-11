(ns bh.rccst.ui-component.atom.chart.pie-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as data]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]

            ["recharts" :refer [ResponsiveContainer PieChart Pie]]))


(log/info "bh.rccst.ui-component.atom.chart.pie-chart")


(def sample-data
  "the Pie Chart works best with \"paired data\" so we return the paired-data from utils"

  ; switching to tabular data to work out the UI logic
  (r/atom data/meta-tabular-data))


(defn local-config [data]
  (let [d      (get @data :data)
        fields (get-in @data [:metadata :fields])]

    (merge
      {:fill (color/get-color 0)}

      ; process options for :name
      (->> fields
        (filter (fn [[k v]] (= :string v)))
        keys
        ((fn [m]
           {:name {:keys m :chosen (first m)}})))

      ; process options for :value
      (->> fields
        (filter (fn [[k v]] (= :number v)))
        keys
        ((fn [m]
           {:value {:keys m :chosen (first m)}}))))))


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

  - component-id : (string) unique id of the chart
  - data : (atom) atom holding metadata-wrapped data to display
  "
  [component-id data]
  (-> ui-utils/default-pub-sub
    (merge
      utils/default-config
      (ui-utils/config-tab-panel component-id)
      (local-config data))))


(defn- config-panel
  "the panel of configuration controls

  ---

  - _ (ignored)
  - component-id : (string) unique identifier for this specific widget instance

  "
  [_ component-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "500px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config component-id]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option component-id ":name" [:name]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option component-id ":value" [:value]]
              [utils/color-config-text component-id ":fill" [:fill] :above-right]]])


(def source-code '[:> PieChart {:width 400 :height 400}
                   [:> Tooltip]
                   [:> Legend]
                   [:> Pie {:dataKey "value" :data @data :fill "#8884d8"}]])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - component-id : (string) unique identifier for this specific widget instance
  "
  [data component-id container-id ui]
  (let [container          (ui-utils/subscribe-local component-id [:container])
        isAnimationActive? (ui-utils/subscribe-local component-id [:isAnimationActive])
        subscriptions      (ui-utils/build-subs component-id (local-config data))]

    (fn [data component-id container-id ui]
      [:> ResponsiveContainer
         [:> PieChart {:label (utils/override true ui :label)}

          (utils/non-gridded-chart-components component-id ui)

          [:> Pie {:dataKey           (ui-utils/resolve-sub subscriptions [:value :chosen])
                   :nameKey           (ui-utils/resolve-sub subscriptions [:name :chosen])
                   :data              (get @data :data)
                   :fill              (ui-utils/resolve-sub subscriptions [:fill])
                   :label             (utils/override true ui :label)
                   :isAnimationActive @isAnimationActive?}]]])))


(defn configurable-component
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - :component-id : (string) name of this chart\n
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

  ---

  - :data : (atom) any data shown by the component's ui
  - :component-id : (string) name of this chart
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






; work out the logic for (option-config)
(comment
  (do
    (def data sample-data)
    (def component-id "pie-chart-demo/pie-chart"))

  (do
    (def label ":name")
    (def path-root [:name])

    (def chosen-path (conj path-root :chosen))
    (def keys-path (conj path-root :keys))
    (def chosen (ui-utils/subscribe-local component-id chosen-path))
    (def keys (ui-utils/subscribe-local component-id keys-path))
    (def btns (->> @keys
                (map (fn [k]
                       {:id k :label k})))))

  (def subscriptions (ui-utils/build-subs component-id (local-config data)))

  ())

