(ns bh.rccst.ui-component.molecule.composite.util.ui
  (:require [bh.rccst.ui-component.molecule.composite.util.signals :as sig]
            [bh.rccst.ui-component.utils :as ui-utils]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [reagent.core :as r]
            ["dagre" :as dagre]
            ["graphlib" :as graphlib]
            ["react-flow-renderer" :refer (ReactFlowProvider Controls Handle Background) :default ReactFlow]))


(def handle-style {:width "8px" :height "8px" :borderRadius "50%"})
(def default-node-style {:padding      "3px" :max-width "180px"
                         :borderRadius "5px" :margin :auto
                         :background   :white :color :black})
(def node-style {:ui/component  {:background :green :color :white}
                 :source/remote {:background :orange :color :black}
                 :source/local  {:background :blue :color :white}
                 :source/fn     {:background :pink :color :black}})


(defn input-output-handles
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
                        ;(log/info "input-handle" label "/" target-port "///" target "/" source-port)
                        [:> Handle {:id    target-port :type "target" :position "top"
                                    :style (merge handle-style {:left (+ 20 (* 10 idx))})}])))
       (into [:<>])))

   ; add the output handles
   (doall
     (->> outputs
       (map-indexed (fn [idx [target ports]]
                      (let [[source-port target-port] ports]
                        ;(log/info "output-handle" label "/" source-port "///" target "/" target-port)
                        [:> Handle {:id    source-port :type "source" :position "bottom"
                                    :style (merge handle-style {:left (+ 20 (* 10 idx))})}])))
       (into [:<>])))])


(defn custom-node
  "build a custom node for the flow diagram, this time for :ui/component, so
  green, since this is a 'view', and one Handle for each input (along the top)
  and output (along the bottom)
  "
  [type d]
  (let [data    (js->clj d)
        label   (get-in data ["data" "label"])
        inputs  (get-in data ["data" "inputs"])
        outputs (get-in data ["data" "outputs"])
        style   (merge default-node-style (type node-style))]

    ;(log/info "custom-node" label data "///" inputs "///" outputs)

    (r/as-element
      [:div {:style style}
       [:h5 {:style (merge {:textAlign :center} style)} label]
       (input-output-handles label inputs outputs)])))


(defn node-type [node]
  (:el-type node))


(defn dump-dagre [dagreGraph]
  (doall
    (map (fn [n]
           (println "node" (js->clj n)))
      (.nodes dagreGraph)))
  (doall
    (map (fn [n]
           (println "edge" (js->clj n)))
      (.edges dagreGraph))))


(defn dagre-graph
  "copy the nodes and edges from Look to dagre, so we can use dagre layout function to put them
  onto the display without drawing over each other
  "
  [graph]
  (let [dagreGraph (new (.-Graph graphlib))
        nodeWidth  172
        nodeHeight 36]

    (.setDefaultEdgeLabel dagreGraph (clj->js {}))
    (.setGraph dagreGraph (clj->js {:rankdir "tb"}))

    (doall
      (map (fn [element]
             (condp = (:el-type element)
               :node (.setNode dagreGraph (:id element)
                       (clj->js {:width nodeWidth :height nodeHeight}))
               :edge (.setEdge dagreGraph (:source element)
                       (:target element))))
        graph))

    dagreGraph))


(defn build-layout
  "use dagre (see https://reactflow.dev/examples/layouting/) to perform an auto-layout of the nodes,
  which are then connected by the edges."
  [graph]
  (let [dagreGraph (dagre-graph graph)
        nodeWidth  172
        nodeHeight 36]

    (.layout dagre dagreGraph)

    (doall
      (map (fn [element]
             ;(log/info "element" (:id element) (.node dagreGraph (clj->js (:id element))))
             (condp = (:el-type element)
               :node (let [dagreNode (.node dagreGraph (clj->js (:id element)))]
                       (assoc element :position {:x (- (.-x dagreNode) (/ nodeWidth 2))
                                                 :y (- (.-y dagreNode) (/ nodeHeight 2))}))
               :edge element))
        graph))))


(defn create-flow-node
  "convert the nodes, currently organized by Loom (https://github.com/aysylu/loom), into
  the format needed by react-flow (https://reactflow.dev)
  "
  [configuration node-id]
  (let [node-type (get-in configuration [:components node-id :type])]
    ;(log/info "node" node-id node-type)
    {:id       (str node-id)
     :el-type  :node
     :type     (str node-type)
     :data     {:label   (str node-id)
                :inputs  (->>
                           (get-in configuration [:denorm node-id :inputs])
                           (map (fn [[k v]]
                                  (let [[sp tp] v]
                                    {(str k) [(str sp) (str tp)]})))
                           (into {}))
                :outputs (->>
                           (get-in configuration [:denorm node-id :outputs])
                           (map (fn [[k v]]
                                  (let [[sp tp] v]
                                    {(str k) [(str sp) (str tp)]})))
                           (into {}))}
     :position {}}))


(defn create-flow-edge
  "convert the edges, currently organized by Loom (https://github.com/aysylu/loom), into
  the format needed by react-flow (https://reactflow.dev)
  "
  [configuration idx [node-id target-id :as edge]]
  (let [[source-handle target-handle] (get-in configuration [:denorm node-id :outputs target-id])]

    ;(log/info "flow-edge" idx "/" node-id "/" source-handle "///" target-id "/" target-handle)

    {:id            (str idx)
     :el-type       :edge
     :source        (str node-id)
     :sourceHandle  (str source-handle)
     :target        (str target-id)
     :targetHandle  (str target-handle)
     :label         (str target-handle)
     :style         {:stroke-width 1 :stroke :black}
     :arrowHeadType "arrowclosed"
     :animated      false}))


(defn compute-edges
  "pull out just the relevant information from the configuration, so it can be passed into Loom and
  the interconnected digraph can be built
  "
  [configuration]
  (->> configuration
    :links
    (mapcat (fn [[entity links]]
              (mapcat (fn [[source-port targets]]
                        (map (fn [[target target-port]]
                               [entity target])
                          targets))
                links)))
    (into [])))


(defn make-flow
  "take the Loom graph and turn it into what react-flow needs to draw it onto the display
  "
  [configuration]
  (let [flow (apply conj
               (map #(create-flow-node configuration %) (:nodes configuration))
               (map-indexed (fn [idx node]
                              (create-flow-edge configuration idx node))
                 (:edges configuration)))]
    (build-layout flow)))


(defn prep-environment [configuration component-id registry]
  ; 1. remote subscriptions (including the remote call)
  ;
  ; [SIDE EFFECT]
  (sig/process-components configuration :source/remote registry component-id)

  ; 2. build the subscription for the "container" which provide the basis for the
  ;     subscriptions for the "locals"
  ;
  ; [SIDE EFFECT]
  (ui-utils/create-widget-sub component-id)
  (ui-utils/create-widget-local-sub component-id [:blackboard])

  ; 3. add blackboard data to the app-db and build local subscriptions/events against the blackboard
  ;
  ; [SIDE EFFECT]
  (sig/process-components configuration :source/local registry component-id)

  ; 4. local functions (to build subscriptions against the blackboard or remotes)
  ;
  ; [SIDE EFFECT]
  (sig/process-components configuration :source/fn registry component-id))

