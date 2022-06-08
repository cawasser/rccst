(ns bh.rccst.ui-component.atom.chart.sankey-chart
  (:require [bh.rccst.ui-component.atom.chart.utils :as utils]
            [bh.rccst.ui-component.atom.chart.wrapper-2 :as wrapper]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.utils.color :as c]
            [bh.rccst.ui-component.utils.example-data :as data]
            ["recharts" :refer [ResponsiveContainer Sankey Tooltip Layer Rectangle Layer]]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.chart.sankey-chart-2")


(def sample-data
  "the Sankey Chart works best with \"directed acyclic graph data\" so we return the dag-data from utils"
  data/dag-data)

(def dummy-config-data {:nodes {"Visit"            {:include true :fill "#ff0000" :stroke "#ff0000"}
                                "Direct-Favourite" {:include true :fill "#00ff00" :stroke "#00ff00"}
                                "Page-Click"       {:include true :fill "#0000ff" :stroke "#0000ff"}
                                "Detail-Favourite" {:include true :fill "#12a4a4" :stroke "#12a4a4"}
                                "Lost"             {:include true :fill "#ba7b47" :stroke "#ba7b47"}}
                        :links {"Visit"            {:fill "#ff000040"}
                                "Direct-Favourite" {:fill "#00ff0040"}
                                "Page-Click"       {:fill "#0000ff40"}
                                "Detail-Favourite" {:fill "#12a4a440"}
                                "Lost"             {:fill "#ba7b4740"}}})


(defn local-config [data]
  ;(log/info "local-config" @data)

  {:isAnimationActive true
   :tooltip           {:include true}
   :node              {:fill "#77c878" :stroke "#000000"}
   :link              {:stroke "#77c878" :curve 0.5}})


(defn config [component-id data]
  ;(log/info "config" component-id data)
  (merge
    ui-utils/default-pub-sub
    (local-config data)
    {:tab-panel {:value     (keyword component-id "config")
                 :data-path [:containers (keyword component-id) :tab-panel]}}))


(defn config-panel [data component-id]
  ;(log/info "config-panel" data "//" component-id)

  [rc/v-box :src (rc/at)
   :width "400px"
   :height "500px"
   :gap "5px"
   :children [[utils/tooltip component-id]
              [rc/line :size "2px"]
              [rc/v-box :src (rc/at)
               :gap "5px"
               :children [[utils/color-config-text component-id "node fill" [:node :fill] :right-below]
                          [utils/color-config-text component-id "node stroke" [:node :stroke] :right-below]]]
              [rc/line :size "2px"]
              [utils/color-config-text component-id "link stroke" [:link :stroke] :right-below]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[utils/text-config component-id ":curve" [:link :curve]]
                          [utils/slider-config component-id 0 1 0.1 [:link :curve]]]]]])


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
  "build the reagent/react components (as hiccup) needed to draw the `node` parts (rectangles)
  and labels of the diagram.

  ---

  - containerWidth : (number) with of the container, used to determine if the label should be to the left or right of the rectangle
  - fill : (color) color to fill the rectangle
  - stroke : (color) color to use as the outline (stroke) of the rectangle
  - props : (has-map) additional props sent to the reagent/react component by the diagram itself

