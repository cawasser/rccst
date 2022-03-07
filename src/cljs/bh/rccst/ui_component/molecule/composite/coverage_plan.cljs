(ns bh.rccst.ui-component.molecule.composite.coverage-plan
  "provide a composed UI for a \"Coverage Plan\" which shows targets and satellite coverage areas
  on a 3D globe"
  (:require [bh.rccst.ui-component.atom.experimental.ui-element :as e]
            [bh.rccst.ui-component.utils :as ui-utils]
            [loom.graph :as lg]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            ["dagre" :as dagre]
            ["graphlib" :as graphlib]
            ["react-flow-renderer" :refer (ReactFlowProvider Controls Handle) :default ReactFlow]))


(log/info "bh.rccst.ui-component.molecule.composite.coverage-plan")


(defn fn-coverage
  "registers the subscription for the entity defined by 'layers'. processing from
  inputs to the output is performed by 'some-computation'

  this function assumes that the caller provides fully-qualified signal vectors, so the CALLER
  is responsible for building the keyword (using path->keyword)

  - targets : (vector of keywords) the subscription signal for the target data
  - satellites : (vector of keywords) the subscription signal for the 'selected' satellite data
  - coverages : (vector of keywords) the subscription signal for the coverage data

  builds and registers the subscription provided by 'layers'

  "
  [& {:keys [targets satellites coverages layers]}]

  (let [some-computation (fn [t s c] [:fn-coverage])]

    (re-frame/reg-sub
      layers
      :<- [targets]
      :<- [satellites]
      :<- [coverages]
      (fn [t s c [_ _]]
        (some-computation t s c)))))


(defn fn-range
  "registers the subscription for the entity defined by 'selected'. processing from
  inputs to output is performed by 'some-computation'

  - data : (vector of keywords) the subscription signal for the input data
  - container-id : (string) name of the container holding the blackboard

  builds and registers the subscription :<container>/blackboard.<selected>
  "
  [& {:keys [data selected]}]

  (let [some-computation (fn [x] [:fn-range])]

    (re-frame/reg-sub
      selected
      :<- [data]
      (fn [d [_ _]]
        (some-computation d)))))


; assume the ui components have the following meta-data:
;
;      you SUBSCRIBE from a :port/sink    (re-frame/subscribe ...)
;
;      you PUBLISH to a :port/source      (re-frame/dispatch ...)
;
;      you do BOTH with :port/source-sink (both)


(def meta-data-registry
  {
   :table/selectable-table {:component e/selectable-table
                            :ports     {:data      :port/source-sink
                                        :selection :port/source}}

   :globe/three-d-globe    {:component e/three-d-globe
                            :ports     {:layers       :port/sink
                                        :current-time :port/sink}}

   :slider/slider          {:component e/slider
                            :ports     {:value :port/source-sink
                                        :range :port/source}}

   :label/label            {:component e/label
                            :ports     {:value :port/sink}}})


