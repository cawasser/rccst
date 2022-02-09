(ns bh.rccst.ui-component.atom.sankey-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper :as c]
            [bh.rccst.ui-component.utils :as ui-utils]

            ["recharts" :refer [Sankey Tooltip Layer Rectangle]]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(defn config [widget-id]
  {:tab-panel {:value     (keyword widget-id "config")
               :data-path [:widgets (keyword widget-id) :tab-panel]}
   :tooltip   {:include true}
   :node      {:fill "#77c878" :stroke "#000000"}
   :link      {:stroke "#77c878" :curve 0.5}})


(defn config-panel [_ widget-id]
  [rc/v-box :src (rc/at)
   :height "500px"
   :gap "5px"
   :children [[utils/tooltip widget-id]
              [rc/line :size "2px"]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[utils/color-config-text widget-id "node fill" [:node :fill] :right-below]
                          [utils/color-config-text widget-id "node stroke" [:node :stroke] :right-below]]]
              [rc/line :size "2px"]
              [utils/color-config-text widget-id "link stroke" [:link :stroke] :right-below]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[utils/text-config widget-id ":curve" [:link :curve]]
                          [utils/slider-config widget-id 0 1 0.1 [:link :curve]]]]]])

;; region ; component

(def source-code '[:> Sankey
                   {:width         500 :height 400
                    :node          (partial complex-node 500 @fill)
                    :data          @data
                    :margin        {:top 20 :bottom 20 :left 20 :right 20}
                    :nodeWidth     10
                    :nodePadding   60
                    :linkCurvature @curve
                    :iterations    64
                    :link          {:stroke @stroke}}
                   (when @tooltip? [:> Tooltip])])


(defn- complex-node
  "
> See [here](https://cljdoc.org/d/reagent/reagent/1.1.0/doc/tutorials/react-features#hooks)
> for details on how the Reagent/React interop work for this
"
  [containerWidth fill stroke props]
  (let [{x                           "x"
         y                           "y"
         width                       "width"
         height                      "height"
         index                       "index"
         {name "name" value "value"} "payload"} (js->clj props)
        isOut (< containerWidth (+ x width 30 6))]
    (r/as-element
      [:> Layer {:key (str "CustomNode$" index)}
       [:> Rectangle {:x x :y y :width width :height height :fill fill :stroke stroke}]
       [:text {:textAnchor (if isOut "end" "start")
               :x          (if isOut (- x 6) (+ x width 6))
               :y          (+ y (/ height 2))
               :fontSize   14
               :stroke     "#333"}
        name]
       [:text {:textAnchor    (if isOut "end" "start")
               :x             (if isOut (- x 6) (+ x width 6))
               :y             (+ y 13 (/ height 2))
               :fontSize      12
               :stroke        "#333"
               :strokeOpacity 0.5}
        (str value "k")]])))


(defn component [data widget-id]
  (let [tooltip? (ui-utils/subscribe-local widget-id [:tooltip :include])
        node-fill (ui-utils/subscribe-local widget-id [:node :fill])
        node-stroke (ui-utils/subscribe-local widget-id [:node :stroke])
        link-stroke (ui-utils/subscribe-local widget-id [:link :stroke])
        curve (ui-utils/subscribe-local widget-id [:link :curve])]

    (fn []
      [c/chart
       [:> Sankey
        {:width         500 :height 400
         :node          (partial complex-node 500 @node-fill @node-stroke)
         :data          @data
         :margin        {:top 20 :bottom 20 :left 20 :right 20}
         :nodeWidth     10
         :nodePadding   60
         :linkCurvature @curve
         :iterations    64
         :link          {:stroke @link-stroke}}
        (when @tooltip? [:> Tooltip])]])))


;; endregion