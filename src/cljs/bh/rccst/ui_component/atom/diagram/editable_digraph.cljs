(ns bh.rccst.ui-component.atom.diagram.editable-digraph
  (:require [bh.rccst.ui-component.atom.diagram.diagram.dagre-support :as dagre]
            [clojure.set :as set]
            [re-com.core :as rc]
            [reagent.core :as r]
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

     :edges [
             {:id        "target-data->targets"
              :source    ":topic/target-data" :target ":ui/targets"
              :style     {:strokeWidth 2 :stroke :orange}
              :markerEnd {:type (.-Arrow MarkerType)}}      ;:type (.-ArrowClosed MarkerType)}}
             {:id        "targets->selected-targets"
              :source    ":ui/targets" :target ":topic/selected-targets"
              :style     {:strokeWidth 2 :stroke :blue}
              :markerEnd {:type (.-ArrowClosed MarkerType)}}]}))
(def sample-data-2
  (r/atom
    {:nodes [{:id       ":fn/range",
              :type     ":source/fn",
              :data     {:label   ":fn/range",
                         :inputs  {":topic/coverage-data" [":data" ":data"]},
                         :outputs {":topic/time-range" [":range" ":data"]}},
              :position {:x 0, :y 86}}
             {:id       ":ui/globe",
              :type     ":ui/component",
              :data     {:label   ":ui/globe",
                         :inputs  {":topic/shapes" [":data" ":shapes"], ":topic/current-time" [":data" ":current-time"]},
                         :outputs {}},
              :position {:x 0, :y 602}}
             {:id       ":topic/coverage-data",
              :type     ":source/remote",
              :data     {:label   ":topic/coverage-data",
                         :inputs  {},
                         :outputs {":fn/coverage" [":data" ":coverages"], ":fn/range" [":data" ":data"]}},
              :position {:x 454, :y 0}}
             {:id       ":topic/shapes",
              :type     ":source/local",
              :data     {:label   ":topic/shapes",
                         :inputs  {":fn/coverage" [":shapes" ":data"]},
                         :outputs {":ui/globe" [":data" ":shapes"]}},
              :position {:x 615.5, :y 516}}
             {:id       ":ui/satellites",
              :type     ":ui/component",
              :data     {:label   ":ui/satellites",
                         :inputs  {":topic/satellite-data" [":data" ":data"]},
                         :outputs {":topic/satellite-data" [":data" ":data"], ":topic/selected-satellites" [":selection" ":data"]}},
              :position {:x 333, :y 258}}
             {:id       ":topic/current-time",
              :type     ":source/local",
              :data     {:label   ":topic/current-time",
                         :inputs  {":ui/time-slider" [":value" ":data"]},
                         :outputs {":ui/current-time" [":data" ":value"],
                                   ":ui/time-slider"  [":data" ":value"],
                                   ":ui/globe"        [":data" ":current-time"],
                                   ":fn/coverage"     [":data" ":current-time"]}},
              :position {:x 0, :y 344}}
             {:id       ":fn/coverage",
              :type     ":source/fn",
              :data     {:label   ":fn/coverage",
                         :inputs  {":topic/coverage-data"       [":data" ":coverages"],
                                   ":topic/current-time"        [":data" ":current-time"],
                                   ":topic/selected-targets"    [":data" ":targets"],
                                   ":topic/selected-satellites" [":data" ":satellites"]},
                         :outputs {":topic/shapes" [":shapes" ":data"]}},
              :position {:x 615.5, :y 430}}
             {:id       ":topic/time-range",
              :type     ":source/local",
              :data     {:label   ":topic/time-range",
                         :inputs  {":fn/range" [":range" ":data"]},
                         :outputs {":ui/time-slider" [":data" ":range"]}},
              :position {:x 0, :y 172}}
             {:id       ":ui/time-slider",
              :type     ":ui/component",
              :data     {:label   ":ui/time-slider",
                         :inputs  {":topic/current-time" [":data" ":value"], ":topic/time-range" [":data" ":range"]},
                         :outputs {":topic/current-time" [":value" ":data"]}},
              :position {:x 0, :y 258}}
             {:id       ":topic/target-data",
              :type     ":source/remote",
              :data     {:label   ":topic/target-data",
                         :inputs  {":ui/targets" [":selection" ""]},
                         :outputs {":ui/targets" [":data" ":data"]}},
              :position {:x 666, :y 172}}
             {:id       ":topic/satellite-data",
              :type     ":source/remote",
              :data     {:label   ":topic/satellite-data",
                         :inputs  {":ui/satellites" [":selection" ""]},
                         :outputs {":ui/satellites" [":data" ":data"]}},
              :position {:x 222, :y 344}}
             {:id       ":ui/targets",
              :type     ":ui/component",
              :data     {:label   ":ui/targets",
                         :inputs  {":topic/target-data" [":data" ":data"]},
                         :outputs {":topic/target-data" [":data" ":data"], ":topic/selected-targets" [":selection" ":data"]}},
              :position {:x 666, :y 258}}
             {:id       ":topic/selected-targets",
              :type     ":source/local",
              :data     {:label   ":topic/selected-targets",
                         :inputs  {":ui/targets" [":selection" ":data"]},
                         :outputs {":fn/coverage" [":data" ":targets"]}},
              :position {:x 666, :y 344}}
             {:id       ":topic/selected-satellites",
              :type     ":source/local",
              :data     {:label   ":topic/selected-satellites",
                         :inputs  {":ui/satellites" [":selection" ":data"]},
                         :outputs {":fn/coverage" [":data" ":satellites"]}},
              :position {:x 444, :y 344}}
             {:id       ":ui/current-time",
              :type     ":ui/component",
              :data     {:label ":ui/current-time", :inputs {":topic/current-time" [":data" ":value"]}, :outputs {}},
              :position {:x 60.5, :y 430}}]
     :edges [{:targetHandle ":data" :animated false
              :source       ":fn/range" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "0" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":range" :target ":topic/time-range"}
             {:targetHandle ":coverages" :animated false
              :source       ":topic/coverage-data" :style {:strokeWidth 1 :stroke :black}
              :label        ":coverages" :id "1" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":fn/coverage"}
             {:targetHandle ":data" :animated false
              :source       ":topic/coverage-data" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "2" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":fn/range"}
             {:targetHandle ":shapes" :animated false
              :source       ":topic/shapes" :style {:strokeWidth 1 :stroke :black}
              :label        ":shapes" :id "3" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":ui/globe"}
             {:targetHandle ":data" :animated false
              :source       ":ui/satellites" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "4" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":topic/satellite-data"}
             {:targetHandle ":data" :animated false
              :source       ":ui/satellites" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "5" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":selection" :target ":topic/selected-satellites"}
             {:targetHandle ":value" :animated false
              :source       ":topic/current-time" :style {:strokeWidth 1 :stroke :black}
              :label        ":value" :id "6" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":ui/current-time"}
             {:targetHandle ":value" :animated false
              :source       ":topic/current-time" :style {:strokeWidth 1 :stroke :black}
              :label        ":value" :id "7" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":ui/time-slider"}
             {:targetHandle ":current-time" :animated false
              :source       ":topic/current-time" :style {:strokeWidth 1 :stroke :black}
              :label        ":current-time" :id "8" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":ui/globe"}
             {:targetHandle ":current-time" :animated false
              :source       ":topic/current-time" :style {:strokeWidth 1 :stroke :black}
              :label        ":current-time" :id "9" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":fn/coverage"}
             {:targetHandle ":data" :animated false
              :source       ":fn/coverage" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "10" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":shapes" :target ":topic/shapes"}
             {:targetHandle ":range" :animated false
              :source       ":topic/time-range" :style {:strokeWidth 1 :stroke :black}
              :label        ":range" :id "11" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":ui/time-slider"}
             {:targetHandle ":data" :animated false
              :source       ":ui/time-slider" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "12" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":value" :target ":topic/current-time"}
             {:targetHandle ":data" :animated false
              :source       ":topic/target-data" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "13" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":ui/targets"}
             {:targetHandle ":data" :animated false
              :source       ":topic/satellite-data" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "14" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":ui/satellites"}
             {:targetHandle ":data" :animated false
              :source       ":ui/targets" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "15" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":topic/target-data"}
             {:targetHandle ":data" :animated false
              :source       ":ui/targets" :style {:strokeWidth 1 :stroke :black}
              :label        ":data" :id "16" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":selection" :target ":topic/selected-targets"}
             {:targetHandle ":targets" :animated false
              :source       ":topic/selected-targets" :style {:strokeWidth 1 :stroke :black}
              :label        ":targets" :id "17" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":fn/coverage"}
             {:targetHandle ":satellites" :animated false
              :source       ":topic/selected-satellites" :style {:strokeWidth 1 :stroke :black}
              :label        ":satellites" :id "18" :markerEnd {:type (.-ArrowClosed MarkerType)}
              :sourceHandle ":data" :target ":fn/coverage"}]}))
