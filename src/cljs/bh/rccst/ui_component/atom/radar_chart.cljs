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
      "constructs the configuration data structure for the widget. This is specific to this being a radar-chart component.

      ---

      - widget-id : (string) id of the widget, in this specific case
      "
      [widget-id]
      (-> ui-utils/default-pub-sub
          (merge
            utils/default-config
            {:tab-panel {:value     (keyword widget-id "config")
                         :data-path [:widgets (keyword widget-id) :tab-panel]}
             :radar-mark    {:include true :dataKey :A :stroke "#8884d8" :fill "#8884d8" :fillOpacity 0.6}
             :radar-sally   {:include true :dataKey :B :stroke "#82ca9d" :fill "#82ca9d" :fillOpacity 0.6}})))

(defn- radar-config [widget-id label path position]
       [rc/v-box :src (rc/at)
        :gap "5px"
        :children [[utils/boolean-config widget-id label (conj path :include)]
                   [utils/color-config widget-id ":fill" (conj path :fill) position]
                   [utils/color-config widget-id ":stroke" (conj path :stroke) position]
                   [utils/slider-config widget-id 0 1 0.1 (conj path :fillOpacity)]]])

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
       :children [[utils/non-gridded-chart-components widget-id]
                  [rc/line :src (rc/at) :size "2px"]
                  [rc/h-box :src (rc/at)
                   :gap "10px"
                   :children [[radar-config widget-id "Mark" [:radar-mark] :above-right]
                              [radar-config widget-id "Sally" [:radar-sally] :above-center]]]]])


;(defn- component-panel
;       "the chart to draw, taking cues from the settings of the configuration panel
;
;       ---
;
;       - data : (atom) any data used by the component's ui
;       - widget-id : (string) unique identifier for this specific widget
;       "
;       [data widget-id]
;       (let [container (ui-utils/subscribe-local widget-id [:container])
;             radar-mark? (ui-utils/subscribe-local widget-id [:radar-mark :include])
;             radar-mark-stroke (ui-utils/subscribe-local widget-id [:radar-mark :stroke])
;             radar-mark-fill (ui-utils/subscribe-local widget-id [:radar-mark :fill])
;             radar-mark-fillOpacity (ui-utils/subscribe-local widget-id [:radar-mark :fillOpacity])
;             radar-sally? (ui-utils/subscribe-local widget-id [:radar-sally :include])
;             radar-sally-stroke (ui-utils/subscribe-local widget-id [:radar-sally :stroke])
;             radar-sally-fill (ui-utils/subscribe-local widget-id [:radar-sally :fill])
;             radar-sally-fillOpacity (ui-utils/subscribe-local widget-id [:radar-sally :fillOpacity])]
;
;
;
;
;            (fn []
;                [:> RadarChart {:width 400 :height 400 :cx "50%" :cy "50%" :outerRadius "80%" :data @data}
;                                    [:> PolarGrid]
;                                    [:> PolarAngleAxis {:dataKey :subject}]
;                                    [:> PolarRadiusAxis {:angle "30" :domain [0, 150]}]
;                                    [:> Radar {:name "Mike" :dataKey :A :stroke "#8884d8" :fill "#8884d8" :fillOpacity 0.6}]
;                                    [:> Radar {:name "Sally" :dataKey :B :stroke "#82ca9d" :fill "#82ca9d" :fillOpacity 0.6}]] )))               ;[:> RadarChart {:width 400 :height 400 :cx "50%" :cy "50%" :outerRadius "80%" :data @data}


(defn- component-panel
       "the chart to draw, taking cues from the settings of the configuration panel

       ---

       - data : (atom) any data used by the component's ui
       - widget-id : (string) unique identifier for this specific widget
       "
       [data widget-id]
       (let [container (ui-utils/subscribe-local widget-id [:container])
             radar-mark? (ui-utils/subscribe-local widget-id [:radar-mark :include])
             radar-mark-stroke (ui-utils/subscribe-local widget-id [:radar-mark :stroke])
             radar-mark-fill (ui-utils/subscribe-local widget-id [:radar-mark :fill])
             radar-mark-fillOpacity (ui-utils/subscribe-local widget-id [:radar-mark :fillOpacity])
             radar-sally? (ui-utils/subscribe-local widget-id [:radar-sally :include])
             radar-sally-stroke (ui-utils/subscribe-local widget-id [:radar-sally :stroke])
             radar-sally-fill (ui-utils/subscribe-local widget-id [:radar-sally :fill])
             radar-sally-fillOpacity (ui-utils/subscribe-local widget-id [:radar-sally :fillOpacity])]




            (fn []
                [:> RadarChart {:width 400 :height 400 :outerRadius "75%" :data @data}
                 (utils/non-gridded-chart-components widget-id)

                 [:> PolarGrid]
                 [:> PolarAngleAxis {:dataKey :subject}]
                 [:> PolarRadiusAxis {:angle "30" :domain [0, 150]}]
                 (when @radar-mark? [:> Radar {:name "Mark"
                                               :dataKey :A
                                               :fill @radar-mark-fill
                                               :stroke @radar-mark-stroke
                                               :fillOpacity @radar-mark-fillOpacity}])
                 (when @radar-sally? [:> Radar {:name "Sally"
                                                :dataKey :B
                                                :fill @radar-sally-fill
                                                :stroke @radar-sally-stroke
                                                :fillOpacity @radar-sally-fillOpacity}])])))

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
                      (ui-utils/init-widget @id (config @id))
                      (ui-utils/dispatch-local @id [:container] container-id))

                ;(log/info "component" @id)

                [c/configurable-chart
                 :data data
                 :id @id
                 :config-panel config-panel
                 :component component-panel]))))

;(defn component
;
;      [data widget]
;
;      (fn []
;          [c/chart
;                 [:div
;                  [:> RadarChart {:width 400 :height 400 :cx "50%" :cy "50%" :outerRadius "80%" :data @data}
;                   [:> PolarGrid]
;                   [:> PolarAngleAxis {:dataKey :subject}]
;                   [:> PolarRadiusAxis]
;                   [:> Radar {:name "Mike" :dataKey :A :stroke "#8884d8" :fill "#8884d8" :fillOpacity 0.6}]
;                   [:> Radar {:name "Sally" :dataKey :B :stroke "#82ca9d" :fill "#82ca9d" :fillOpacity 0.6}]]]]
;      ))


