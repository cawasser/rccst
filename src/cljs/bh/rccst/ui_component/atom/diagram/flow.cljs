(ns bh.rccst.ui-component.atom.diagram.flow
  (:require [bh.rccst.ui-component.atom.card.flippable-card :as card]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            ["react-flow-renderer" :refer (ReactFlowProvider MiniMap Controls Handle) :default ReactFlow]))


(defn diagram-cell [x y]
  {:x (+ 10 (* x 200)) :y (+ 10 (* y 200))})


(def sample-data
  (r/atom [{:id        "viirs-5"
            :el-type   :node
            :type      "globe"
            :data      {:sensor "viirs-5"}
            ;:dragHandle "drag-handle"
            :draggable false
            :position  (diagram-cell 0 1)}

           {:id        "abi-meso-11"
            :el-type   :node
            :type      "globe"
            :data      {:sensor "abi-meso-11"}
            ;:dragHandle "drag-handle"
            :draggable false
            :position  (diagram-cell 0 0)}

           {:id        "goes-east"
            :el-type   :node
            :type      "platform"
            :data      {:label "GOES East"
                        :image "/images/icons/Weather-Satellite-PNG-Clipart.png"}
            ;:dragHandle "drag-handle"
            :draggable false
            :position  (diagram-cell 1 0)}

           {:id        "central"
            :el-type   :node
            :type      "downlink-terminal"
            :data      {:label "Wallops"
                        :image "/images/icons/downlink-terminal.png"}
            :draggable false
            :position  (diagram-cell 2 0)}

           {:id        "washington"
            :el-type   :node
            :type      "processing-center"
            :data      {:label "NSOF Suitland"
                        :image "/images/icons/processing-center.png"}
            :draggable false
            :position  (diagram-cell 3 0)}

           {:id        "noaa-xx"
            :el-type   :node
            :type      "platform"
            :data      {:label "NOAA XX"
                        :image "/images/icons/Weather-Satellite-PNG-Clipart.png"}
            :draggable false
            :position  (diagram-cell 1 1)}

           {:id        "mountain"
            :el-type   :node
            :type      "downlink-terminal"
            :data      {:label "Svalbaard/McMurdo"
                        :image "/images/icons/downlink-terminal.png"}
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


(def card-style {:width "100px" :height "150px"})
(def handle-style {:width "8px" :height "8px" :borderRadius "50%"})


(defn- node [d]
  (let [data  (js->clj d)
        label (get-in data ["data" "label"])
        id    (get data "id")]
    (log/info "node" data)
    (r/as-element
      [:div#node-card {:style card-style}
       [card/card
        :style card-style
        :front [:div {:style card-style}
                label]
        :back [:div {:style card-style}
               "back"]]
       [:> Handle {:id    (str id "-out") :type "source" :position "right"
                   :style handle-style}]
       [:> Handle {:id    (str id "-in") :type "target" :position "left"
                   :style handle-style}]])))


(defn component [& {:keys [data component-id container-id]}]
  [:div {:style {:width "1000px" :height "500px"}}
   [:> ReactFlowProvider
    [:> ReactFlow {:className        component-id
                   :elements         @data
                   :nodeTypes        {"globe"             node
                                      "platform"          node
                                      "downlink-terminal" node
                                      "processing-center" node}
                   :edgeTypes        {}
                   :zoomOnScroll     false
                   :preventScrolling false
                   :onConnect        #()}
     [:> MiniMap]
     [:> Controls]]]])