(ns bh.rccst.ui-component.molecule.composite
  "provides a \"container\" to hold and organize other atoms and molecules
  components have \"ports\" which define their inputs and outputs:

      you SUBSCRIBE with a :port/sink, ie, data come IN   (re-frame/subscribe ...)

      you PUBLISH to a :port/source, ie, data goes OUT    (re-frame/dispatch ...)

      you do BOTH with :port/source-sink (both)           should we even have this, or should we spell out both directions?

  the question about :port/source-sink arises because building the layout (the call for the UI itself) doesn't actually
  need to make a distinction (in fact the code is a bit cleaner if we don't) and we have the callee sort it out (since it
  needs to implement the correct usage anyway). The flow-diagram, on the other hand, is easier if we DO make the
  distinction, so we can quickly build all the Nodes and Handles used for the diagram...
  "
  (:require [bh.rccst.ui-component.molecule.composite.util.signals :as sig]
            [bh.rccst.ui-component.molecule.composite.util.digraph :as dig]
            [bh.rccst.ui-component.molecule.composite.util.ui :as ui]
            [bh.rccst.ui-component.molecule.component-layout :as cl]
            [bh.rccst.ui-component.atom.experimental.ui-element :as e]
            [bh.rccst.ui-component.table :as real-table]
            [bh.rccst.ui-component.utils :as ui-utils]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [loom.graph :as lg]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.layout :as layout]
            ["dagre" :as dagre]
            ["graphlib" :as graphlib]
            ["react-flow-renderer" :refer (ReactFlowProvider Controls Handle Background) :default ReactFlow]))


(re-frame/reg-event-db
  ::add-component
  (fn-traced [db [_ id component]]
    (update-in db [:widgets (keyword id) :components] (partial apply conj) component)))


(def meta-data-registry
  (merge
    real-table/table-meta-data
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
                              :ports     {:value :port/sink}}}))


(def source-code '[composite
                   :data component/ui-definition
                   :component-id :container.component
                   :container-id :container])


(defn config []
  {})





;;;;;;;;;;
;;;;;;;;;;
;
;  UI Support Functions
;
;;;;;;;;;;
;;;;;;;;;;
;; region



(defn- definition-panel
  "show the text definition of the composed UI
  "
  [& {:keys [configuration]}]


  (let [components (:components configuration)
        links      (:links configuration)
        layout     (:layout configuration)]

    ;(log/info "definition-panel" components)
    ;(log/info "definition-panel" links)
    ;(log/info "definition-panel" layout)

    (fn [& {:keys [configuration]}]
      [rc/v-box :src (rc/at)
       :width "70%"
       :height "100%"
       :gap "10px"
       :children [[:h3 "Components"]
                  [containers/v-scroll-pane {:height "10em"}
                   [layout/text-block (str components)]]

                  [:h3 "Links"]
                  [containers/v-scroll-pane {:height "10em"}
                   [layout/text-block (str links)]]

                  [:h3 "Layout"]
                  [containers/v-scroll-pane {:height "10em"}
                   [layout/text-block (str layout)]]]])))


(defn- dag-panel
  "show the DAG, built form the configuration passed into the component, in a panel
  (beside the actual UI)
  "
  [& {:keys [configuration component-id container-id ui]}]
  (let [config-flow (ui/make-flow configuration)
        node-types  {":ui/component"  (partial ui/custom-node :ui/component)
                     ":source/remote" (partial ui/custom-node :source/remote)
                     ":source/local"  (partial ui/custom-node :source/local)
                     ":source/fn"     (partial ui/custom-node :source/fn)}]
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


(defn stand-in [components]
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
                    components)))]])


(defn- component-panel
  "show the UI, built from the configuration data passed to the component
  "
  [& {:keys [configuration component-id container-id]}]
  (let [layout           (:layout configuration)
        components       (:components configuration)
        component-lookup (into {}
                           (sig/process-components
                             configuration :ui/component
                             meta-data-registry component-id))

        ; 1. build UI components (with subscription/event signals against the blackboard or remotes)
        composed-ui      (sig/process-ui component-lookup [] layout)]

    (log/info "component-panel" component-id "//" composed-ui)

    (fn [& {:keys [configuration component-id container-id]}]

      ; 5. return the composed component layout!
      [:div {:style {:width "1000px" :height "100%"}}
       composed-ui])))
;[stand-in components]])))


