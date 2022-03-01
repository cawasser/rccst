(ns bh.rccst.ui-component.atom.diagram.flow
  (:require [bh.rccst.ui-component.atom.card.flippable-card :as card]
            [bh.rccst.ui-component.atom.chart.area-chart :as area-chart]
            [bh.rccst.ui-component.atom.chart.bar-chart :as bar-chart]
            [bh.rccst.ui-component.atom.chart.line-chart :as line-chart]
            [bh.rccst.ui-component.atom.oz.bar-chart :as oz-bar-chart]
            [bh.rccst.ui-component.atom.chart.colored-pie-chart :as pie-chart]
            [bh.rccst.ui-component.utils :as ui-utils]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            ["react-flow-renderer" :refer (ReactFlowProvider MiniMap Controls Handle) :default ReactFlow]))


(defn diagram-cell [x y]
  {:x (+ 10 (* x 200)) :y (+ 10 (* y 200))})


(def sample-data
  (r/atom [{:id        "viirs-5"
            :el-type   :node
            :type      "globe"
            :data      {:label "viirs-5"
                        :chart  pie-chart/component}
            :draggable false
            :position  (diagram-cell 0 1)}

           {:id        "abi-meso-11"
            :el-type   :node
            :type      "globe"
            :data      {:label "abi-meso-11"
                        :chart  pie-chart/component}
            :draggable false
            :position  (diagram-cell 0 0)}

           {:id        "goes-east"
            :el-type   :node
            :type      "platform"
            :data      {:label "GOES East"
                        :image "/images/icons/Weather-Satellite-PNG-Clipart.png"
                        :chart area-chart/component}
            :draggable false
            :position  (diagram-cell 1 0)}

           {:id        "central"
            :el-type   :node
            :type      "downlink-terminal"
            :data      {:label "Wallops"
                        :image "/images/icons/downlink-terminal.png"
                        :chart bar-chart/component}
            :draggable false
            :position  (diagram-cell 2 0)}

           {:id        "washington"
            :el-type   :node
            :type      "processing-center"
            :data      {:label "NSOF Suitland"
                        :image "/images/icons/processing-center.png"
                        :chart line-chart/component}
            :draggable false
            :position  (diagram-cell 3 0)}

           {:id        "noaa-xx"
            :el-type   :node
            :type      "platform"
            :data      {:label "NOAA XX"
                        :image "/images/icons/Weather-Satellite-PNG-Clipart.png"
                        :chart area-chart/component}
            :draggable false
            :position  (diagram-cell 1 1)}

           {:id        "mountain"
            :el-type   :node
            :type      "downlink-terminal"
            :data      {:label "Svalbaard/McMurdo"
                        :image "/images/icons/downlink-terminal.png"
                        :chart bar-chart/component}
            :draggable false
            :position  (diagram-cell 2 1)}

           ; edges
           {:id    "11-n" :el-type :edge :source "abi-meso-11" :target "goes-east"
            :style {:stroke-width 20 :stroke :gray} :animated true}
           {:id    "v5-n" :el-type :edge :source "viirs-5" :target "noaa-xx"
            :style {:stroke-width 20 :stroke :gray} :animated true}
           {:id    "e-c" :el-type :edge :source "goes-east" :target "central"
            :style {:stroke-width 50 :stroke :orange} :animated true}
           {:id    "c-w" :el-type :edge :source "central" :target "washington"
            :style {:stroke-width 25 :stroke "#f00"} :animated true}
           {:id    "n-m" :el-type :edge :source "noaa-xx" :target "mountain"
            :style {:stroke-width 30 :stroke :lightgreen} :animated true}
           {:id    "m-w" :el-type :edge :source "mountain" :target "washington"
            :style {:stroke-width 5} :animated true}]))


(def data-sources {"GOES East"         area-chart/sample-data
                   "NOAA XX"           area-chart/sample-data
                   "Wallops"           bar-chart/sample-data
                   "Svalbaard/McMurdo" bar-chart/sample-data
                   "NSOF Suitland"     line-chart/sample-data
                   "viirs-5"           pie-chart/sample-data
                   "abi-meso-11"       pie-chart/sample-data})


(def source-code '[:> ReactFlowProvider
                   [:> ReactFlow {:className        component-id
                                  :elements         @data
                                  :nodeTypes        {}
                                  :edgeTypes        {}
                                  :zoomOnScroll     false
                                  :preventScrolling false
                                  :onConnect        #()}
                    [:> MiniMap]
                    [:> Controls]]])


