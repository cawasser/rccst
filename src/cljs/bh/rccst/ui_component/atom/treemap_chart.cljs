(ns bh.rccst.ui-component.atom.treemap-chart
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]
            [reagent.core :as r]

            ["recharts" :refer [Treemap]]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]))


; region ; configuration params

(def default-ratio (/ 4 3))
(def default-stroke "#ffffff")
(def default-fill "#8884d8")

(defn config [widget-id]
  (merge
    ui-utils/default-pub-sub
    {:tab-panel {:value     (keyword widget-id "config")
                 :data-path [:widgets (keyword widget-id) :tab-panel]}
     :isAnimationActive true
     :ratio  {:include true
              :n 4
              :d 3}
     :stroke  {:color "#ffffff"}
     :fill {:color "#8884d8"}}))

;; endregion


;; region ; config and component panels


(defn- ratio-config [widget-id]
  [rc/h-box :src (rc/at)
   :gap "5px"
   :children [[utils/boolean-config widget-id ":ratio" [:ratio :include]]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[utils/slider-config widget-id 1 10 [:ratio :n]]
                          [utils/slider-config widget-id 1 10 [:ratio :d]]]]]])


(defn- config-panel
  "the panel of configuration controls

  ---

  - config : (atom) holds all the configuration settings made by the user
  "
  [_ widget-id]

  [rc/v-box :src (rc/at)
   :gap "10px"
   :width "100%"
   :height "500px"
   :style {:padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[utils/isAnimationActive widget-id]
              [rc/line :src (rc/at) :size "2px"]
              [rc/v-box :src (rc/at)
               :gap "10px"
               :children [[ratio-config widget-id]
                          [utils/color-config-text widget-id ":stroke" [:stroke :color]]
                          [utils/color-config-text widget-id ":fill" [:fill :color]]]]]])

;; endregion


(def source-code `[:> Treemap
                   {:width 400 :height 400
                    :data @data
                    :dataKey "size"
                    :isAnimationActive true
                    :ratio (/ 4 3)
                    :stroke "#ffffff"
                    :fill "#8884d8"}])


(defn- component-panel
  "the chart to draw, taking cues from the settings of the configuration panel

  ---

  - data : (atom) any data shown by the component's ui
  - widget-id : (string) unique identifier for this widget instance
  "
  [data widget-id]
  (let [;ratio (ui-utils/subscribe-local widget-id [:ratio :include])
        isAnimationActive? (ui-utils/subscribe-local widget-id [:isAnimationActive])
        stroke (ui-utils/subscribe-local widget-id [:stroke :color])
        fill (ui-utils/subscribe-local widget-id [:fill :color])]

    (fn []
      [:> Treemap
       {:width 400 :height 400
        :data @data
        :dataKey "size"
        :isAnimationActive @isAnimationActive?
        :ratio default-ratio
        :stroke @stroke
        :fill @fill}])))


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

(comment
  (def widget-id "treemap-chart-demo")

  ())