(def sample-data (r/atom {:title        "Coverage Plan"
                          :component-id :coverage-plan
                          :components   {; ui components
                                         :ui/targets                {:type :ui/component :name :table/selectable-table}
                                         :ui/satellites             {:type :ui/component :name :table/selectable-table}
                                         :ui/globe                  {:type :ui/component :name :globe/three-d-globe}
                                         :ui/time-slider            {:type :ui/component :name :slider/slider}
                                         :ui/current-time           {:type :ui/component :name :label/label}

                                         ; remote data sources
                                         :topic/target-data         {:type :source/remote :name :source/targets}
                                         :topic/satellite-data      {:type :source/remote :name :source/satellites}
                                         :topic/coverage-data       {:type :source/remote :name :source/coverages}

                                         ; composite-local data sources
                                         :topic/selected-targets    {:type :source/local :name :selected-targets}
                                         :topic/selected-satellites {:type :source/local :name :selected-satellites}
                                         :topic/current-time        {:type :source/local :name :current-time}
                                         :topic/selected-coverages  {:type :source/local :name :selected-coverages}
                                         :topic/time-range          {:type :source/local :name :time-range}

                                         ; transformation functions
                                         :fn/coverage               {:type  :source/fn
                                                                     :name  fn-coverage
                                                                     :ports {:targets    :port/sink
                                                                             :satellites :port/sink
                                                                             :coverages  :port/sink
                                                                             :layers     :port/source}}
                                         :fn/range                  {:type  :source/fn
                                                                     :name  fn-range
                                                                     :ports {:data  :port/sink
                                                                             :range :port/source}}}

                          :links        {; ui components publish to what? via which port?
                                         ;
                                         ; <source>                 {<source-port>  {<target> <target-port>
                                         ;                                           <target> <target-port>}}
                                         ;
                                         :ui/targets                {:data      {:topic/target-data :data}
                                                                     :selection {:topic/selected-targets :data}}
                                         :ui/satellites             {:data      {:topic/satellite-data :data}
                                                                     :selection {:topic/selected-satellites :data}}
                                         :ui/time-slider            {:value {:topic/current-time :data}}

                                         ; transformation functions publish to what?
                                         :fn/coverage               {:selected {:topic/selected-coverages :data}}
                                         :fn/range                  {:range {:topic/time-range :data}}

                                         ; topics are inputs into what?
                                         :topic/target-data         {:data {:ui/targets :data}}
                                         :topic/satellite-data      {:data {:ui/satellites :data}}
                                         :topic/selected-targets    {:data {:fn/coverage :targets}}
                                         :topic/selected-satellites {:data {:fn/coverage :satellites}}
                                         :topic/coverage-data       {:data {:fn/coverage :coverages
                                                                            :fn/range    :data}}
                                         :topic/selected-coverages  {:data {:ui/globe :coverages}}
                                         :topic/current-time        {:data {:ui/current-time :value
                                                                            :ui/time-slider  :value
                                                                            :ui/globe        :current-time}}
                                         :topic/time-range          {:data {:ui/time-slider :range}}}

                          :layout       [rc/v-box
                                         [rc/h-box
                                          [rc/v-box [:ui/targets] [:ui/satellites] [:ui/time-slider]]
                                          [rc/v-box [:ui/globe] [:ui/current-time]]]]}))


