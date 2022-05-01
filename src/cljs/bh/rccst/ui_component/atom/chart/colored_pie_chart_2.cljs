(ns bh.rccst.ui-component.atom.chart.colored-pie-chart-2
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as example-data]
            [bh.rccst.ui-component.atom.chart.wrapper-2 :as wrapper]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            ["recharts" :refer [ResponsiveContainer PieChart Pie Cell]]))


(log/info "bh.rccst.views.atom.example.chart.bar-chart-2")


(def source-code '[])
(def sample-data example-data/meta-tabular-data)


(defn local-config [data]

  ;(log/info "local-config" @data)

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


(defn config [component-id data]
  (merge
    ui-utils/default-pub-sub
    utils/default-config
    (ui-utils/config-tab-panel component-id)
    (local-config data)))


(defn- color-anchors
  "build the config ui-components needed for each of the pie slices
  "
  [chart-id]
  [:<>
   (doall
     (map (fn [[id color-data]]
            (let [text  (:name color-data)]
              ^{:key id} [utils/color-config-text chart-id text [:colors id :color] :right-above]))
       @(ui-utils/subscribe-local chart-id [:colors])))])


(defn config-panel [data component-id]
  ;(log/info "config-panel" component-id "//" @data)

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
              [utils/option component-id ":value" [:value]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[rc/label :src (rc/at) :label "Pie Colors"]
                          (color-anchors component-id)]]]])


(defn- component* [& {:keys [data component-id container-id
                             subscriptions isAnimationActive?]
                      :as params}]


  (let [d (if (empty? data) [] (get data :data))]

    ;(log/info "colored-pie-chart" component-id "//" data "//" d)

    [:> ResponsiveContainer
     [:> PieChart {:label true} (utils/override true {} :label)

      (utils/non-gridded-chart-components component-id {})

      [:> Pie {:dataKey           (ui-utils/resolve-sub subscriptions [:value :chosen])
               :nameKey           (ui-utils/resolve-sub subscriptions [:name :chosen])
               :data              d
               :label             (utils/override true {} :label)
               :isAnimationActive @isAnimationActive?}
       (doall
         (map-indexed
           (fn [idx {name :name}]
             ^{:key (str idx name)}
             [:> Cell {:key  (str "cell-" idx)
                       :fill (or (:color (ui-utils/resolve-sub subscriptions [:colors name]))
                               (color/get-color 0))}])
           d))]]]))


(defn component [& {:keys [data config-data component-id container-id
                           data-panel config-panel] :as params}]

  ;(log/info "component-2" params)

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


(def meta-data {:rechart/colored-pie-2 {:component component
                                        ;:configurable-component configurable-component
                                        :ports     {:data   :port/sink
                                                    :config :port/sink}}})

