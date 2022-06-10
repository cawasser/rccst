(ns bh.rccst.ui-component.atom.chart.scatter-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper-2 :as wrapper]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as data]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            ["recharts" :refer [ResponsiveContainer ScatterChart Scatter Brush
                                XAxis YAxis ZAxis Tooltip]]))


(log/info "bh.rccst.ui-component.atom.chart.scatter-chart")


(def sample-data
  "the Scatter Chart works best with \"triplet data\" so we return the triplet-data from utils"
  data/meta-tabular-data)


(defn local-config [data]
  (let [d      (:data @data)
        fields (get-in @data [:metadata :fields])]
    (merge
      (->> fields
        (filter (fn [[k v]] (= :string v)))
        keys
        ((fn [m]
           {:name {:keys m :chosen (first m)}})))

      (->> fields
        (filter (fn [[k v]] (= :number v)))
        keys
        ((fn [m]
           {:values {:keys m :chosen (take 3 m)}})))

      (->> d
        (map-indexed (fn [idx entry]
                       {(ui-utils/path->keyword (:name entry))
                        {:name    (:name entry)
                         :include true
                         :color   (nth (cycle color/default-stroke-fill-colors) idx)}}))
        (into {})))))


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

  - component-id : (string) unique id of the chart
  "
  [component-id data]
  ;(log/info "scatter config" data)
  (->
    ui-utils/default-pub-sub
    (merge
      utils/default-config
      (ui-utils/config-tab-panel component-id)
      (local-config data))))


(defn- cell-config [component-id label path position]
  (let [p (ui-utils/path->keyword path)]
    ;(log/info "cell-config" component-id "//" label "//" p)
    [rc/h-box
     :gap "5px"
     :children [[utils/boolean-config component-id "" (conj [p] :include)]
                [utils/color-config-text component-id label (conj [p] :color) :right-above]]]))


(defn- make-cell-config [component-id data]
  (->> (:data @data)
    (map-indexed (fn [idx {:keys [name] :as item}]
                   [cell-config component-id name [name] :above-right]))
    (into [:<>])))


(defn config-panel [data component-id]
  ;(log/info "scatter config-panel" component-id "//" data)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "400px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/non-gridded-chart-config @data component-id]
              [rc/line :src (rc/at) :size "2px"]
              [utils/option component-id ":name" [:name]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/column-multi-picker data component-id ":values" [:values :chosen]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/label :src (rc/at) :label "Colors"]
                          (make-cell-config component-id data)]]]])


(def source-code `[:> ScatterChart {:width 400 :height 400}])


(defn- make-scatter [data subscriptions]
  (let [ret    (->> data
                 :data
                 (map (fn [{:keys [name] :as item}]
                        (if (ui-utils/resolve-sub subscriptions [name :include])
                          [:> Scatter
                           {:name name
                            :fill (or (ui-utils/resolve-sub subscriptions [name :color])
                                    (color/get-color 0))
                            :data [item]}]
                          [])))
                 (remove empty?)
                 (into [:<>]))]

    ;(log/info "make-scatter" values "//" ret)
    ret))


(comment
  (def item {:name "name" :uv "uv" :pv "pv" :tv "tv"})
  (def values '(:uv :pv))

  (select-keys item values)

  ())


(defn- component*
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - component-id : (string) unique identifier for this specific widget instance
  "
  [& {:keys [data component-id container-id
             subscriptions isAnimationActive?]
      :as   params}]

  ;(log/info "component*" data)

  (let [[x y z] (ui-utils/resolve-sub subscriptions [:values :chosen])]

    [:> ResponsiveContainer
     [:> ScatterChart

      (utils/chart-grid component-id {})

      [:> Tooltip]
      [:> XAxis {:type "number" :dataKey (name x)}]
      [:> YAxis {:type "number" :dataKey (name y)}]
      [:> ZAxis {:type "number" :dataKey (name z) :range [0 10000]}]

      (make-scatter data subscriptions)]]))

(defn component [& {:keys [data config-data component-id container-id
                           data-panel config-panel] :as params}]

  ;(log/info "component" params)

  [wrapper/base-chart
   :data data
   :config-data config-data
   :component-id component-id
   :container-id container-id
   :component* component*
   :component-panel wrapper/component-panel
   :data-panel data-panel
   :config-panel config-panel
   :config config
   :local-config local-config])


(def meta-data {:component component
                :sources   {:data :source-type/meta-tabular}
                :pubs      []
                :subs      []})

(comment
  (def data sample-data)

  (->> @data
    (map #(-> % (dissoc :name)))
    (map #(->> % vals (zipmap [:x :y :z]))))


  ())


(comment
  (def data [{:name "page-1" :fill "#ffffff" :uv 4000 :pv 2400 :amt 2400}
             {:name "page-2" :fill "#00ff00" :uv 3000 :pv 1398 :amt 2210}])

  (first data)


  (map (fn [{:keys [x]}] {:x x}) data)

  (map (fn [{:keys [name]}] {:x name}) data)

  (map (fn [{:keys [name fill]}] [:> Scatter {:name name :fill fill}]) data)

  (map (fn [{:keys [name fill uv pv amt]}] [:> Scatter {:name name :fill fill :data {:uv uv :pv pv :amt amt}}]) data)


  (into [:<>] (map (fn [{:keys [name fill uv pv amt]}] [:> Scatter {:name name :fill fill :data {:uv uv :pv pv :amt amt}}]) data))


  ())