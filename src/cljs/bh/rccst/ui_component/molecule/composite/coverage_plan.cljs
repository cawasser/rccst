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
            ["react-flow-renderer" :refer (ReactFlowProvider Controls Handle Background) :default ReactFlow]))


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

  (let [some-computation (fn [t s c] [:layers])]
    ;["fn-coverage" {:targets targets :satellites satellites
    ;                :coverages coverages :layers layers}]))

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
  [& {:keys [data range]}]

  (let [some-computation (fn [x] [:range])]
    ;["fn-range" {:data data :range range}]))

    (re-frame/reg-sub
      range
      :<- [data]
      (fn [d [_ _]]
        (some-computation d)))))


;; components have "ports" which define their inputs and outputs:
;;
;;      you SUBSCRIBE to a :port/sink, ie, data goes IN     (re-frame/subscribe ...)
;;
;;      you PUBLISH to a :port/source, ie, data goes OUT    (re-frame/dispatch ...)
;;
;;      you do BOTH with :port/source-sink (both)           should we even have this?
;;
;; the question about :port/source-sink arises because building the layout (the call for the UI itself) we don't actually
;; need to make a distinction (in fact the code is a bit cleaner if we don't) and have the callee sort it out (since it
;; needs to implement the correct usage anyway). The flow-diagram, on the other hand, is easier if we DO make the
;; distinction, so we can quickly build all the Nodes and Handles used for the diagram...
;;
;;


(def meta-data-registry
  {
   :table/selectable-table {:component e/selectable-table
                            :ports     {:data      :port/source-sink ; out this be {:data-in :port/sink} & {:data-out :port/source}?
                                        :selection :port/source}}

   :globe/three-d-globe    {:component e/three-d-globe
                            :ports     {:layers       :port/sink
                                        :current-time :port/sink}}

   :slider/slider          {:component e/slider
                            :ports     {:value :port/source-sink
                                        :range :port/sink}}

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
                                         :topic/layers              {:type :source/local :name :layers}
                                         :topic/time-range          {:type :source/local :name :time-range}

                                         ; transformation functions
                                         :fn/coverage               {:type  :source/fn :name fn-coverage
                                                                     :ports {:targets   :port/sink :satellites :port/sink
                                                                             :coverages :port/sink :layers :port/source}}
                                         :fn/range                  {:type  :source/fn :name fn-range
                                                                     :ports {:data :port/sink :range :port/source}}}

                          :links        {; components publish to what? via which port?
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
                                         :fn/coverage               {:layers {:topic/layers :data}}
                                         :fn/range                  {:range {:topic/time-range :data}}

                                         ; topics are inputs into what?
                                         :topic/target-data         {:data {:ui/targets :data}}
                                         :topic/satellite-data      {:data {:ui/satellites :data}}
                                         :topic/selected-targets    {:data {:fn/coverage :targets}}
                                         :topic/selected-satellites {:data {:fn/coverage :satellites}}
                                         :topic/coverage-data       {:data {:fn/coverage :coverages
                                                                            :fn/range    :data}}
                                         :topic/layers              {:data {:ui/globe :coverages}}
                                         :topic/current-time        {:data {:ui/current-time :value
                                                                            :ui/time-slider  :value
                                                                            :ui/globe        :current-time}}
                                         :topic/time-range          {:data {:ui/time-slider :range}}}

                          :layout       [:v-box
                                         [:h-box
                                          [:v-box [:ui/targets :ui/satellites :ui/time-slider]
                                           :v-box [:ui/globe :ui/current-time]]]]}))


