(ns bh.rccst.ui-component.atom.chart.radar-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            ["recharts" :refer [RadarChart PolarGrid PolarAngleAxis PolarRadiusAxis Radar]]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(def sample-data (r/atom {:metadata {:type   :tabular
                                     :id     :subject
                                     ;:domain :fullMark
                                     :domainRangeMin 10
                                     :domainRangeMax 100
                                     :fields {:subject :string :A :number :B :number :fullMark :number}}
                          :data     [{:subject "Math" :A 120 :B 110}
                                     {:subject "Chinese" :A 98 :B 130}
                                     {:subject "English" :A 100 :B 110}
                                     {:subject "History" :A 77 :B 81}
                                     {:subject "Economics" :A 99 :B 140}
                                     {:subject "Literature" :A 98 :B 105}]}))



(defn- get-range-across-fields [data]
       (let [source-data (get-in @data [:data])
             all-values (->> (get-in @data [:metadata :fields])
                             (filter (fn [[k v]] (= :number v)))
                             keys
                             (map-indexed (fn [idx a] (map #(a %) source-data)))
                             (reduce into)
                             (distinct))
             domainMin (apply min all-values)
             domainMax (apply max all-values)]
            (log/info "domain min = " domainMin, "domain max = " domainMax)
            (if (= domainMin domainMax)
              {:domain [0 domainMax]}
              {:domain [domainMin domainMax]})))

(comment
  (def source-data (get-in @data [:data]))
  (def all-values (->> (get-in @data [:metadata :fields])
                       (filter (fn [[k v]] (= :number v)))
                       keys
                       (map-indexed (fn [idx a] (map #(a %) source-data)))
                       (reduce into)
                       (distinct)))
  (def domainMin (apply min all-values))
  (def domainMax (apply max all-values))

  (->> (get-in @data [:metadata :fields])
       (filter (fn [[k v]] (= :number v)))
       keys
       (map-indexed (fn [idx a] (map #(a %) source-data)))
       (reduce into)
       (distinct))
  ())

(defn- get-field-range [field data]
       (let [source-data (get-in @data [:data])
             domainMin (reduce min (map #(field %) source-data))
             domainMax (reduce max (map #(field %) source-data))]
            (if (= domainMin domainMax)
              {:domain [0 domainMax]}
              {:domain [domainMin domainMax]})))


(defn- domain-range [data]
       (let [domainField (get-in @data [:metadata :domain])]
            (if (nil? domainField)
              (get-range-across-fields data)
              (get-field-range domainField data))))


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
      (merge (domain-range data)
             (->> (get-in @data [:metadata :fields])
                  (filter (fn [[k v]] (= :number v)))
                  keys
                  (map-indexed (fn [idx a]
                                   {a {:include     true
                                       :name        a
                                       :fill        (ui-utils/get-color idx)
                                       :stroke      (ui-utils/get-color idx)
                                       :fillOpacity 0.6}}))
                  (into {}))))


(def source-code '[:> RadarChart {:width 400 :height 400 :outerRadius "75%" :data @data}
                   (utils/non-gridded-chart-components widget-id)

                   [:> PolarGrid]
                   [:> PolarAngleAxis {:dataKey :subject}]
                   [:> PolarRadiusAxis {:angle "30" :domain [0, 150]}]
                   [:> Radar {:name        "Mark"
                              :dataKey     :A
                              :fill        "#8884d8"
                              :stroke      "#8884d8"
                              :fillOpacity 0.5}]])


(defn config
      "constructs the configuration panel for the chart's configurable properties. This is specific to
      this being a radar-chart component (see [[local-config]]).

      Merges together the configuration needed for:

      1. radar charts
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

            (local-config data))))


(defn- radar-config [chart-id label path position]
       [rc/v-box :src (rc/at)
        :gap "5px"
        :children [[utils/boolean-config chart-id label (conj path :include)]
                   [utils/color-config chart-id ":fill" (conj path :fill) position]
                   [utils/color-config chart-id ":stroke" (conj path :stroke) position]
                   [utils/slider-config chart-id 0 1 0.1 (conj path :fillOpacity)]]])

(defn- make-radar-config [chart-id data]
       (->> (get-in @data [:metadata :fields])
            (filter (fn [[k v]] (= :number v)))
            keys
            (map-indexed (fn [idx a]
                             [radar-config chart-id a [a] :above-right]))
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
       :children [[utils/non-gridded-chart-config chart-id]
                  [rc/line :src (rc/at) :size "2px"]
                  [rc/h-box :src (rc/at)
                   :width "400px"
                   :style ui-utils/h-wrap
                   :gap "10px"
                   :children (make-radar-config chart-id data)]]])

(defn- make-radar-display [chart-id data subscriptions]
       (->> (get-in @data [:metadata :fields])
            (filter (fn [[_ v]] (= :number v)))
            keys
            (map (fn [a]
                     (if (ui-utils/resolve-sub subscriptions [a :include])
                       [:> Radar {:name        (ui-utils/resolve-sub subscriptions [a :name])
                                  :dataKey     a
                                  :fill        (ui-utils/resolve-sub subscriptions [a :fill])
                                  :stroke      (ui-utils/resolve-sub subscriptions [a :stroke])
                                  :fillOpacity (ui-utils/resolve-sub subscriptions [a :fillOpacity])}]
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
             subscriptions (ui-utils/build-subs chart-id (local-config data))]

            (fn []
                [:> RadarChart {:width 400 :height 400 :data (get @data :data)}
                 [:> PolarGrid]
                 [:> PolarAngleAxis {:dataKey :subject}]
                 [:> PolarRadiusAxis {:angle "30" :domain (ui-utils/resolve-sub subscriptions [:domain])}]

                 (utils/non-gridded-chart-components chart-id)

                 (make-radar-display chart-id data subscriptions)])))

(comment
  (def chart-id "radar-chart-demo/radar-chart")
  (def data sample-data)
  (def subscriptions (ui-utils/build-subs chart-id (local-config data)))
  ())
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
                      (ui-utils/init-widget @id (config @id data))
                      (ui-utils/dispatch-local @id [:container] container-id))

                ;(log/info "component" @id)

                [c/configurable-chart
                 :data data
                 :id @id
                 :data-panel utils/dummy-data-panel
                 ;:data-panel sample-data
                 :config-panel config-panel
                 :component component-panel]))))

(comment
  (def domainField :fullMark)
  (def source-data (get-in @sample-data [:data]))
  (reduce max (map #(domainField %) source-data))
  (let [source-data (get-in @sample-data [:data])])

  (def fields (get-in @sample-data [:metadata :fields]))
  (filter (fn [[k v]] (= :number v)) fields)
  keys


  (def numValues nil)
  (concat numValues '(1 2 3))
  (concat numValues '(3 4 5))

  (def source-data (get-in @sample-data [:data]))
  (->> (get-in @sample-data [:metadata :fields])
       (filter (fn [[k v]] (= :number v)))
       keys
       (map-indexed (fn [idx a] (map #(a %) source-data)))
       (reduce into)
       (distinct))

  (def fieldNames (get-in @sample-data [:metadata :fields]))
  (def numFieldsOnly (filter (fn [[k v]] (= :number v)) fieldNames))
  (def keysOnly (keys numFieldsOnly))
  (def res (map-indexed (fn [idx a] (a source-data)) keysOnly))
  ())

