(ns bh.rccst.ui-component.atom.chart.line-chart-2
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
            ["recharts" :refer [ResponsiveContainer LineChart Line Brush]]))


(def source-code '[])
(def sample-data example-data/meta-tabular-data)


(defn local-config [data]
  (merge
    {:brush false}
    (->> (get-in @data [:metadata :fields])
         (filter (fn [[k v]] (= :number v)))
         keys
         (map-indexed (fn [idx a]
                        ;(log/info "line color" idx a (ui-utils/get-color idx))
                        {a {:include true
                            :stroke  (color/get-color idx)
                            :fill    (color/get-color idx)}}))
         (into {}))))


(defn- config [chart-id data]
  (-> ui-utils/default-pub-sub
      (merge
        utils/default-config
        {:type      "line-chart"
         :tab-panel {:value     (keyword chart-id "config")
                     :data-path [:widgets (keyword chart-id) :tab-panel]}}
        (local-config data))
      (assoc-in [:x-axis :dataKey] (get-in @data [:metadata :id]))))


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


(defn config-panel [data chart-id]

  ;(log/info "config-panel" data chart-id)

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

  (log/info "make-line-display" data)
  (->> (get-in data [:metadata :fields])
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


(defn- component* [& {:keys [data component-id container-id
                             subscriptions isAnimationActive?]
                      :as   params}]

  (let [d (if (empty? data) [] (get data :data))]

    (log/info "component*" data "//" d)
    [:> ResponsiveContainer
     [:> LineChart {:data d}

      (utils/standard-chart-components component-id {})

      (when (ui-utils/resolve-sub subscriptions [:brush]) [:> Brush])

      (make-line-display component-id data subscriptions isAnimationActive?)]]))


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


(def meta-data {:rechart/line-2 {:component component
                                 ;:configurable-component configurable-component
                                 :ports     {:data   :port/sink
                                             :config :port/sink}}})