(def source-code '[coverage-plan])


(defn- config [_ _]
  {})



; we want to turn the composite-def into things like...
;
(comment
  {:fn/coverage     (fn-coverage
                      :targets [:coverage-plan/blackboard.topic.selected-targets]
                      :satellites [:coverage-plan/blackboard.topic.selected-satellites]
                      :coverages [:bh.rccst.subs/source :topic/coverages]
                      :layers [:coverage-plan/blackboard.topic.layers])

   :fn/range        (fn-range
                      :data [:bh.rccst.subs/source :topic/coverages]
                      :selected [:coverage-plan/blackboard.topic.time-range])

   :ui/targets      [e/selectable-table
                     :component-id :coverage-plan/targets
                     :container-id :coverage-plan
                     :data [:bh.rccst.subs/source :topic/target-data]
                     :selected [:coverage-plan/blackboard.topic.selected-targets]]

   :ui/satellites   [e/selectable-table
                     :component-id :coverage-plan/satellites
                     :container-id :coverage-plan
                     :data [:bh.rccst.subs/source :topic/satellite-data]
                     :selected [:coverage-plan/blackboard.topic.selected-targets]]

   :ui/globe        [e/globe
                     :component-id :coverage-plan/globe
                     :container-id :coverage-plan
                     :layers [:coverage-plan/blackboard.topic.layers]
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


(defn- make-params [configuration node direction container-id]
  (->> configuration
    :denorm
    node
    direction
    (map (fn [[target ports]]
           (let [[source-port target-port] ports]
             (println target (-> configuration :components target :type))
             (if (= direction :outputs)
               {source-port (if (= :source/local (-> configuration :components target :type))
                              (ui-utils/path->keyword container-id :backboard target)
                              target)}
               {target-port (if (= :source/local (-> configuration :components target :type))
                              (ui-utils/path->keyword container-id :backboard target)
                              target)}))))
    (into {})))


(comment
  (do
    (def config @sample-data)
    (def container-id "dummy")
    (def links (:links config))
    (def layout (:layout config))
    (def components (:components config))
    (def graph (apply lg/digraph (compute-edges config)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)
    (def configuration (assoc @sample-data
                         :graph graph
                         :denorm (denorm-components graph (:links config) (lg/nodes graph))
                         :nodes (lg/nodes graph)
                         :edges (lg/edges graph)))
    (def node :fn/coverage)
    (def direction :inputs))

  (make-params configuration :fn/coverage :inputs :dummy)

  (->> configuration
    :denorm
    node
    direction)

  (->> configuration
    :denorm
    node
    direction
    (map (fn [[target ports]]
           (let [[source-port target-port] ports]
             (println target (-> configuration :components target :type))
             (if (= direction :outputs)
               {source-port (if (= :source/local (-> configuration :components target :type))
                              (ui-utils/path->keyword container-id :backboard target)
                              target)}
               {target-port (if (= :source/local (-> configuration :components target :type))
                              (ui-utils/path->keyword container-id :backboard target)
                              target)}))))
    (into {}))


  ())


(defmulti component->ui (fn [{:keys [type]}]
                          type))


(defmethod component->ui :ui/component [{:keys [node registry configuration container-id]}]
  ;(log/info "component->ui :ui/component" node)
  (let [ui-type      (->> configuration :components node :name)
        ui-component (->> registry ui-type :component)]
    {node
     (into [ui-component]
       (flatten
         (seq
           (merge
             (make-params configuration node :inputs container-id)
             (make-params configuration node :outputs container-id)))))}))


(defmethod component->ui :source/local [{:keys [node component-id container-id]}]
  (log/info "component->ui :source/local" node)

  ; 1. add the key to the blackboard (what about a default value?)
  (re-frame/dispatch-sync [:events/init-widget-locals node {}])

  ; 2. create the subscription against the new :blackboard key
  (ui-utils/create-widget-local-sub component-id [:blackboard node])

  ; 3. create the event against the new :blackboard key
  (ui-utils/create-widget-local-event component-id [:blackboard node])

  ; 3. return the signal vector for the new subscription
  [(ui-utils/path->keyword container-id node)])


(defmethod component->ui :source/remote [{:keys [node]}]
  (log/info "component->ui :source/remote" node)

  ; 1. subscribe to the server (if needed)
  (re-frame/dispatch [:bh.rccst.events/subscribe-to #{node}])

  ; 2. add the key to the app-db (I think this happens in step 2)

  ; 3. return the signal vector to the new data-source key
  [:bh.rccst.subs/source node])


(defmethod component->ui :source/fn [{:keys [node configuration container-id]}]
  (let [actual-fn (->> configuration :components node :name)
        denorm    (->> configuration :denorm node)]

    ;(log/info "component->ui :source/fn" node "//" actual-fn "//" denorm)

    (apply actual-fn
      (flatten
        (seq
          (merge
            (make-params configuration node :inputs container-id)
            (make-params configuration node :outputs container-id)))))))


(defn- process-components [configuration type registry container-id]
  (->> configuration
    :components
    (filter (fn [[_ meta-data]]
              (= type (:type meta-data))))
    (map (fn [[node meta-data]]
           (component->ui {:node          node
                           :type          (:type meta-data)
                           :configuration configuration
                           :registry      registry
                           :container-id  container-id})))))

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


(defn- get-predecessor-name [links graph source target]
  ;(log/info "pred" source target "//" graph)
  (->> links
    (filter (fn [[s _]]
              (and (contains? (lg/predecessors* graph source) s)
                (= s target))))
    vals
    first
    keys
    first))


(defn- get-successor-name [links graph source target]
  (->> links
    source
    (filter (fn [[s _]]
              (contains? (lg/successors* graph source) target)))
    vals
    first
    vals
    first))
;(->> links
;  source
;  target))


(defn denorm-components-obe
  "denormalize the links between components by mixing in additional information bout the
  ports at both ends of the inter-connection:

  {<source> {:inputs  {<target> [<source's-port> <target's-port>]}
            {:outputs {<target> [<source's-port> <target's-port>]}}


  "
  [graph links nodes]
  (->> nodes
    (map (fn [node]
           (let [node-meta (->> links node)]
             {node {:inputs
                    (into {}
                      (map (fn [target]
                             ; TODO: what the heck should replace :dummy?!?!?!?!
                             {target [:dummy (get-predecessor-name links graph node target)]})
                        (lg/predecessors* graph node)))

                    :outputs
                    (into {}
                      (map (fn [target]
                             ; TODO: what the heck should replace :dummy?!?!?!?!
                             {target [:dummy (get-successor-name links graph node target)]})
                        (lg/successors* graph node)))}})))
    (into {})))


(defn- get-inputs
  "get all the inputs to the given node (these are 'predecessors')

  we grab the node's predecessors, and format the data correctly:

  {<source> [<node's-port> <source's-port>]
   <source> [<node's-port> <source's-port>]}


  WORK-IN-PROGRESS
  "
  [links graph node]
  (->> node
    (lg/predecessors* graph)
    (map (fn [p]
           ; 1. grab the target meta-data for each source
           (apply merge
             (map (fn [[source-port targets]]
                    (let [target-port (get targets node)]
                      {p [source-port target-port]}))
               (get links p)))))
    (into {})))


(defn- get-outputs [links node]
  "get all the outputs of the given node

  these are given directly by the links, but need reformatting from:

  {<source's-port {<target> <target's-port>
                   <target> <target's-port>}}

  to:

  {<target> [<node's-port> <target's-port>]
   <target> [<node's-port> <target's-port>]}


  WORK-IN-PROGRESS
  "
  (->> links
    node
    (map (fn [[node-port target-meta]]
           (apply merge
             (map (fn [[target target-port]]
                    {target [node-port target-port]})
               target-meta))))
    (apply merge)))


(defn denorm-components
  "denormalize the links between components by mixing in additional information bout the
  ports at both ends of the inter-connection:

  {<node> {:inputs  {<source> [<node's-port> <source's-port>]
                     <source> [<node's-port> <source's-port>]}
           :outputs {<target> [<node's-port> <target's-port>]
                     <target> [<node's-port> <target's-port>]}
           :params  {<source> [<node's-port> <source's-port>]
                     <source> [<node's-port> <source's-port>]
                     <target> [<node's-port> <target's-port>]
                     <target> [<node's-port> <target's-port>]}}

  WORK-IN-PROGRESS
  "
  [graph links nodes]
  (->> nodes
    (map (fn [node]
           {node
            {:inputs  (get-inputs links graph node)
             :outputs (get-outputs links node)
             :params  {}}}))
    (into {})))


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

    ;(log/info "custom-node" label data "///" inputs "///" outputs)

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


(defn- build-layout
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


(defn- create-flow-edge
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
    (build-layout flow)))


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
    [:div {:style {:width "100%" :height "100%"}}
     [:> ReactFlowProvider
      [:> ReactFlow {:className        component-id
                     :elements         config-flow
                     :nodeTypes        node-types
                     :edgeTypes        {}
                     :zoomOnScroll     false
                     :preventScrolling false
                     :onConnect        #()}
       [:> Background]
       [:> Controls]]]]))


(defn- component-panel
  "show the UI, built form the configuration data passed to the component
  "
  [& {:keys [configuration component-id container-id ui]}]
  (let [layout      (:layout configuration)
        components  (:components configuration)
        composed-ui ()]

    (fn [& {:keys [configuration component-id container-id ui]}]
      [rc/v-box
       :style {:textAlign :center}
       :gap "15px"
       :width "100%" :height "100%"
       :children [[:h2 "The composed UI will display here"]
                  [rc/line :size "2px" :color "blue"]
                  (into [:<>]
                    (map (fn [[node meta-data]]
                           ^{:key node} [:h3 (str node)])
                      (filter (fn [[node {:keys [type]}]]
                                (= :ui/component type))
                        components)))]])))

;;endregion


(defn component
  "build a UI from a data structure (data), which provides the :components, :links between them,
  and the :layout of the physical UI-components on the display
  "
  [& {:keys [data component-id container-id ui]}]
  (let [id            (r/atom nil)
        configuration @data
        graph         (apply lg/digraph (compute-edges configuration))
        comp-or-dag?  (r/atom :component)]

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
         :width "1000px"
         :height "800px"
         :justify :end
         :children [(condp = @comp-or-dag?
                      :dag [dag-panel
                            :configuration full-config
                            :component-id @id
                            :container-id container-id
                            :ui ui]
                      :component [component-panel
                                  :configuration full-config
                                  :component-id @id
                                  :container-id container-id
                                  :ui ui]
                      :default [rc/alert-box :src (rc/at)
                                :alert-type :warning
                                :body "There is a problem with this component."])
                    [rc/md-icon-button
                     :md-icon-name (if (= :component @comp-or-dag?)
                                     "zmdi-settings"
                                     "zmdi-view-compact")
                     :tooltip (if (= :component @comp-or-dag?)
                                "view the DAG for this component"
                                "view the component as it is rendered")
                     :on-click #(do
                                  (reset! comp-or-dag? (if (= :component @comp-or-dag?)
                                                         :dag
                                                         :component)))]]]))))





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


;; basics of Loom (https://github.com/aysylu/loom)
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


;; how do we use Loom for our composite?

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


;; playing with the graph
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


;; dagre
(comment
  (def graph (apply lg/digraph (compute-edges @sample-data)))
  (def dagreGraph (dagre-graph graph))

  (make-flow graph)

  (clj->js {:width 10 :height 10})
  (clj->js {:width :thing :height 10})

  (.node ("my-id"))


  ())


;; layout!
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


  (do
    (def graph (apply lg/digraph (compute-edges @sample-data)))
    (def configuration
      (assoc @sample-data
        :graph graph
        :denorm (denorm-components graph (:links configuration) (lg/nodes graph))
        :nodes (lg/nodes graph)
        :edges (lg/edges graph))))
  (def config-flow (make-flow configuration))

  ())


;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;
;
; THIS IS THE ONE!!!
;
;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;
;; piece together the data needed to build all the UI components and supporting functions
(comment
  (do
    (def config @sample-data)
    (def container-id "dummy")
    (def links (:links config))
    (def layout (:layout config))
    (def components (:components config))
    (def graph (apply lg/digraph (compute-edges config)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)

    (def configuration (assoc @sample-data
                         :graph graph
                         :denorm (denorm-components graph (:links config) (lg/nodes graph))
                         :nodes (lg/nodes graph)
                         :edges (lg/edges graph))))

  ;; 1. build the functions... (how? where?)
  ;; region
  ;;
  ;; actually, since the functions "subscribe" to some inputs and then produce something
  ;; that "others" subscribe to, they need to be "cascaded subscriptions" themselves,
  ;; so we will actually build the subscriptions alongside step 2, using the data we assemble here
  ;; (input signals, and the "subscription name(s)")
  ;;
  ;; NOTE: these functions need to produce ONE subscription for each :port/source
  ;;
  ;; for example,
  ;;
  ;;         {:fn/compute {:name some-computation
  ;;                       :ports {:input :port/sink
  ;;                               :computed-output :port/source}}}
  ;;
  ;; builds the equivalent of:
  ;;
  ;;         (re/frame/reg-sub-db
  ;;           :container/blackboard.computed-output
  ;;           :<- [:container/blackboard.input]
  ;;           (fn [input [_ _]]
  ;;              (some-computation* input))
  ;;
  ;;;
  ;; something like,
  ;;
  ;;         {:fn/compute {:name some-computation
  ;;                       :ports {:input-1 :port/sink
  ;;                               :input-2 :port/sink
  ;;                               :computed-output :port/source}}}
  ;;
  ;; would build the equivalent of:
  ;;
  ;;         (re/frame/reg-sub-db
  ;;           :container/blackboard.computed-output
  ;;           :<- [:container/blackboard.input-1]
  ;;           :<- [:container/blackboard.input-2]
  ;;           (fn [input-1 input-2 [_ _]]
  ;;              (some-computation* input-1 input-2))
  ;;
  ;; endregion


  ;; we could mix-in the "local name" for each link by mapping over the
  ;; successors and predecessors

  ;; actually, now it looks like :links already has all the names we need, we just need to
  ;; make the distinction between :inputs and :outputs for the flow-diagram (the UI Layout doesn't
  ;; need this data) But, I think we can use this function to split the flow-diagram data from the
  ;; UI-layout data (and putting BOTH into the expanded data configuration structure)


  ;; let's just use the terms :inputs and :outputs and drop the preds/succs

  ;; outputs
  ;; region
  (def source :fn/coverage)

  (->> links
    source
    (map (fn [[source-port target-meta]]
           (apply merge
             (map (fn [[target target-port]]
                    {target [source-port target-port]})
               target-meta))))
    (apply merge))


  {:fn/range {:outputs (get-outputs links :fn/range)}}
  (get-outputs links :fn/coverage)

  ; map over all the cmoponets (:ui/globe & :ui/current-time should have not outputs!)
  (->> nodes
    (map (fn [node]
           {node (get-outputs links node)})))


  ;; endregion


  ;; inputs
  ;; region
  (def node :ui/globe)


  ; find components with :ui/globe as a target (ie. predecessors of :ui/globe
  (def preds (lg/predecessors* graph source))

  ; then map over then and pull out their source-port and the target meta-data for
  ; :ui/globe

  (->> node
    (lg/predecessors* graph)
    (map (fn [p]
           (apply merge
             (map (fn [[source-port targets]]
                    (let [target-port (get targets node)]
                      {p [source-port target-port]}))
               (get links p)))))
    (into {}))

  (:topic/coverage-data links)

  (get-inputs links graph :ui/globe)
  (get-inputs links graph :fn/range)


  (->> nodes
    (map (fn [node]
           {node (get-inputs links graph node)})))

  ;; endregion


  ;; now put is all together
  ;; region

  (->> nodes
    (map (fn [node]
           {node
            {:inputs  (get-inputs links graph node)
             :outputs (get-outputs links node)}}))
    (into {}))

  (denorm-components-2 graph links nodes)

  ;; endregion

  ; GOT IT!
  ;
  ; we can now work from any node to its inputs and outputs,
  ; which means we can build the signal vectors for the ui elements
  ;
  ; AND a react-flow diagram of the event-mode for the UI!
  ;


  ; QUESTION: should we mix-in the notion of :local and :remote right here, so we can
  ; build the correct subscription/event signals?
  ;
  ; OR we can leave that logic to the function that actually builds the signal vectors (see
  ;    Step 2)
  ;        THIS requires looking at the component's meta-data
  ;
  ; OR we could leave it to the component itself to build the correct vector(s)
  ;
  ; what about the flow-diagram?
  ;


  ; 2. build the subscription and event signal vectors (just call them)
  ;; region
  (defn dummy [& {:keys [data range]}]
    {:data data :range range})

  (def target :topic/coverage-data)
  (def thing {:data [:topic/coverage-data], :range [:topic/time-range]})

  (flatten (seq thing))

  (apply conj [:dummy] (flatten (seq thing)))



  (make-params configuration :fn/range :inputs :dummy)
  (make-params configuration :fn/range :outputs :dummy)


  (make-params configuration :fn/coverage :inputs :dummy)

  (apply fn-range
    (flatten (seq (merge
                    (make-params configuration :fn/range :inputs :dummy)
                    (make-params configuration :fn/range :outputs :dummy)))))


  (->> components
    (filter (fn [[node meta-data]]
              (= :ui/component (:type meta-data))))
    (map (fn [[node meta-data]]
           (component->ui {:node          node
                           :type          (:type meta-data)
                           :configuration configuration
                           :registry      meta-data-registry
                           :container-id  :dummy}))))

  ;; endregion


  ; the correct order of operations is:
  ;
  ; 1. remote subscriptions (including the remote call)
  ;
  ; [SIDE EFFECT]
  (process-components configuration :source/remote meta-data-registry :coverage-plan)

  ; 1a. build the subscription for the "container" which provide the basis for the
  ;     subscriptions for the "locals"
  ;
  ; [SIDE EFFECT]
  (ui-utils/create-widget-sub container-id)


  ; 2. add blackboard data to the app-db and build local subscriptions/events against the blackboard
  ;
  ; [SIDE EFFECT]
  (process-components configuration :source/local meta-data-registry :coverage-plan)

  ; 3. local functions (to build subscriptions against the blackboard or remotes)
  ;
  ; [SIDE EFFECT]
  (process-components configuration :source/fn meta-data-registry :coverage-plan)

  ; 4. build UI components (with subscriptions against the blackboard or remotes)
  ;
  ;      actually, this can happen at any time, since evaluation is deferred to Reagent upon re-render
  ;
  ; this just builds the vectors and maps them to the component-id in the configuration in pre for Step 5
  ;
  (def component-lookup (into {}
                          (process-components
                            configuration :ui/component
                            meta-data-registry :coverage-plan)))

  ; 5. run layout over the UI components using component-lookup
  ;



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


; work out the "new" logic for building the :layout
(comment
  (do
    (def config @sample-data)
    (def container-id "dummy")
    (def links (:links config))
    (def layout (:layout config))
    (def components (:components config))
    (def graph (apply lg/digraph (compute-edges config)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)

    (def configuration (assoc @sample-data
                         :graph graph
                         :denorm (denorm-components graph (:links config) (lg/nodes graph))
                         :nodes (lg/nodes graph)
                         :edges (lg/edges graph)
                         :ui (into {}
                               (process-components
                                 configuration :ui/component
                                 meta-data-registry :coverage-plan)))))

  ; in a more 'tree format, the layout looks like this:
  ;
  ; |-v-box ---------------------------------------------|
  ; | |--------------------------------------------------|
  ; | | h-box                                            |
  ; | | |-v-box ---------------| |-v-box ----------------|
  ; | | |    [:ui/targets]     | |   [:ui/globe]         |
  ; | | |    [:ui/satellites]  | |   [:ui/current-time]  |
  ; | | |    [:ui/time-slider] | |                       |
  ; |-|-|----------------------|-|-----------------------|

  (:ui configuration)
  (:layout configuration)


  (def layout [:v-box
               [:h-box
                [:v-box [[:ui/targets] [:ui/satellites] [:ui/time-slider]]]
                [:v-box [[:ui/globe] [:ui/current-time]]]]])

  (def should-be [:v-box
                  :children [[:h-box
                              :children [[:v-box
                                          :children [[:ui/targets]
                                                     [:ui/satellites]
                                                     [:ui/time-slider]]]
                                         [:v-box
                                          :children [[:ui/globe]
                                                     [:ui/current-time]]]]]]])

  (defn process-ui [tree]
    (let [[node children] tree
          siblings (and (vector? node))
                     ;(or (= :v-box (first node)) (= :h-box (first node))))
          branch?  (and (vector? children)
                     (or (= :v-box node) (= :h-box node)))]
      (cond
        branch? (do
                  (println "branch" node "///" children)
                  [node :children [(process-ui children)]])
        siblings (do
                   (println "siblings" tree)
                   ; this mapv adds an extra '[]' which we don't need,
                   ; but how to get rid of it?
                   (mapv process-ui tree))
        :else (do
                (println "leaf" tree)
                tree))))

  (def tree [:v-box [:h-box [:c :d]]])
  (process-ui tree)

  (def tree2 [:v-box
              [:h-box
               [[:v-box [:c :d]]
                [:v-box [:e :f]]]]])
  (process-ui tree2)

  (def produces [:v-box
                 :children [[:h-box
                             :children [[:c] [:d]]]]])



  ())