(def sample-data-3
  (r/atom
    {:nodes [{:id "1 " :type "input" :data {:label "input"} :position {:x 0 :y 0}}
             {:id "2 " :data {:label "node 2 "} :position {:x 0 :y 0}}
             {:id "2a " :data {:label "node 2a "} :position {:x 0 :y 0}}
             {:id "2b " :data {:label "node 2b "} :position {:x 0 :y 0}}
             {:id "2c " :data {:label "node 2c "} :position {:x 0 :y 0}}
             {:id "2d " :data {:label "node 2d "} :position {:x 0 :y 0}}
             {:id "3 " :data {:label "node 3 "} :position {:x 0 :y 0}}
             {:id "4 " :data {:label "node 4 "} :position {:x 0 :y 0}}
             {:id "5 " :data {:label "node 5 "} :position {:x 0 :y 0}}
             {:id "6 " :type "output" :data {:label "output"} :position {:x 0 :y 0}}
             {:id "7 " :type "output" :data {:label "output"} :position {:x 0 :y 0}}]

     :edges [{:id "e12" :source "1 " :target "2 " :type "smoothstep" :animated true}
             {:id "e13" :source "1 " :target "3 " :type "smoothstep" :animated true}
             {:id "e22a" :source "2 " :target "2a " :type "smoothstep" :animated true}
             {:id "e22b" :source "2 " :target "2b " :type "smoothstep" :animated true}
             {:id "e22c" :source "2 " :target "2c " :type "smoothstep" :animated true}
             {:id "e2c2d" :source "2c " :target "2d " :type "smoothstep" :animated true}
             {:id "e45" :source "4 " :target "5 " :type "smoothstep" :animated true}
             {:id "e56" :source "5 " :target "6 " :type "smoothstep" :animated true}
             {:id "e57" :source "5 " :target "7 " :type "smoothstep" :animated true}]}))


