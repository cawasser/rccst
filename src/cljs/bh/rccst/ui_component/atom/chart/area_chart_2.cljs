(ns bh.rccst.ui-component.atom.chart.area-chart-2
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.re-com.configure-toggle :as ct]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.example-data :as example-data]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.ui-component.utils.locals :as l]
            [bh.rccst.ui-component.atom.chart.wrapper-2 :as wrapper]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            ["recharts" :refer [ResponsiveContainer AreaChart Area Brush]]))

(log/info "bh.rccst.views.atom.example.chart.area-chart-2")


(def source-code '[])
(def sample-data example-data/meta-tabular-data)


(defn local-config [data]

  ;(log/info "local-config" @data)

  (let [ret (merge
              {:brush false}
              (->> (get-in @data [:metadata :fields])
                   (filter (fn [[k v]] (= :number v)))
                   keys
                   (map-indexed (fn [idx a]
                                  {a {:include true
                                      :fill    (color/get-color idx)
                                      :stroke  (color/get-color idx)
                                      :stackId ""}}))
                   (into {})))]
    ;(log/info "local-config" ret)
    ret))


(defn config [chart-id data]
  (-> ui-utils/default-pub-sub
      (merge
        utils/default-config
        {:tab-panel {:value     (keyword chart-id "config")
                     :data-path [:widgets (keyword chart-id) :tab-panel]}}
        (local-config data))
      (assoc-in [:x-axis :dataKey] :name)))


(defn- area-config [component-id label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config component-id label (conj path :include)]
              [utils/color-config component-id ":fill" (conj path :fill) position]
              [utils/color-config component-id ":stroke" (conj path :stroke) :right-above]
              [utils/text-config component-id ":stackId" (conj path :stackId)]]])


(defn- make-area-config [component-id data]
  ;(log/info "make-area-config" component-id "//" @data)

  (->> (get-in @data [:metadata :fields])
       (filter (fn [[k v]] (= :number v)))
       keys
       (map-indexed (fn [idx a]
                      [area-config component-id a [a] :above-right]))
       (into [])))


(defn config-panel [data component-id]
  ;(log/info "config-panel" component-id "//" @data)

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "100%"
   :style {:padding          "5px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data component-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :width "100%"
               :height "100%"
               :style ui-utils/h-wrap
               :gap "10px"
               :children (make-area-config component-id data)]
              [rc/line :src (rc/at) :size "2px"]
              [utils/boolean-config component-id ":brush?" [:brush]]]])


(defn- make-area-display [data subscriptions isAnimationActive?]
  ;(log/info "make-area-display" data "//" subscriptions)

  (let [ret (->> (get-in data [:metadata :fields])
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
                 (into [:<>]))]
    ;(log/info "ret" ret)

    ret))


(defn- component* [& {:keys [data component-id container-id
                             subscriptions isAnimationActive?]
                      :as   params}]
  (let [d (if (empty? data) [] (get data :data))]

    [:> ResponsiveContainer
     [:> AreaChart {:data d}

      (utils/standard-chart-components component-id {})

      (when (ui-utils/resolve-sub subscriptions [:brush]) [:> Brush])

      (make-area-display data subscriptions isAnimationActive?)]]))


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


(def meta-data {:rechart/area-2 {:component component
                                 ;:configurable-component configurable-component
                                 :ports     {:data   :port/sink
                                             :config :port/sink}}})

