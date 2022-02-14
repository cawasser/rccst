(ns bh.rccst.ui-component.atom.radar-chart
  (:require [taoensso.timbre :as log]
            ["recharts" :refer [RadarChart PolarGrid PolarAngleAxis PolarRadiusAxis Radar]]
            [re-com.core :as rc]
            [reagent.core :as r]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]))

(def sample-data (r/atom [{:subject "Math" :A 120 :B 110 :fullMark 150}
           {:subject "Chinese" :A 98 :B 130 :fullMark 150}
           {:subject "English" :A 100 :B 110 :fullMark 150}
           {:subject "History" :A 77 :B 81 :fullMark 150}
           {:subject "Economics" :A 99 :B 140 :fullMark 150}
           {:subject "Literature" :A 98 :B 105 :fullMark 150}]))

(def source-code "dummy Radar Chart Code")


(defn config
      "constructs the configuration data structure for the widget. This is specific to this being a bar-chart component.

      ---

      - widget-id : (string) id of the widget, in this specific case
      "
      [widget-id]
      (-> utils/default-config
          (merge
            {:tab-panel {:value     (keyword widget-id "config")
                         :data-path [:widgets (keyword widget-id) :tab-panel]}
             :brush     false
             :bar-uv    {:include true :fill "#ff0000" :stackId ""}
             :bar-pv    {:include true :fill "#00ff00" :stackId ""}
             :bar-amt   {:include false :fill "#0000ff" :stackId ""}
             :bar-d     {:include false :fill "#0f0f0f" :stackId ""}})
          (assoc-in [:x-axis :dataKey] :name)))


(defn- bar-config [widget-id label path position]
       [rc/v-box :src (rc/at)
        :gap "5px"
        :children [[utils/boolean-config widget-id label (conj path :include)]
                   [utils/color-config widget-id ":fill" (conj path :fill) position]
                   [utils/text-config widget-id ":stackId" (conj path :stackId)]]])


(defn config-panel
      "the panel of configuration controls

      ---

      - data : (atom) data to display (may be used by the standard configuration components for thins like axes, etc.\n  - config : (atom) holds all the configuration settings made by the user
      "
      [data widget-id]

      [rc/v-box :src (rc/at)
       :gap "10px"
       :width "100%"
       :style {:padding          "15px"
               :border-top       "1px solid #DDD"
               :background-color "#f7f7f7"}
       :children [[utils/standard-chart-config data widget-id]
                  [rc/line :src (rc/at) :size "2px"]
                  [rc/h-box :src (rc/at)
                   :gap "10px"
                   :children [[bar-config widget-id "bar (uv)" [:bar-uv] :above-right]
                              [bar-config widget-id "bar (pv)" [:bar-pv] :above-center]
                              [bar-config widget-id "bar (amt)" [:bar-amt] :above-center]
                              [bar-config widget-id "bar (d)" [:bar-d] :above-left]]]
                  [rc/line :src (rc/at) :size "2px"]
                  [utils/boolean-config widget-id ":brush?" [:brush]]]])


(defn component

      [data widget]

      (fn []
          [c/chart
                 [:div
                  [:> RadarChart {:width 400 :height 400 :cx "50%" :cy "50%" :outerRadius "80%" :data @data}
                   [:> PolarGrid]
                   [:> PolarAngleAxis {:dataKey :subject}]
                   [:> PolarRadiusAxis]
                   [:> Radar {:name "Mike" :dataKey :A :stroke "#8884d8" :fill "#8884d8" :fillOpacity 0.6}]
                   [:> Radar {:name "Sally" :dataKey :B :stroke "#82ca9d" :fill "#82ca9d" :fillOpacity 0.6}]]]]
      ))


;comment (
;
;         (defn component
;               "the chart to draw, taking cues from the settings of the configuration panel
;
;               ---
;
;               - data : (atom) any data used by the component's ui
;               - widget-id : (string) unique identifier for this specific widget
;               "
;               [data widget-id]
;               (let [bar-uv? (ui-utils/subscribe-local widget-id [:bar-uv :include])
;                     bar-uv-fill (ui-utils/subscribe-local widget-id [:bar-uv :fill])
;                     bar-uv-stackId (ui-utils/subscribe-local widget-id [:bar-uv :stackId])
;                     bar-pv? (ui-utils/subscribe-local widget-id [:bar-pv :include])
;                     bar-pv-fill (ui-utils/subscribe-local widget-id [:bar-pv :fill])
;                     bar-pv-stackId (ui-utils/subscribe-local widget-id [:bar-pv :stackId])
;                     bar-amt? (ui-utils/subscribe-local widget-id [:bar-amt :include])
;                     bar-amt-fill (ui-utils/subscribe-local widget-id [:bar-amt :fill])
;                     bar-amt-stackId (ui-utils/subscribe-local widget-id [:bar-amt :stackId])
;                     bar-d? (ui-utils/subscribe-local widget-id [:bar-d :include])
;                     bar-d-fill (ui-utils/subscribe-local widget-id [:bar-d :fill])
;                     bar-d-stackId (ui-utils/subscribe-local widget-id [:bar-d :stackId])
;                     isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
;                     brush? (ui-utils/subscribe-local widget-id [:brush])]
;
;                    (fn []
;                        [c/chart
;                         [:> BarChart {:width 400 :height 400 :data @data}
;
;                          (utils/standard-chart-components widget-id)
;
;                          (when @brush? [:> Brush])
;
;                          (when @bar-uv? [:> Bar (merge {:type              "monotone" :dataKey :uv
;                                                         :isAnimationActive @isAnimationActive?
;                                                         :fill              @bar-uv-fill}
;                                                        (when (seq @bar-uv-stackId) {:stackId @bar-uv-stackId}))])
;
;                          (when @bar-pv? [:> Bar (merge {:type              "monotone" :dataKey :pv
;                                                         :isAnimationActive @isAnimationActive?
;                                                         :fill              @bar-pv-fill}
;                                                        (when (seq @bar-pv-stackId) {:stackId @bar-pv-stackId}))])
;
;                          (when @bar-amt? [:> Bar (merge {:type              "monotone" :dataKey :amt
;                                                          :isAnimationActive @isAnimationActive?
;                                                          :fill              @bar-amt-fill}
;                                                         (when (seq @bar-amt-stackId) {:stackId @bar-amt-stackId}))])
;
;                          (when @bar-d? [:> Bar (merge {:type              "monotone" :dataKey :d
;                                                        :isAnimationActive @isAnimationActive?
;                                                        :fill              @bar-d-fill}
;                                                       (when (seq @bar-d-stackId) {:stackId @bar-d-stackId}))])]])))
;
;
;          )
;