(def source-code '[])


(def handle-style {:width "8px" :height "8px" :borderRadius "50%"})
(def default-node-style {:padding      "3px" :max-width "180px"
                         :borderRadius "5px" :margin :auto
                         :background   :white :color :black})
(def node-style {:ui/component  {:background :green :color :white}
                 :source/remote {:background :orange :color :black}
                 :source/local  {:background :blue :color :white}
                 :source/fn     {:background :pink :color :black}})


(defn- source-panel [])


(defn- find-node-by-id [nodes id]
  (first (filter #(= id (:id %)) nodes)))


(defn- input-handles
  "
  NOTE: the inputs (values in the hash-map) are STRINGS!
  "
  [label inputs position]
  [:<>
   (doall
     (->> inputs
       (map-indexed (fn [idx [target ports]]
                      (let [[source-port target-port] ports]
                        ;(log/info "input-handle" label target-port)
                        [:> Handle {:id    target-port :type "target" :position position
                                    :style (merge handle-style {:left (+ 20 (* 10 idx))})}])))
       (into [:<>])))])


(defn- output-handles
  "
  NOTE: the inputs (values in the hash-map) are STRINGS!
  "
  [label outputs position]
  [:<>
   (doall
     (->> outputs
       (map-indexed (fn [idx [target ports]]
                      (let [[source-port target-port] ports]
                        ;(log/info "output-handle" label source-port)
                        [:> Handle {:id    source-port :type "source" :position position
                                    :style (merge handle-style {:right (+ 20 (* 10 idx))})}])))
       (into [:<>])))])


(defn- apply-handles [label inputs outputs input-position output-position]
  (let [i        (->> inputs
                   (map (fn [[k [s d]]] [k s d]))
                   (into #{}))
        o        (->> outputs
                   (map (fn [[k [s d]]] [k s d]))
                   (into #{}))
        in-out   (set/intersection i o)
        in-only  (set/difference (set/difference i o) in-out)
        out-only (set/difference (set/difference o i) in-out)]

    (input-handles label in-out input-position)
    (input-handles label in-only input-position)
    (output-handles label out-only output-position)))


(defmulti custom-node (fn [type _] type))


(defmethod custom-node :ui/component [type d]
  (let [data    (js->clj d :keywordize-keys true)
        label   (get-in data [:data :label])
        inputs  (get-in data [:data :inputs])
        outputs (get-in data [:data :outputs])
        style   (merge default-node-style (type node-style))]

    ;(log/info "custom-node" label type data "///" inputs "///" outputs)

    (r/as-element
      [:div {:style style}                                  ;:on-click #(open-details component-id label)}
       [:h5 {:style (merge {:textAlign :center} style)} label]
       (input-handles label inputs "bottom")
       (output-handles label outputs "bottom")])))


(defmethod custom-node :source/remote [type d]
  (let [data    (js->clj d :keywordize-keys true)
        label   (get-in data [:data :label])
        inputs  (get-in data [:data :inputs])
        outputs (get-in data [:data :outputs])
        style   (merge default-node-style (type node-style))]

    ;(log/info "custom-node" label type data "///" inputs "///" outputs)

    (r/as-element
      [:div {:style style}                                  ;:on-click #(open-details component-id label)}
       [:h5 {:style (merge {:textAlign :center} style)} label]
       (input-handles label inputs "top")
       (output-handles label outputs "top")])))


(defmethod custom-node :source/local [type d]
  (let [data    (js->clj d :keywordize-keys true)
        label   (get-in data [:data :label])
        inputs  (get-in data [:data :inputs])
        outputs (get-in data [:data :outputs])
        style   (merge default-node-style (type node-style))]

    ;(log/info "custom-node" label type data "///" inputs "///" outputs)

    (r/as-element
      [:div {:style style}                                  ;:on-click #(open-details component-id label)}
       [:h5 {:style (merge {:textAlign :center} style)} label]
       (input-handles label inputs "top")
       (output-handles label outputs "top")])))


(defmethod custom-node :source/fn [type d]
  (let [data    (js->clj d :keywordize-keys true)
        label   (get-in data [:data :label])
        inputs  (get-in data [:data :inputs])
        outputs (get-in data [:data :outputs])
        style   (merge default-node-style (type node-style))]

    ;(log/info "custom-node" label type data "///" inputs "///" outputs)

    (r/as-element
      [:div {:style style}                                  ;:on-click #(open-details component-id label)}
       [:h5 {:style (merge {:textAlign :center} style)} label]
       (input-handles label inputs "bottom")
       (output-handles label outputs "bottom")])))


(defmethod custom-node :default [type d]
  (let [data    (js->clj d :keywordize-keys true)
        label   (get-in data [:data :label])
        inputs  (get-in data [:data :inputs])
        outputs (get-in data [:data :outputs])
        style   (merge default-node-style (type node-style))]

    ;(log/info "custom-node" label type data "///" inputs "///" outputs)

    (r/as-element
      [:div {:style style}                                  ;:on-click #(open-details component-id label)}
       [:h5 {:style (merge {:textAlign :center} style)} label]
       (input-handles label inputs "top")
       (output-handles label outputs "bottom")])))


(defn- default-custom-node
  "build a custom node for the flow diagram, based on the :type property of the node
  "
  [type d]

  ;(log/info "default-custom-node" type)

  (custom-node type d))


(def default-color-pallet {":ui/component"  "#00ff00"
                           ":source/remote" "#FFA500"
                           ":source/local"  "#0000ff"
                           ":source/fn"     "#FFC0CB"})
(def color-black "#000000")
(def color-white "#ffffff")


(defn custom-minimap-node-color [color-pallet default-color node]
  (or (get color-pallet (.-type node)) default-color))


(def default-node-types #js {":ui/component"  (partial default-custom-node :ui/component)
                             ":source/remote" (partial default-custom-node :source/remote)
                             ":source/local"  (partial default-custom-node :source/local)
                             ":source/fn"     (partial default-custom-node :source/fn)})


(def default-minimap-styles {:nodeStrokeColor  (partial custom-minimap-node-color
                                                 default-color-pallet color-white)
                             :node-color       (partial custom-minimap-node-color
                                                 default-color-pallet color-black)
                             :nodeBorderRadius 5})


(defn- on-drag-start [node-type event]
  (.setData (.-dataTransfer event) "editable-flow" node-type)
  (set! (.-effectAllowed (.-dataTransfer event)) "move"))


(defn- on-drag-over [event]
  (.preventDefault event)
  (set! (.-dropEffect (.-dataTransfer event)) "move"))


(defn- on-drop [set-nodes-fn wrapper event]
  (.preventDefault event)
  (let [node-type       (.getData (.-dataTransfer event) "editable-flow")
        x               (.-clientX event)
        y               (.-clientY event)
        reactFlowBounds (.getBoundingClientRect @wrapper)]

    ;(log/info "on-drop" node-type
    ;"//" @wrapper)
    ;"//" (.-current @wrapper)
    ;"//" (.getBoundingClientRect @wrapper))
    ;"//" (js->clj reactFlowBounds)

    (when (not= node-type "undefined")
      (let [new-id   (str node-type "-new")
            new-node {:id       new-id
                      :type     node-type
                      :data     {:label   new-id
                                 :inputs  []
                                 :outputs []}
                      :position {:x (- x (.-left reactFlowBounds))
                                 :y (- y (.-top reactFlowBounds))}}]
        ; TODO: need to trigger a "get back into CLJS and SAVE" operation
        (set-nodes-fn (fn [nds] (.concat nds (clj->js new-node))))))))


(comment
  (def data [{:id "one"} {:id "two"}])

  (conj data {:id "three"})


  ())


(defn- make-draggable-node [[k {:keys [label type color text-color]}]]
  ;(log/info "make-draggable-node" label type)
  ^{:key label} [:div.draggable
                 {:style       {:width           "150px" :height "50px"
                                :margin-bottom   "5px"
                                :display         :flex
                                :justify-content :center
                                :align-items     :center
                                :cursor          :grab
                                :border-radius   "3px" :padding "2px"
                                :background      color :color text-color}
                  :onDragStart #(on-drag-start type %)
                  :draggable   true}
                 label])


(def default-tool-types {:ui/component  {:label ":ui/component" :type :ui/component :color "green" :text-color :white}
                         :source/remote {:label ":source/remote" :type :source/remote :color "orange" :text-color :black}
                         :source/local  {:label ":source/local" :type :source/local :color "blue" :text-color :white}
                         :source/fn     {:label ":source/fn" :type :source/fn :color "pink" :text-color :black}})


(defn- tool-panel [tool-types]
  ;(log/info "tool-panel" tool-types)
  [:div#tool-panel {:display         :flex
                    :flex-direction  :column
                    :justify-content :center
                    :align-items     :center
                    :style           {:width         "200px" :height "100%"
                                      :border-radius "5px" :padding "15px 10px"
                                      :background    :white :box-shadow "5px 5px 5px #888888"}}
   (doall
     (map make-draggable-node tool-types))])


(defn- flow* [& {:keys [component-id nodes edges
                        node-types edge-types
                        minimap-styles
                        on-change-nodes on-change-edges on-drop on-drag-over
                        zoom-on-scroll preventScrolling connectFn]}]

  ;(log/info "flow(star)" component-id)
  ;"//" (or minimap-styles {}))
  ; "//" (js->clj minimap-styles) "//" (type minimap-styles))

  (let [params (apply merge {:nodes               nodes
                             :edges               edges
                             :onNodesChange       on-change-nodes
                             :onEdgesChange       on-change-edges
                             :zoomOnScroll        (or zoom-on-scroll false)
                             :preventScrolling    (or preventScrolling false)
                             :onConnect           (or connectFn #())
                             :fitView             true
                             :attributionPosition "top-right"
                             :onDrop              (or on-drop #())
                             :onDragOver          (or on-drag-over #())}
                 (when node-types {:node-types node-types})
                 (when edge-types {:edge-types node-types}))]

    [:> ReactFlow params
     [:> MiniMap (if minimap-styles minimap-styles {})]
     [:> Background]
     [:> Controls]]))


(defn- editable-flow [& {:keys [component-id
                                nodes edges
                                node-types edge-types
                                minimap-styles on-drop on-drag-over
                                zoom-on-scroll preventScrolling connectFn]}]
  (let [;{n :nodes e :edges} (dagre/build-layout nodes edges)
        [ns set-nodes on-change-nodes] (useNodesState (clj->js nodes))
        [es set-edges on-change-edges] (useEdgesState (clj->js edges))
        !wrapper (clojure.core/atom nil)]


    ;(log/info "editable-flow"
    ;  "//" ns
    ;  "//" nodes)
    ;  "//" set-nodes
    ;  "//" on-change-nodes)

    [:div#wrapper {:style {:width "800px" :height "700px"}
                   :ref   (fn [el]
                            (reset! !wrapper el))}
     [flow*
      :component-id component-id
      :nodes ns :edges es
      :on-change-nodes on-change-nodes
      :on-change-edges on-change-edges
      :node-types node-types
      :edge-types edge-types
      :minimap-styles minimap-styles
      :connectFn connectFn
      :zoom-on-scroll zoom-on-scroll
      :preventScrolling preventScrolling
      :on-drop (partial on-drop set-nodes !wrapper)
      :on-drag-over on-drag-over]]))


(defn component [& {:keys [data
                           node-types edge-types
                           minimap-styles
                           connectFn zoom-on-scroll preventScrolling
                           component-id container-id]}]

  ;(log/info "component (DIGRAPH)" "//" (:nodes @data))

  [rc/h-box :src (rc/at)
   :gap "10px"
   :children [[tool-panel default-tool-types]
              [:f> editable-flow
               :component-id component-id
               :nodes (:nodes @data)
               :edges (:edges @data)
               :node-types node-types
               :edge-types edge-types
               :on-drop on-drop
               :on-drag-over on-drag-over
               :minimap-styles minimap-styles
               :connectFn connectFn
               :zoom-on-scroll zoom-on-scroll
               :preventScrolling preventScrolling]]])


(comment
  (:nodes @sample-data)
  (swap! sample-data assoc :nodes (conj (:nodes @sample-data)
                                    {:id "dummy-node" :position {:x 0 :y 0}}))

  ())



(comment
  (let {nodes :nodes edges :edges} (dagre/build-layout
                                     (:nodes @sample-data-2)
                                     (:edges @sample-data-2)))

  (->> (dagre/build-layout (:nodes @sample-data-2)
         (:edges @sample-data-2))
    :nodes
    (map #(dissoc % :targetPosition :sourcePosition)))

  ())