;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; STYLES
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region
(def default-background "#dcdcdc")
(def default-color "#FF")
(def card-style {:width "100px" :height "150px"})
(def handle-style {:width "8px" :height "8px" :borderRadius "50%"})
(def image-style {:width        "75px" :height "75px"
                  :display      :block
                  :margin-left  :auto
                  :margin-right :auto})
(def label-style {:margin-top "10px" :margin-bottom "5px"
                  :text-align :center})
(def node-style-globe {:width         "100px" :height "100px"
                       :border-radius "50%"
                       :overflow      "hidden"})
(def node-style-square {:width           "100px" :height "150px"
                        :overflow        "hidden"
                        :background      default-background
                        :color           default-color
                        :display         :flex
                        :flex-direction  :column
                        :justify-content :center
                        :align-items     :center})
;; endregion


(defn- globe-node [props & {:keys [data label image chart]}]
  [card/card
   :style card-style
   :front [:div#entity-card {:style (merge node-style-square props)}
           [:img {:style (merge image-style
                           {:background-color default-background}
                           props)
                  :src   image}]
           [:div.subtitle.is-3 {:style label-style} label]]
   :back [:div {:style (merge node-style-square props)}
          [chart
           :data data
           :component-id (ui-utils/path->keyword "diagram" label "chart")
           :container-id (ui-utils/path->keyword "diagram" label)
           :ui {:legend false :tooltip false :label false}]]])


(defn- platform-node [props & {:keys [data label image chart]}]
  [card/card
   :style card-style
   :front [:div#entity-card {:style (merge node-style-square props)}
           [:img {:style (merge image-style
                           {:background-color default-background}
                           props)
                  :src   image}]
           [:div.subtitle.is-3 {:style label-style} label]]
   :back [:div {:style (merge node-style-square props)}
          [chart
           :data data
           :component-id (ui-utils/path->keyword "diagram" label "chart")
           :container-id (ui-utils/path->keyword "diagram" label)
           :ui {:x-axis false :y-axis false
                :legend false :tooltip false}]]])


(comment
  (def data area-chart/sample-data)

  (assoc @data
    :data
    (map (fn [r]
           (dissoc r :pv :tv :d))
      (:data @data)))
  ())



(defn- downlink-node [props & {:keys [data label image chart]}]
  [card/card
   :style card-style
   :front [:div#entity-card {:style (merge node-style-square props)}
           [:img {:style (merge image-style
                           {:background-color default-background}
                           props)
                  :src   image}]
           [:div.subtitle.is-3 {:style label-style} label]]
   :back [:div {:style (merge node-style-square props)}
          [chart
           :data data
           :component-id (ui-utils/path->keyword "diagram" label "chart")
           :container-id (ui-utils/path->keyword "diagram" label)
           :ui {:grid   false :x-axis false :y-axis false
                :legend false :tooltip false}]]])


(defn- processing-node [props & {:keys [data label image chart]}]
  [card/card
   :style card-style
   :front [:div#entity-card {:style (merge node-style-square props)}
           [:img {:style (merge image-style
                           {:background-color default-background}
                           props)
                  :src   image}]
           [:div.subtitle.is-3 {:style label-style} label]]
   :back [:div {:style (merge node-style-square props)}
          [chart
           :data data
           :component-id (ui-utils/path->keyword "diagram" label "chart")
           :container-id (ui-utils/path->keyword "diagram" label)
           :ui {:grid   false :x-axis false :y-axis false
                :legend false :tooltip false}]]])


(defn- node [type sources d]
  (let [data   (js->clj d)
        label  (get-in data ["data" "label"])
        image  (get-in data ["data" "image"])
        chart  (get-in data ["data" "chart"])
        id     (get data "id")
        source (get sources label)]

    ;(log/info "node" id label (if source @source {}))

    (r/as-element
      [:div#node-card {:style (merge card-style {:margin 0})}
       [type {:margin 0}
        :data source
        :label label
        :image image
        :chart chart]
       [:> Handle {:id    (str id "-out") :type "source" :position "right"
                   :style handle-style}]
       [:> Handle {:id    (str id "-in") :type "target" :position "left"
                   :style handle-style}]])))


(defn component [& {:keys [data component-id container-id]}]
  [:div {:style {:width "1000px" :height "500px"}}
   [:> ReactFlowProvider
    [:> ReactFlow {:className        component-id
                   :elements         @data
                   :nodeTypes        {"globe"             (partial node globe-node data-sources)
                                      "platform"          (partial node platform-node data-sources)
                                      "downlink-terminal" (partial node downlink-node data-sources)
                                      "processing-center" (partial node processing-node data-sources)}
                   :edgeTypes        {}
                   :zoomOnScroll     false
                   :preventScrolling false
                   :onConnect        #()}
     [:> MiniMap]
     [:> Controls]]]])