(def source-code '[coverage-plan])


(defn- config [_ _]
  {})



; we want to turn the composite-def into things like...
;
(comment
  {:fn/coverage     (fn-coverage
                      :targets [:coverage-plan/blackboard.topic.selected-targets]
                      :satellites [:coverage-plan/blackboard.topic.selected-satellites]
                      :coverages [:topic/coverages]
                      :selected [:coverage-plan/blackboard.topic.selected-coverages])

   :fn/range        (fn-range
                      :data [:topic/coverages]
                      :selected [:coverage-plan/blackboard.topic.time-range])

   :ui/targets      [e/selectable-table
                     :component-id :coverage-plan/targets
                     :container-id :coverage-plan
                     :data [:topic/target-data]
                     :selected [:coverage-plan/blackboard.topic.selected-targets]]

   :ui/satellites   [e/selectable-table
                     :component-id :coverage-plan/satellites
                     :container-id :coverage-plan
                     :data [:topic/satellite-data]
                     :selected [:coverage-plan/blackboard.topic.selected-targets]]

   :ui/globe        [e/globe
                     :component-id :coverage-plan/globe
                     :container-id :coverage-plan
                     :coverages [:coverage-plan/blackboard.topic.selected-coverages]
                     :current-time [:coverage-plan/blackboard.topic.current-time]]

   :ui/time-slider  [e/slider
                     :component-id :coverage-plan/slider
                     :container-id :coverage-plan
                     :value [:coverage-plan/blackboard.topic.current-time]
                     :range [:coverage-plan/blackboard.topic.time-range]]

   :ui/current-time [e/label
                     :component-id :coverage-plan/label
                     :container-id :coverage-plan
                     :value [:coverage-plan/blackboard.topic.current-time]]}

  ())


;;;;;;;;;;
;;;;;;;;;;
;
;  We'll use multi-methods to convert the component types into the correct "code"
;
;;;;;;;;;;
;;;;;;;;;;
;; region

(defmulti component->ui (fn [{:keys [type]}]
                          type))

(defmethod component->ui :ui/component [{:keys [name]}]
  [name])

(defmethod component->ui :source/local [{:keys [component-id name]}]
  (ui-utils/path->keyword component-id name))

(defmethod component->ui :source/remote [{:keys [component-id name]}]
  name)

(defmethod component->ui :source/fn [{:keys [name ports]}]
  [name ports])
;; endregion


;;;;;;;;;;
;;;;;;;;;;
;
;  Expand on the configuration, computing denormalized data, the Loom digraph, etc.
;
;;;;;;;;;;
;;;;;;;;;;
;; region


(defn- expand-components [data]
  (let [components (:components data)]
    (->> data
      :components
      (map (fn [[id meta-data]]
             {id (assoc meta-data
                   :ports
                   (condp = (:type meta-data)
                     :ui/component (->> components id :name meta-data-registry :ports)
                     :source/remote {:port/pub-sub :data}
                     :source/local {:port/pub-sub :data}
                     :source/fn (:ports meta-data)))}))
      (assoc data :components))))


(defn- get-predecessor-name [links source target]
  ;(log/info "pred" source target)
  (->> links
    (filter (fn [[s targets]]
              (and (get targets source)
                (= s target))))
    vals
    first
    (#(get % source))))


(defn- get-successor-name [links source target]
  (->> links
    source
    target))


(defn denorm-components
  "denormalize the links between components by mixing in additional information bout the
  ports at both ends of the inter-connection:

  {<source> {:inputs  {<target> [<target's-port> <source's-port>]}
            {:outputs {<target> [<target's-port> <source's-port>]}}


  "
  [graph links nodes]
  (->> nodes
    (map (fn [node]
           (let [node-meta (->> links node)]
             {node {:inputs
                    (into {}
                      (map (fn [target]
                             ; TODO: what the heck should replace :dummy?!?!?!?!
                             {target [(get-predecessor-name links node target) :dummy]})
                        (lg/predecessors* graph node)))

                    :outputs
                    (into {}
                      (map (fn [target]
                             ; TODO: what the heck should replace :dummy?!?!?!?!
                             {target [(get-successor-name links node target) :dummy]})
                        (lg/successors* graph node)))}})))
    (into {})))



(comment
  (do
    (def data @sample-data)
    (def graph (apply lg/digraph (compute-edges @sample-data)))
    (def nodes (lg/nodes graph))
    (def links (:links data))
    (def components (:components data))
    (def configuration (assoc @sample-data
                         :components (expand-components data)
                         :graph graph
                         :nodes (lg/nodes graph)
                         :edges (lg/edges graph)))

    (def node-meta (->> links :ui/satellites)))


  (->> data
    :components
    (map (fn [[id meta-data] component]
           {id (assoc meta-data
                 :ports
                 (condp = (:type meta-data)
                   :ui/component (->> components id :name meta-data-registry :ports)
                   :source/remote {:port/pub-sub :data}
                   :source/local {:port/pub-sub :data}
                   :source/fn (:ports meta-data)))}))
    (assoc data :components))

  (expand-components data)

  (map #(assoc % :ports "x") (:components data))


  (def target-meta (map (fn [[target _]] (target meta-data-registry)) node-meta))

  (denorm-components graph links nodes)


  ())

;; endregion


;;;;;;;;;;
;;;;;;;;;;
;
;  UI Implementation
;
;;;;;;;;;;
;;;;;;;;;;
;; region

(def handle-style {:width "8px" :height "8px" :borderRadius "50%"})
(def default-node-style {:padding      "3px" :max-width "180px"
                         :borderRadius "5px" :margin :auto
                         :background   :white :color :black})
(def node-style {:ui/component  {:background :green :color :white}
                 :source/remote {:background :orange :color :black}
                 :source/local  {:background :blue :color :white}
                 :source/fn     {:background :pink :color :black}})


(defn- input-output-handles [label inputs outputs]
  [:<>
   ; add the input handles
   (doall
     (->> inputs
       (map (fn [[target port]]
              (log/info "input handle" label target port)
              [:> Handle {:id    port :type "target" :position "top"
                          :style handle-style}]))
       (into [:<>])))

   ; add the output handles
   (doall
     (->> outputs
       (map (fn [[target port]]
              (log/info "output handle" label target port)
              [:> Handle {:id    port :type "source" :position "bottom"
                          :style handle-style}]))
       (into [:<>])))])


(defn- custom-node
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

    (log/info "custom-node" label data "///" inputs "///" outputs)

    (r/as-element
      [:div {:style style}
       [:h5 {:style (merge {:textAlign :center} style)} label]
       (input-output-handles label inputs outputs)])))


(defn- node-type [node]
  (:el-type node))


(defn- dump-dagre [dagreGraph]
  (doall
    (map (fn [n]
           (println "node" (js->clj n)))
      (.nodes dagreGraph)))
  (doall
    (map (fn [n]
           (println "edge" (js->clj n)))
      (.edges dagreGraph))))


(defn- dagre-graph
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


(defn- layout
  "use dagre (see https://reactflow.dev/examples/layouting/) to perform an auto-layout of the nodes,
  which are then connected by the edges."
  [graph]
  (let [dagreGraph (dagre-graph graph)
        nodeWidth  172
        nodeHeight 36]

    (.layout dagre dagreGraph)

    (doall
      (map (fn [element]
             ;(println "element" (:id element) (.node dagreGraph (clj->js (:id element))))
             (condp = (:el-type element)
               :node (let [dagreNode (.node dagreGraph (clj->js (:id element)))]
                       (assoc element :position {:x (- (.-x dagreNode) (/ nodeWidth 2))
                                                 :y (- (.-y dagreNode) (/ nodeHeight 2))}))
               :edge element))
        graph))))


(defn- create-flow-node
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
                           (map (fn [[k v]] {(str k) (str v)}))
                           (into {}))
                :outputs (->>
                           (get-in configuration [:denorm node-id :outputs])
                           (map (fn [[k v]] {(str k) (str v)}))
                           (into {}))}
     :position {}}))

(comment
  (do
    (def graph (apply lg/digraph (compute-edges @sample-data)))
    (def configuration
      (assoc @sample-data
        :graph graph
        :denorm (denorm-components graph (:links configuration) (lg/nodes graph))
        :nodes (lg/nodes graph)
        :edges (lg/edges graph))))


  ())


(defn- create-flow-edge
  "convert the edges, currently organized by Loom (https://github.com/aysylu/loom), into
  the format needed by react-flow (https://reactflow.dev)
  "
  [configuration idx [node-id target-id :as edge]]
  (let [target-handle (or (get-in configuration [:links node-id target-id])
                        (get-in configuration [:links target-id node-id]))
        source-handle (get-in configuration [:denorm node-id :outputs target-id])]

    (log/info "flow-edge" idx node-id "/" source-handle "///" target-id "/" target-handle)

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


(defn- compute-edges
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


(defn- make-flow
  "take the Loom graph and turn it into what react-flow needs to draw it onto the display
  "
  [configuration]
  (let [flow (apply conj
               (map #(create-flow-node configuration %) (:nodes configuration))
               (map-indexed (fn [idx node]
                              (create-flow-edge configuration idx node))
                 (:edges configuration)))]
    (layout flow)))


(defn- dag-panel
  "show the DAG, built form the configuration passed into the component, in a panel
  (beside the actual UI)
  "
  [& {:keys [configuration component-id container-id ui]}]
  (let [config-flow (make-flow configuration)
        node-types  {":ui/component"  (partial custom-node :ui/component)
                     ":source/remote" (partial custom-node :source/remote)
                     ":source/local"  (partial custom-node :source/local)
                     ":source/fn"     (partial custom-node :source/fn)}]
    [:div {:style {:width "70%" :height "100%"}}
     [:> ReactFlowProvider
      [:> ReactFlow {:className        component-id
                     :elements         config-flow
                     :nodeTypes        node-types
                     :edgeTypes        {}
                     :zoomOnScroll     false
                     :preventScrolling false
                     :onConnect        #()}
       [:> Controls]]]]))


(defn- component-panel
  "show the UI, built form the configuration data passed to the component
  "
  [& {:keys [configuration component-id container-id ui]}]
  (let [layout     (:layout configuration)
        components (:components configuration)]
    [:div "The composed UI will display here"
     (map (fn [[node meta-data]]
            ^{:key node} [:p (str node)])
       (filter (fn [[node {:keys [type]}]]
                 (= :ui/component type))
            components))]))

;;endregion


(defn component
  "build a UI from a data structure (data), which provides the :components, :links between them,
  and the :layout of the physical UI-components on the display
  "
  [& {:keys [data component-id container-id ui]}]
  (let [id            (r/atom nil)
        configuration @data
        graph         (apply lg/digraph (compute-edges configuration))]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id (config @id data))
        (ui-utils/dispatch-local @id [:container] container-id))

      ;(log/info "coverage-plan" @id (config @id data))
      (let [full-config (assoc configuration
                          :graph graph
                          :denorm (denorm-components graph (:links configuration) (lg/nodes graph))
                          :nodes (lg/nodes graph)
                          :edges (lg/edges graph))]

        [rc/h-box :src (rc/at)
         :gap "20px"
         :width "1000px"
         :height "800px"
         :children [[dag-panel
                     :configuration full-config
                     :component-id @id
                     :container-id container-id
                     :ui ui]
                    [component-panel
                     :configuration full-config
                     :component-id @id
                     :container-id container-id
                     :ui ui]]]))))





(comment
  (def configuration @sample-data)
  (def graph (apply lg/digraph (compute-edges @sample-data)))
  (def denorm (denorm-components graph (:links @sample-data)
                (lg/nodes graph)))


  (assoc configuration
    :graph graph
    :denorm (denorm-components graph (:links configuration) (lg/nodes graph))
    :nodes (lg/nodes graph)
    :edges (lg/edges graph))


  ())


; basics of Loom (https://github.com/aysylu/loom)
(comment
  (do
    (def g (lg/graph [1 2] [2 3] {3 [4] 5 [6 7]} 7 8 9))
    (def dg (lg/digraph g))
    (def wg (lg/weighted-graph {:a {:b 10 :c 20} :c {:d 30} :e {:b 5 :d 5}}))
    (def wdg (lg/weighted-digraph [:a :b 10] [:a :c 20] [:c :d 30] [:d :b 10]))
    (def fg (lg/fly-graph :successors range :weight (constantly 77))))


  (lg/nodes g)
  (lg/edges g)
  (lg/has-node? g 5)
  (lg/weighted-graph g)

  (lg/nodes fg)

  ())


; how do we use Loom for our composite?
;
(comment
  ; a Loom digraph only needs EDGES (:links)
  (def edges (->> composite-def
               :links
               (mapcat (fn [[entity links]]
                         (map (fn [[target port]]
                                [entity target])
                           links)))
               (into [])))


  ; with THIS set of edges, sources and sinks all look like successors
  (def g (apply lg/digraph edges))

  ())


; playing with the graph
(comment
  (def graph (apply lg/digraph (compute-edges composite-def)))

  (apply conj
    (map #(create-flow-node %) (lg/nodes graph))
    (map-indexed (fn [idx node]
                   (create-flow-edge idx node))
      (lg/edges graph)))

  (def nodes (lg/nodes graph))

  (create-flow-node (first nodes))

  (apply conj '(:a :b) '(:c :d))


  (def config-graph (apply lg/digraph (compute-edges composite-def)))
  (def config-flow (make-flow config-graph))


  ())


; dagre
(comment
  (def graph (apply lg/digraph (compute-edges @sample-data)))
  (def dagreGraph (dagre-graph graph))

  (make-flow graph)

  (clj->js {:width 10 :height 10})
  (clj->js {:width :thing :height 10})

  (.node ("my-id"))


  ())



(def just-components
  {; ui components
   :ui/targets                {:type :ui/component :name :table/selectable-table}
   :ui/satellites             {:type :ui/component :name :table/selectable-table}
   :ui/globe                  {:type :ui/component :name :globe/three-d-globe}
   :ui/time-slider            {:type :ui/component :name :slider/slider}
   :ui/current-time           {:type :ui/component :name :label/label}

   ; remote data sources
   :topic/target-data         {:type :source/remote :name :source/targets}
   :topic/satellite-data      {:type :source/remote :name :source/satellites}
   :topic/coverage-data       {:type :source/remote :name :source/coverages}

   ; composite-local data sources
   :topic/selected-targets    {:type :source/local :name :selected-targets}
   :topic/selected-satellites {:type :source/local :name :selected-satellites}
   :topic/current-time        {:type :source/local :name :current-time}
   :topic/selected-coverages  {:type :source/local :name :selected-coverages}
   :topic/time-range          {:type :source/local :name :time-range}

   ; transformation functions
   :fn/coverage               {:type  :source/fn
                               :name  fn-coverage
                               :ports {:targets    :port/sink
                                       :satellites :port/sink
                                       :coverages  :port/sink
                                       :selected   :port/source}}
   :fn/range                  {:type  :source/fn
                               :name  fn-range
                               :ports {:data  :port/sink
                                       :range :port/source}}})

; layout!
(comment
  (def configuration @sample-data)
  (def links (:links configuration))
  (def layout (:layout configuration))
  (def components (:components configuration))

  (group-by :user-id [{:user-id 1 :uri "/"}
                      {:user-id 2 :uri "/foo"}
                      {:user-id 1 :uri "/account"}])

  (def comp-by-type (->> components
                      seq
                      (group-by (fn [[id meta]]
                                  (:type meta)))))

  (def links-by-type ())

  ; process local subscriptions
  (def local-meta [:topic/selected-coverages {:type :source/local,
                                              :name :selected-coverages}])

  {:selected-coverages (subscribe-local container-id :topic/selected-coverages)}


  ())


; piece together the data needed to build all the UI components and supporting functions
(comment
  (do
    (def configuration @sample-data)
    (def container-id "dummy")
    (def links (:links configuration))
    (def layout (:layout configuration))
    (def components (:components configuration))
    (def graph (apply lg/digraph (compute-edges configuration)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry))

  ; 1. build the functions... (how? where?)
  ;
  ; actually, since the functions "subscribe" to some inputs and then produce something
  ; that "others" subscribe to, they need to be "cascaded subscriptions" themselves,
  ; so we will actually build the subscriptions alongside step 2, using the data we assemble here
  ; (input signals, and the "subscription name(s)")
  ;
  ; NOTE: these functions need to produce ONE subscription for each :port/source
  ;
  ; for example,
  ;
  ;         {:fn/compute {:name some-computation
  ;                       :ports {:input :port/sink
  ;                               :computed-output :port/source}}}
  ;
  ; builds the equivalent of:
  ;
  ;         (re/frame/reg-sub-db
  ;           :container/blackboard.computed-output
  ;           :<- [:container/blackboard.input]
  ;           (fn [input [_ _]]
  ;              (some-computation* input))
  ;
  ;
  ; something like,
  ;
  ;         {:fn/compute {:name some-computation
  ;                       :ports {:input-1 :port/sink
  ;                               :input-2 :port/sink
  ;                               :computed-output :port/source}}}
  ;
  ; would build the equivalent of:
  ;
  ;         (re/frame/reg-sub-db
  ;           :container/blackboard.computed-output
  ;           :<- [:container/blackboard.input-1]
  ;           :<- [:container/blackboard.input-2]
  ;           (fn [input-1 input-2 [_ _]]
  ;              (some-computation* input-1 input-2))
  ;
  ;
  ;


  ; we could mix-in the "local name" for each link by mapping over the
  ; successors and predecessors

  ; predecessors
  (def source :ui/globe)
  (def target :topic/current-time)

  (lg/predecessors* graph :ui/globe)
  (->> links
    (filter (fn [[s targets]]
              {:s s :t targets})))
              ;(and (get targets source)
              ;  (= s target)))))
    ;vals
    ;first
    ;(#(get % source)))

  (get-predecessor-name links :ui/globe :topic/current-time)
  (get-predecessor-name links :fn/range :topic/coverage-data)

  (get-successor-name links :fn/range :topic/time-range)



  (denorm-components graph links nodes)

  ; GOT IT!
  ;
  ; we can now work from any node to its inputs and outputs,
  ; which means we can build the signal vectors for the ui elements


  ; QUESTION: should we mixin the notion of :local and :remote right here, so we can
  ; build the correct subscription/event signals?
  ;
  ; OR we can leave that logic to the function that actually builds the signal vectors (see
  ;    Step 2)
  ;        THIS requires looking at the component's meta-data
  ;
  ; OR we could leave it to the component itself to build the correct vector(s)
  ;





  ; 2. build the subscription and event signal vectors

  ; 3. build the components, passing them their "signals" (vectors)

  ; 4. render the components, using composite-layout/layout


  ())


; get the different handle names, so we can put multiple handles on a single node
; and then also connect the different edges to the correct one
(comment
  (def configuration @sample-data)
  (def node-id :ui/globe)
  (def target-id :topic/selected-coverages)

  (or (get-in configuration [:links node-id target-id])
    (get-in configuration [:links target-id node-id]))

  ())



; new logic for building the flow-nodes, so we can have custom node rendering
(comment
  (do
    (def node-id :fn/range)
    (def graph (apply lg/digraph (compute-edges @sample-data)))
    (def configuration
      (assoc @sample-data
        :graph graph
        :denorm (denorm-components graph (:links configuration) (lg/nodes graph))
        :nodes (lg/nodes graph)
        :edges (lg/edges graph))))

  (:denorm configuration)

  (get-in configuration [:denorm :fn/coverage :inputs])

  (def components (:components configuration))


  (map (fn [[node meta-data]]
         ^{:key node} [:p (str node)])
    (filter (fn [[node {:keys [type]}]]
              (= :ui/component type))
      components))

  (keys components)


  ())


