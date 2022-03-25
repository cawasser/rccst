(ns bh.rccst.ui-component.atom.diagram.editable-digraph
  (:require [reagent.core :as r]
            [taoensso.timbre :as log]
            ["react" :as react]
            ["react-flow-renderer" :refer (ReactFlowProvider MiniMap Controls
                                            Handle MarkerType
                                            Background
                                            applyNodeChanges
                                            applyEdgeChanges
                                            useNodesState
                                            useEdgesState) :default ReactFlow]))


(log/info "bh.rccst.ui-component.atom.diagram.editable-digraph")


(declare node)


(def sample-data
  (r/atom
    {:nodes [{:id       ":ui/targets"
              ;:type     ":ui/component"
              :data     {:label   ":ui/targets"
                         :inputs  []
                         :outputs []}
              :position {:x 0 :y 100}}
             {:id       ":topic/target-data"
              ;:type     ":source/remote"
              :data     {:label   ":topic/target-data"
                         :inputs  []
                         :outputs []}
              :position {:x 100 :y 0}}
             {:id       ":topic/selected-targets"
              ;:type     ":source/local"
              :data     {:label   ":topic/selected-targets"
                         :inputs  []
                         :outputs []}
              :position {:x 0 :y 200}}]

     :edges [{:id        "target-data->targets"
              :source    ":topic/target-data" :target ":ui/targets"
              :style     {:strokeWidth 2 :stroke :orange}
              :markerEnd {:type (.-ArrowClosed MarkerType)}}
             {:id        "targets->selected-targets"
              :source    ":ui/targets" :target ":topic/selected-targets"
              :style     {:strokeWidth 2 :stroke :blue}
              :markerEnd {:type (.-ArrowClosed MarkerType)}}]}))


(def source-code '[])


(def handle-style {:width "8px" :height "8px" :borderRadius "50%"})
(def default-node-style {:padding      "3px" :max-width "180px"
                         :borderRadius "5px" :margin :auto
                         :background   :white :color :black})
(def node-style {:ui/component  {:background :green :color :white}
                 :source/remote {:background :orange :color :black}
                 :source/local  {:background :blue :color :white}
                 :source/fn     {:background :pink :color :black}})


(defn- input-output-handles
  "

  NOTE: the inputs (values in the hash-map) are STRINGS!
  "

  [label inputs outputs]
  [:<>
   ; add the input handles
   (doall
     (->> inputs
       (map-indexed (fn [idx [target ports]]
                      (let [[source-port target-port] ports]
                        [:> Handle {:id    target-port :type "target" :position "top"
                                    :style (merge handle-style {:left (+ 20 (* 10 idx))})}])))
       (into [:<>])))

   ; add the output handles
   (doall
     (->> outputs
       (map-indexed (fn [idx [target ports]]
                      (let [[source-port target-port] ports]
                        [:> Handle {:id    source-port :type "source" :position "bottom"
                                    :style (merge handle-style {:left (+ 20 (* 10 idx))})}])))
       (into [:<>])))])





(defn custom-node
  "build a custom node for the flow diagram, this time for :ui/component, so
  green, since this is a 'view', and one Handle for each input (along the top)
  and output (along the bottom)
  "
  [component-id type d]
  (let [data    (js->clj d :keywordize-keys true)
        label   (get-in data [:data :label])
        inputs  (get-in data [:data :inputs])
        outputs (get-in data [:data :outputs])
        style   (merge default-node-style (type node-style))]

    (log/info "custom-node" label data "///" inputs "///" outputs)

    (r/as-element
      [:div {:style style}                                  ;:on-click #(open-details component-id label)}
       [:h5 {:style (merge {:textAlign :center} style)} label]
       (input-output-handles label inputs outputs)])))


(defn- source-panel [])


(defn- find-node-by-id [nodes id]
  (first (filter #(= id (:id %)) nodes)))



(defn- flow* [& {:keys [component-id nodes edges on-change-nodes on-change-edges
                        zoom-on-scroll preventScrolling connectFn]}]
  [:> ReactFlow {:nodes               nodes
                 :edges               edges
                 ;:nodesDraggable      true
                 ;:nodesConnectable    true
                 ;:nodeTypes           {}
                 ;:edgeTypes           {}
                 :onNodesChange       on-change-nodes
                 :onEdgesChange       on-change-edges
                 :zoomOnScroll        (or zoom-on-scroll false)
                 :preventScrolling    (or preventScrolling false)
                 :onConnect           (or connectFn #())
                 :fitView             true
                 :attributionPosition "top-right"}
   [:> MiniMap]
   [:> Background]
   [:> Controls]])


(defn- editable-flow [& {:keys [component-id nodes edges
                                zoom-on-scroll preventScrolling connectFn]}]
  (let [[ns set-nodes on-change-nodes] (useNodesState (clj->js nodes))
        [es set-edges on-change-edges] (useEdgesState (clj->js edges))]

    ;(log/info "editable-flow"
    ;  "//" ns
    ;  "//" set-nodes
    ;  "//" on-change-nodes)

    [flow*
     :component-id component-id
     :nodes ns  :edges es
     :on-change-nodes on-change-nodes
     :on-change-edges on-change-edges
     :connectFn connectFn
     :zoom-on-scroll zoom-on-scroll
     :preventScrolling preventScrolling]))


(defn component [& {:keys [data node-types edge-types connectFn
                           zoom-on-scroll preventScrolling
                           component-id container-id]}]

  [:div {:style {:width "1000px" :height "700px"}}
   [:f> editable-flow
    :component-id component-id
    :nodes (:nodes @data)
    :edges (:edges @data)
    :node-types node-types
    :edge-types edge-types
    :connectFn connectFn
    :zoom-on-scroll zoom-on-scroll
    :preventScrolling preventScrolling]])


