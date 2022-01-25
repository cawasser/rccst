(ns bh.rccst.views.catalog.example.bar-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [re-com.core :as rc]

            [bh.rccst.views.catalog.utils :as bcu]
            [bh.rccst.views.catalog.example.chart.utils :as utils]
            ["recharts" :refer [BarChart Bar Brush]]))


;; region ; configuration params

(def config (r/atom (-> utils/default-config
                      (merge
                        {:brush   false
                         :bar-uv  {:include true :fill "#ff0000"}
                         :bar-pv  {:include true :fill "#00ff00"}
                         :bar-amt {:include false :fill "#0000ff"}
                         :bar-d   {:include false :fill "#0f0f0f"}})
                      (assoc-in [:x-axis :dataKey] :name))))


;; endregion


;; region ; config and component panels

(defn- bar-config [config label path position]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config config label (conj path :include)]
              [utils/color-config config ":fill" (conj path :fill) position]
              [utils/text-config config ":stackId" (conj path :stackId)]]])


(defn- config-panel
  "the panel of configuration controls

  ---

  - config : (atom) holds all the configuration settings made by the user
  "
  [data config]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/standard-chart-config data config]
              [rc/line :src (rc/at) :size "2px"]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[bar-config config "bar (uv)" [:bar-uv] :above-right]
                          [bar-config config "bar (pv)" [:bar-pv] :above-center]
                          [bar-config config "bar (amt)" [:bar-amt] :above-center]
                          [bar-config config "bar (d)" [:bar-d] :above-left]]]
              [rc/line :src (rc/at) :size "2px"]
              [utils/boolean-config config ":brush?" [:brush]]]])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data used by the component's ui
  - config : (atom) configuration settings made by the user using the config-panel
  "
  [data config]
  (let [bar-uv? (reaction (get-in @config [:bar-uv :include]))
        bar-pv? (reaction (get-in @config [:bar-pv :include]))
        bar-amt? (reaction (get-in @config [:bar-amt :include]))
        bar-d? (reaction (get-in @config [:bar-d :include]))
        isAnimationActive? (reaction (:isAnimationActive @config))
        brush? (reaction (:brush @config))]

    (fn []
      [:> BarChart {:width 400 :height 400 :data @data}

       (utils/standard-chart-components config)

       (when @brush? [:> Brush])

       (when @bar-uv? [:> Bar (merge {:type              "monotone" :dataKey :uv
                                      :isAnimationActive @isAnimationActive?
                                      :fill              (get-in @config [:bar-uv :fill])}
                                (when (not (empty? (get-in @config [:bar-uv :stackId])))
                                  {:stackId (get-in @config [:bar-uv :stackId])}))])

       (when @bar-pv? [:> Bar (merge {:type              "monotone" :dataKey :pv
                                      :isAnimationActive @isAnimationActive?
                                      :fill              (get-in @config [:bar-pv :fill])}
                                (when (not (empty? (get-in @config [:bar-pv :stackId])))
                                  {:stackId (get-in @config [:bar-pv :stackId])}))])

       (when @bar-amt? [:> Bar (merge {:type              "monotone" :dataKey :amt
                                       :isAnimationActive @isAnimationActive?
                                       :fill              (get-in @config [:bar-amt :fill])}
                                 (when (not (empty? (get-in @config [:bar-amt :stackId])))
                                   {:stackId (get-in @config [:bar-amt :stackId])}))])

       (when @bar-d? [:> Bar (merge {:type              "monotone" :dataKey :d
                                     :isAnimationActive @isAnimationActive?
                                     :fill              (get-in @config [:bar-d :fill])}
                               (when (not (empty? (get-in @config [:bar-d :stackId])))
                                 {:stackId (get-in @config [:bar-d :stackId])}))])])))

;; endregion


(comment
  (def config {:bar-d {:stackId "a"}})

  (merge {:type              "monotone" :dataKey :d
          :isAnimationActive @isAnimationActive?
          :fill              (get-in @config [:bar-d :fill])}
    (when (not (empty? (get-in config [:bar-d :stackId])))
      {:stackId (get-in @config [:bar-d :stackId])}))

  ())

(defn example []
  (utils/init-config-panel "bar-chart-demo")

  (let [data (r/atom (mapv (fn [d] (assoc d :d (rand-int 5000))) utils/tabular-data))]
    (bcu/configurable-demo "Bar Chart"
      "Bar Charts (this would be really cool with support for changing options live)"
      [:bar-chart-demo/config :bar-chart-demo/data :bar-chart-demo/tab-panel :bar-chart-demo/selected-tab]
      [utils/tabular-data-panel data]
      [config-panel data config]
      [component-panel data config]
      '[:> BarChart {:width 400 :height 400 :data @data}
        [:> CartesianGrid {:strokeDasharray "3 3"}]
        [:> XAxis {:dataKey "title"}]
        [:> YAxis]
        [:> Tooltip]
        [:> Legend {:layout "horizontal"}]
        [:> Bar {:type "monotone" :dataKey "uv" :fill "#8884d8"}]
        [:> Bar {:type "monotone" :dataKey "pv" :fill "#82ca9d"}]])))