> See [here](https://cljdoc.org/d/reagent/reagent/1.1.0/doc/tutorials/react-features#hooks)
> for details on how the Reagent/React interop work for this
"
  [containerWidth props]
  (let [{x                           "x"
         y                           "y"
         width                       "width"
         height                      "height"
         index                       "index"
         {name "name" value "value"} "payload"} (js->clj props)
        isOut  (< containerWidth (+ x width 30 6))

        c      (get (:nodes dummy-config-data) name)
        fill   (:fill c)
        stroke (:stroke c)]

    ;(log/info "complex-node" name containerWidth fill stroke props)

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


(defn- make-svg-string [sourceX, targetX,
                        sourceY, targetY,
                        sourceControlX, targetControlX,
                        linkWidth]
  (str "M" sourceX ", " (+ sourceY (/ linkWidth 2))

    "C" sourceControlX ", " (+ sourceY (/ linkWidth 2)) ", "
    targetControlX ", " (+ targetY (/ linkWidth 2)) ", "
    targetX ", " (+ targetY (/ linkWidth 2))

    "L" targetX ", " (- targetY (/ linkWidth 2)) ", "

    "C" targetControlX ", " (- targetY (/ linkWidth 2)) ", "
    sourceControlX ", " (- sourceY (/ linkWidth 2)) ","
    sourceX ", " (- sourceY (/ linkWidth 2))

    "Z"))


(defn color-source->white [index payload]
  (let [color-from (:fill (get (:nodes dummy-config-data) (get-in payload [:source :name])))
        c-from     (-> color-from
                     c/hex->rgba
                     (assoc :a 0.5)
                     c/rgba-map->js-function)
        c-to       (-> color-from
                     c/hex->rgba
                     (assoc :a 0.05)
                     c/rgba-map->js-function)]
    [:defs
     [:linearGradient {:id (str "linkGradient$" index)}
      [:stop {:offset "0%" :stopColor c-from}]
      [:stop {:offset "100%" :stopColor c-to}]]]))


(defn color-white->target [index payload]
  (let [color-to (:fill (get (:nodes dummy-config-data) (get-in payload [:target :name])))
        c-from   (-> color-to
                   c/hex->rgba
                   (assoc :a 0.05)
                   c/rgba-map->js-function)
        c-to     (-> color-to
                   c/hex->rgba
                   (assoc :a 0.5)
                   c/rgba-map->js-function)]
    [:defs
     [:linearGradient {:id (str "linkGradient$" index)}
      [:stop {:offset "0%" :stopColor c-from}]
      [:stop {:offset "100%" :stopColor c-to}]]]))


(defn color-source->target [index payload]
  (let [color-from (:fill (get (:nodes dummy-config-data) (get-in payload [:source :name])))
        color-to   (:fill (get (:nodes dummy-config-data) (get-in payload [:target :name])))
        c-from     (-> color-from
                     c/hex->rgba
                     (assoc :a 0.5)
                     c/rgba-map->js-function)
        c-mid      (-> color-from
                     c/hex->rgba
                     (assoc :a 0.2)
                     c/rgba-map->js-function)
        c-to       (-> color-to
                     c/hex->rgba
                     (assoc :a 0.3)
                     c/rgba-map->js-function)]
    [:defs
     [:linearGradient {:id (str "linkGradient$" index)}
      [:stop {:offset "0%" :stopColor c-from}]
      [:stop {:offset "30%" :stopColor c-mid}]
      [:stop {:offset "100%" :stopColor c-to}]]]))


(defn- complex-link [link-color-fn props]
  (let [{:keys [sourceX, targetX,
                sourceY, targetY,
                sourceControlX, targetControlX,
                linkWidth,
                index, payload]} (js->clj props :keywordize-keys true)]

    ;(log/info "complex-link (props)" (js->clj props :keywordize-keys true))

    (r/as-element
      [:> Layer {:key (str "CustomLink$" index)}

       (link-color-fn index payload)

       [:path {:d           (make-svg-string
                              sourceX, targetX,
                              sourceY, targetY,
                              sourceControlX, targetControlX,
                              linkWidth)
               ;:fill        c-from
               :fill        (str "url(#linkGradient$" index ")")
               :strokeWidth 0}]])))


(defn- component* [& {:keys [data component-id container-id
                             link-color-fn
                             subscriptions]
                      :as   params}]

  ;(log/info "component-star" component-id
    ;"//" data
    ;"//" subscriptions
    ;"//" link-color-fn

  (let [tooltip? (ui-utils/resolve-sub subscriptions [:tooltip :include])
        curve    (ui-utils/resolve-sub subscriptions [:link :curve])]

    [:div "sankey chart"]
    [:> ResponsiveContainer
     [:> Sankey
      {:node          (partial complex-node 700)
       :data          data
       :margin        {:top 20 :bottom 20 :left 20 :right 20}
       :nodeWidth     10
       :nodePadding   60
       :linkCurvature curve
       :iterations    64
       :link          (partial complex-link (or link-color-fn color-white->target))}
      (when tooltip? [:> Tooltip])]]))


(defn component [& {:keys [component-id] :as params}]

  ;(log/info "component" params)

  (let [input-params (assoc params :component-panel wrapper/component-panel
                                   :config config
                                   :local-config local-config
                                   :component* component*)]
    (reduce into [wrapper/base-chart] input-params)))


(def meta-data {:component component
                :sources   {:data :source-type/meta-dag}
                :pubs      []
                :subs      []})



(comment
  (def component-id "sankey-chart-demo")

  ())