(defn component
  "build a UI from a data structure (data), which provides the :components, :links between them,
  and the :layout of the physical UI-components on the display
  "
  [& {:keys [data component-id container-id]}]
  (let [id            (r/atom nil)
        configuration @data
        graph         (apply lg/digraph (ui/compute-edges configuration))
        comp-or-dag?  (r/atom :component)
        full-config   (assoc configuration
                        :graph graph
                        :denorm (dig/denorm-components graph (:links configuration) (lg/nodes graph))
                        :nodes (lg/nodes graph)
                        :edges (lg/edges graph))]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id {:blackboard {} :container ""})
        (ui-utils/dispatch-local @id [:container] container-id)

        (ui/prep-environment full-config @id meta-data-registry))

      (let [buttons [{:id :component :label [:i {:class "zmdi zmdi-view-compact"}]}
                     {:id :dag :label [:i {:class "zmdi zmdi-share"}]}
                     {:id :definition :label [:i {:class "zmdi zmdi-format-subject"}]}]]

        [rc/h-box :src (rc/at)
         :width "1000px"
         :height "800px"
         :justify :end
         :children [(condp = @comp-or-dag?
                      :dag [dag-panel
                            :configuration full-config
                            :component-id @id
                            :container-id container-id]
                      :component [component-panel
                                  :configuration full-config
                                  :component-id @id
                                  :container-id container-id]
                      :definition [definition-panel
                                   :configuration configuration]
                      :default [rc/alert-box :src (rc/at)
                                :alert-type :warning
                                :body "There is a problem with this component."])

                    [rc/horizontal-bar-tabs
                     :model comp-or-dag?
                     :tabs buttons
                     :on-change #(reset! comp-or-dag? %)]]]))))


(defn composite
  "make the composite:

  1. sets up the app-db to hold the local state, specifically the `:components`
  2. stores the children as `:components` in local state
  2. organizes the children onto the display

  ---

  - id : (string) unique id for this composite
  - components : (vector) vector of hiccup components (atoms or molecules) to manage and display

  Returns - (hiccup) a single reagent component (equivalent to a `:div`)
  "
  [& {:keys [id components]}]

  [cl/layout components])




; RICH COMMENTS
;; region

(comment
  (def configuration @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
  (def graph (apply lg/digraph (compute-edges @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)))
  (def denorm (denorm-components graph (:links @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
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
  (def graph (apply lg/digraph (compute-edges @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)))
  (def dagreGraph (dagre-graph graph))

  (make-flow graph)

  (clj->js {:width 10 :height 10})
  (clj->js {:width :thing :height 10})

  (.node ("my-id"))


  ())


;; layout!
(comment
  (def configuration @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
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
    (def graph (apply lg/digraph (compute-edges @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)))
    (def configuration
      (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
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
    (def config @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
    (def container-id "dummy")
    (def links (:links config))
    (def layout (:layout config))
    (def components (:components config))
    (def graph (apply lg/digraph (compute-edges config)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)

    (def configuration (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
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
  (def configuration @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
  (def node-id :ui/globe)
  (def target-id :topic/selected-coverages)

  (or (get-in configuration [:links node-id target-id])
    (get-in configuration [:links target-id node-id]))

  ())


; new logic for building the flow-nodes, so we can have custom node rendering
(comment
  (do
    (def node-id :fn/range)
    (def graph (apply lg/digraph (compute-edges @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)))
    (def configuration
      (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
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
    (def data @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
    (def graph (apply lg/digraph (compute-edges @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)))
    (def nodes (lg/nodes graph))
    (def links (:links data))
    (def components (:components data))
    (def configuration (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
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
    (def config @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
    (def container-id "dummy")
    (def links (:links config))
    (def layout (:layout config))
    (def components (:components config))
    (def graph (apply lg/digraph (compute-edges config)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)

    (def configuration (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
                         :graph graph
                         :denorm (denorm-components graph (:links config) (lg/nodes graph))
                         :nodes (lg/nodes graph)
                         :edges (lg/edges graph)
                         :ui-lookup (into {}
                                      (process-components
                                        configuration :ui/component
                                        meta-data-registry :coverage-plan))))
    (def component-lookup (into {}
                            (process-components
                              configuration :ui/component
                              meta-data-registry :coverage-plan))))

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

  (:ui-lookup configuration)
  (:layout configuration)


  ;(def layout [:v-box
  ;             [[:h-box
  ;               [[:v-box [:ui/targets :ui/satellites :ui/time-slider]]
  ;                [:v-box [:ui/globe :ui/current-time]]]]]])

  (def should-be [:v-box
                  :children
                  [[:h-box
                    :children
                    [[:v-box :children [:ui/targets :ui/satellites :ui/time-slider]]
                     [:v-box :children [:ui/globe :ui/current-time]]]]]])

  ;(defn process-ui [lookup a tree]
  ;  (let [[node children] tree
  ;        siblings? (and (vector? node))
  ;        ;(or (= :v-box (first node)) (= :h-box (first node))))
  ;        branch?   (and (vector? children)
  ;                    (or (= :v-box node) (= :h-box node)))]
  ;    (cond
  ;      branch? (do
  ;                (println "branch" node "///" children)
  ;                (apply conj a [node :children (process-ui lookup [] children)]))
  ;      siblings? (do
  ;                  (println "siblings" tree)
  ;                  ; this mapv adds an extra '[]' which we don't need,
  ;                  ; but how to get rid of it?
  ;                  (apply conj a (mapv #(process-ui lookup [] %) tree)))
  ;      :else (do
  ;              (println "leaf" tree)
  ;              tree))))

  (= (process-ui component-lookup [] layout)
    should-be)




  (def tree [:v-box [[:h-box [:c :d]]]])
  (process-ui component-lookup [] tree)
  (def produces [:v-box
                 :children [[:h-box
                             :children [:c :d]]]])


  (def tree2 [:v-box
              [[:h-box
                [[:v-box [:c :d :e]]
                 [:v-box [:f :g]]]]]])
  (process-ui component-lookup [] tree2)

  (def produces2 [:v-box
                  :children
                  [[:h-box
                    :children [[:v-box
                                :children [:c :d :e]]
                               [:v-box
                                :children [:f :g]]]]]])



  (def produces-layout [:v-box
                        :children [[:h-box
                                    :children [[:v-box
                                                :children [:ui/targets :ui/satellites :ui/time-slider]]
                                               [:v-box
                                                :children [:ui/globe :ui/current-time]]]]]])


  ())


; token substitution
(comment
  (do
    (def config @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
    (def container-id "dummy")
    (def links (:links config))
    (def layout (:layout config))
    (def components (:components config))
    (def graph (apply lg/digraph (compute-edges config)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)

    (def configuration (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
                         :graph graph
                         :denorm (denorm-components graph (:links config) (lg/nodes graph))
                         :nodes (lg/nodes graph)
                         :edges (lg/edges graph)
                         :ui-lookup (into {}
                                      (process-components
                                        configuration :ui/component
                                        meta-data-registry :coverage-plan))))
    (def component-lookup (into {}
                            (process-components
                              configuration :ui/component
                              meta-data-registry :coverage-plan)))
    (def node :h-box))

  (:ui-lookup configuration)

  (parse-token component-lookup node)

  (into (parse-token lookup node) [:children [:a :b :c]])


  ())


; make-param
(comment
  (do
    (def config @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
    (def container-id "dummy")
    (def links (:links config))
    (def layout (:layout config))
    (def components (:components config))
    (def graph (apply lg/digraph (ui/compute-edges config)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)
    (def configuration (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
                         :graph graph
                         :denorm (dig/denorm-components graph (:links config) (lg/nodes graph))
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
           (let [remote (-> configuration :components target :name)]
             [target remote]))))

  ; make-param
  (->> configuration
    :denorm
    node
    direction
    (map (fn [[target ports]]
           (let [[source-port target-port] ports
                 remote (-> configuration :components target :name)]
             ;(println target (-> configuration :components target :type))
             (if (= direction :outputs)
               {source-port (if (= :source/local (-> configuration :components target :type))
                              [(ui-utils/path->keyword container-id :blackboard target)]
                              [:bh.rccst.subs/source remote])}
               {target-port (if (= :source/local (-> configuration :components target :type))
                              [(ui-utils/path->keyword container-id :blackboard target)]
                              [:bh.rccst.subs/source remote])}))))
    (into {}))


  ; process-component :ui/component
  (do
    (def node :ui/targets)
    (def ui-type (->> configuration :components node :name))
    (def ui-component (->> registry ui-type :component))
    (def container-id "dummy"))

  (let [ui-type      (->> configuration :components node :name)
        ui-component (->> registry ui-type :component)]
    {node
     (reduce into [ui-component]
       (seq
         (merge
           (make-params configuration node :inputs container-id)
           (make-params configuration node :outputs container-id))))})

  (def params '([:data [:one :two]] [:thing [:one :three]]))

  (reduce into [] params)

  (reduce into [:node] '([:data [:one :two]] [:thing [:one :three]]))

  ())


; process-components
(comment
  (do
    (def data @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
    (def container-id "coverage-plan-demo")
    (def component-id :coverage-plan-demo.component)
    (def links (:links data))
    (def layout (:layout data))
    (def components (:components data))
    (def graph (apply lg/digraph (compute-edges data)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)
    (def configuration (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
                         :graph graph
                         :denorm (denorm-components graph (:links data) (lg/nodes graph))
                         :nodes (lg/nodes graph)
                         :edges (lg/edges graph)))
    (def source-type :source/local)
    (def node-type :source/local))



  (->> configuration
    :components
    (filter (fn [[_ meta-data]]
              (= node-type (:type meta-data))))
    (map (fn [[node meta-data]]
           ;(log/info "process-components (nodes)" node "//" meta-data "//" (:type meta-data))
           (component->ui {:node          node
                           :type          (:type meta-data)
                           :configuration configuration
                           :registry      registry
                           :component-id  (ui-utils/path->keyword container-id node)
                           :container-id  container-id}))))





  (process-components configuration :source/local meta-data-registry component-id)


  (ui-utils/init-widget component-id {:blackboard {}})

  (ui-utils/create-widget-local-sub component-id [:container])
  (ui-utils/create-widget-local-event component-id [:container])

  (ui-utils/subscribe-local component-id [:container])
  (ui-utils/dispatch-local component-id [:container] container-id)

  (ui-utils/subscribe-local component-id [:blackboard])
  (re-frame/subscribe [:coverage-plan-demo.component.blackboard])

  (ui-utils/dispatch-local component-id [:blackboard] {:dummy "dummy"})

  (ui-utils/create-widget-local-sub component-id [:blackboard :topic/layers])
  (ui-utils/create-widget-local-event component-id [:blackboard :topic/layers])
  (ui-utils/dispatch-local component-id [:blackboard :topic/layers] {:dummy "ui-utils"})

  (re-frame/dispatch [:coverage-plan-demo.component.blackboard.topic.layers {:dummy "re-frame"}])


  (ui-utils/subscribe-local component-id [:blackboard :topic/current-time])

  (ui-utils/create-widget-local-sub component-id [:blackboard :topic/current-time])
  (ui-utils/create-widget-local-event component-id [:blackboard :topic/current-time])
  (ui-utils/dispatch-local component-id [:blackboard :topic/current-time] {})





  (re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.layers])

  (->> configuration
    :components
    (filter (fn [[_ meta-data]]
              (= type (:type meta-data))))
    (map (fn [[node meta-data]]
           ;(log/info "process-components (nodes)" node "//"
           ;  meta-data "//" (:type meta-data))
           (component->ui {:node          node
                           :type          (:type meta-data)
                           :configuration configuration
                           :registry      registry
                           :component-id  component-id
                           :container-id  container-id}))))



  (re-frame/subscribe [:coverage-plan-demo/component])
  (re-frame/subscribe [:coverage-plan-demo/blackboard])
  (re-frame/subscribe [:coverage-plan-demo/blackboard.layers])
  (re-frame/dispatch [:coverage-plan-demo/blackboard.layers {:dummy "one"}])


  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  ;
  ; the problem is KEYWORDS, specifically the (keyword ...) function and the
  ; (name ...) function as applied to keywords:
  ;

  (keyword "dummy")
  ; :dummy

  (keyword "container" "component")
  ; :namespace/dummy

  (keyword "container/component" "dummy")
  ; :namespace/component/dummy   <-- BAD!
  ;      (actually invalid. The runtime is fine, but the reader can't handle it!)

  ;:namespace/component/dummy

  (keyword "namespace/component" :dummy)
  ; :namespace/component/dummy   <-- BAD!


  (keyword :namespace/component :topic/dummy)

  (name :dummy)
  ; "dummy"

  (name :namespace/dummy)
  ; "dummy"  <-- also BAD!

  ; so
  (keyword (name :namespace/component) (name :topic/dummy))
  ; :component/dummy   <-- really, really BAD



  ; built the (path->keyword ...) function, but...
  ;
  (ui-utils/path->keyword :namespace/component :topic/dummy)
  ; :component/dummy   <-- STILL BAD

  (ui-utils/path->keyword "namespace/component" :topic/dummy)
  ; :namespace/component/dummy   <-- STILL BAD

  (keyword (clojure.string/join "." ["container" "component" "topic.dummy"]))

  (str :topic/dummy)

  ())


; subscription scratchpad
(comment
  (ui-utils/subscribe-local :coverage-plan-demo.component
    [:blackboard :topic/current-time])

  (ui-utils/dispatch-local :coverage-plan-demo.component
    [:blackboard :topic/current-time] (js/Date.))


  (re-frame/reg-sub
    :coverage-plan-demo.component.blackboard.topic.time-range
    :<- [:coverage-plan-demo.component.blackboard.topic.current-time]
    (fn [t _]
      [0 t]))


  (re-frame/reg-sub
    :coverage-plan-demo.component.blackboard.topic.layers
    :<- [:coverage-plan-demo.component.blackboard.topic.selected-targets]
    :<- [:coverage-plan-demo.component.blackboard.topic.selected-satellites]
    :<- [:bh.rccst.subs/source :topic/coverage-data]
    (fn [t s c _]
      [{:layer-1 {} :layer-2 {}}]))


  (ui-utils/dispatch-local :coverage-plan-demo.component
    [:blackboard :topic/current-time] 75)


  (re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.current-time])
  (re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.selected-targets])
  (re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.selected-satellites])
  (re-frame/subscribe [:bh.rccst.subs/source :source/targets])
  (re-frame/subscribe [:bh.rccst.subs/source :source/satellites])
  (re-frame/subscribe [:bh.rccst.subs/source :source/coverages])

  (re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.layers])
  (re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.time-range])

  ())


; building the UI components "for real"
(comment
  (do
    (def data @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
    (def container-id "coverage-plan-demo")
    (def component-id :coverage-plan-demo.component)
    (def links (:links data))
    (def layout (:layout data))
    (def components (:components data))
    (def graph (apply lg/digraph (compute-edges data)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)
    (def configuration (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
                         :graph graph
                         :denorm (denorm-components graph (:links data) (lg/nodes graph))
                         :nodes (lg/nodes graph)
                         :edges (lg/edges graph)))
    (def source-type :source/local)
    (def node-type :source/local))

  (def component-lookup (into {}
                          (process-components
                            configuration :ui/component
                            meta-data-registry component-id)))

  (def component-ui (process-ui component-lookup [] layout))


  (process-ui component-lookup [] [:ui/globe])

  (process-ui component-lookup [] [:v-box [:ui/time-slider]])

  (re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.time-range])
  (re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.current-time])

  (re-frame/dispatch [:coverage-plan-demo.component.blackboard.topic.current-time 22])

  ())


; have to actually CALL the fn/subcription we built!
(comment
  (do
    (def config @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition)
    (def container-id :coverage-plan-demo.component)
    (def links (:links config))
    (def layout (:layout config))
    (def components (:components config))
    (def graph (apply lg/digraph (compute-edges config)))
    (def nodes (lg/nodes graph))
    (def edges (lg/edges graph))
    (def registry meta-data-registry)

    (def configuration (assoc @bh.rccst.ui-component.molecule.composite.coverage-plan/ui-definition
                         :graph graph
                         :denorm (denorm-components graph (:links config) (lg/nodes graph))
                         :nodes (lg/nodes graph)
                         :edges (lg/edges graph)
                         :ui-lookup (into {}
                                      (process-components
                                        configuration :ui/component
                                        meta-data-registry :coverage-plan))))
    (def component-lookup (into {}
                            (process-components
                              configuration :ui/component
                              meta-data-registry :coverage-plan)))
    (def node :fn/range)

    (def actual-fn (->> configuration :components node :name))
    (def denorm (->> configuration :denorm node)))

  (make-params configuration node :inputs container-id)
  (make-params configuration node :outputs container-id)


  (def built (seq
               (reduce into [actual-fn]
                 (seq
                   (merge
                     (make-params configuration node :inputs container-id)
                     (make-params configuration node :outputs container-id))))))

  (built)

  (actual-fn
    (merge
      (make-params configuration node :inputs container-id)
      (make-params configuration node :outputs container-id)))

  (let [actual-fn (->> configuration :components node :name)
        denorm    (->> configuration :denorm node)

        _         (log/info "component->ui :source/fn" node "//" actual-fn "//" denorm)

        built-fn  (apply actual-fn
                    (seq
                      (merge
                        (make-params configuration node :inputs container-id)
                        (make-params configuration node :outputs container-id))))])


  ())

;; endregion


(comment
  (def components [[[:div "1"] [empty] [:div "2"]]
                   [[empty] [:div "3"] [:div "4"]]])
  (layout/layout components)

  ())

; make sure the event handler preserves the ordering of the components in the DSL
(comment
  (def id "dummy")
  (def c {:widgets {id (config id)}})

  ((partial apply conj) [] [[:a :b] [:c :d]])

  ; using DSL (in progress, see component-layout
  (def components [[[:div "1"]]
                   [[:div "2"]]])
  (def components [[[:div "1"] [empty] [:div "2"]]
                   [[:div "3"] [:div "4"]]])

  (update-in c [:widgets id :components] (partial apply conj) components)

  ())
