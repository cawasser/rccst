(ns bh.rccst.ui-component.molecule.composite.coverage-plan
  "provide a composed UI for a \"Coverage Plan\" which shows targets and satellite coverage areas
  on a 3D globe"
  (:require [bh.rccst.ui-component.utils :as ui-utils]
            [loom.graph :as lg]
            [re-com.core :as rc]
            [reagent.core :as r]
            [reagent.core :as r]
            ["dagre" :as dagre]
            ["graphlib" :as graphlib]
            ["react-flow-renderer" :refer (ReactFlowProvider Controls Handle) :default ReactFlow]))


; declare some dummy functions so everything compiles
;; region

(defn subscribe-local [name]
  [:fn/subscribe-local name])
(defn subscribe-remote [name]
  [:fn/subscribe-remote name])

(defn publish-local [event value]
  [:fn/dispatch event value])
(defn publish-remote [event value]
  [:fn/dispatch event value])

(defn pub-sub-local [name]
  [[:fn/subscribe-local name]
   [:fn/publish-local name]])
(defn pub-sub-remote [name]
  [[:fn/subscribe-remote name]
   [:fn/publish-remote name]])

(defn h-box [& body]
  (into [] body))
(defn v-box [& body]
  (into [] body))


; we can define the "Coverage Plan" as:
;
;    note: fn-coverage and fn-range are functions (in this namespace)
;

(defn fn-coverage [& {:keys []}]
  [])
(defn fn-range [& {:keys []}]
  [])


;; endregion


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
                                                                             :selected   :port/source}}
                                         :fn/range                  {:type  :source/fn
                                                                     :name  fn-range
                                                                     :ports {:data  :port/sink
                                                                             :range :port/source}}}

                          :links        {; ui components publish to what?
                                         :ui/targets                {:topic/target-data      :data
                                                                     :topic/selected-targets :selection}
                                         :ui/satellites             {:topic/satellite-data      :data
                                                                     :topic/selected-satellites :selection}
                                         :ui/time-slider            {:topic/current-time :value}

                                         ; transformation functions publish to what?
                                         :fn/coverage               {:topic/selected-coverages :selected}
                                         :fn/range                  {:topic/time-range :range}

                                         ; topics are inputs into what?
                                         :topic/target-data         {:ui/targets :data}
                                         :topic/satellite-data      {:ui/satellites :data}
                                         :topic/selected-targets    {:fn/coverage :targets}
                                         :topic/selected-satellites {:fn/coverage :satellites}
                                         :topic/coverage-data       {:fn/coverage :coverages
                                                                     :fn/range    :data}
                                         :topic/selected-coverages  {:ui/globe :coverages}
                                         :topic/current-time        {:ui/current-time :value
                                                                     :ui/time-slider  :value
                                                                     :ui/globe        :current-time}
                                         :topic/time-range          {:ui/time-slider :range}}

                          :layout       [v-box
                                         [h-box
                                          [v-box [:ui/targets] [:ui/satellites] [:ui/time-slider]]
                                          [v-box [:ui/globe] [:ui/current-time]]]]}))


(def source-code '[coverage-plan])


(defn- config [_ _]
  {})


; assume the ui components have the following meta-data:
;
;      you PUBLISH using a :port/sink
;
;      you SUBSCRIBE via a :port/source
;
;      you do BOTH with :port/source-sink
;

; using keywords to make this simpler in a sandbox
;
(def meta-data {:ui/selectable-table {:component :component/selectable-table
                                      :ports     {:data      :port/source-sink
                                                  :selection :port/source}}

                :ui/globe            {:component :component/globe
                                      :ports     {:coverages    :port/source
                                                  :current-time :port/source}}

                :ui/slider           {:component :component/slider
                                      :ports     {:value :port/source-sink
                                                  :range :port/sink}}

                :ui/label            {:component :component/label
                                      :ports     {:value :port/sink}}})

; we want to turn the composite-def into things like...
;
(comment
  {:fn/coverage     (fn-coverage
                      :targets (subscribe-local :source/local :topic/selected-targets)
                      :satellites (subscribe-local :source/local :topic/selected-satellites)
                      :coverages (subscribe-remote :source/remote :topic/coverages)
                      :selected (publish-local :source/local :topic/selected-coverages))


   :fn/range        (fn-range
                      :data (subscribe-remote :source/remote :topic/coverages)
                      :selected (publish-local :source/local :topic/time-range))

   :ui/targets      [:component/selectable-table
                     :component-id :coverage-plan/targets
                     :container-id :coverage-plan
                     :data (pub-sub-remote :source/remote :topic/target-data)
                     :selected (publish-local :source/local :topic/selected-targets)]

   :ui/satellites   [:component/selectable-table
                     :component-id :coverage-plan/satellites
                     :container-id :coverage-plan
                     :data (pub-sub-remote :source/remote :topic/satellite-data)
                     :selected (publish-local :source/local :topic/selected-targets)]

   :ui/globe        [:component/globe
                     :component-id :coverage-plan/globe
                     :container-id :coverage-plan
                     :coverages (subscribe-local :source/local :topic/selected-coverages)
                     :current-time (subscribe-local :source/local :topic/current-time)]

   :ui/time-slider  [:component/slider
                     :component-id :coverage-plan/slider
                     :container-id :coverage-plan
                     :value (pub-sub-local :source/local :topic/current-time)
                     :range (subscribe-local :source/local :topic/time-range)]

   :ui/current-time [:component/label
                     :component-id :coverage-plan/label
                     :container-id :coverage-plan
                     :value (subscribe-local :source/local :topic/current-time)]}

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

(defmethod component->ui :source/local [{:keys [name]}]
  (subscribe-local name))

(defmethod component->ui :source/remote [{:keys [name]}]
  (subscribe-local name))

(defmethod component->ui :source/fn [{:keys [name ports]}]
  [name ports])
;; endregion


(defn- node-type [node]
  (:el-type node))


(defn- dagre-graph [graph]
  (let [dagreGraph (new (.-Graph graphlib))
        nodeWidth  172
        nodeHeight 36]


    (.setDefaultEdgeLabel dagreGraph (clj->js {}))
    (.setGraph dagreGraph (clj->js {:rankdir "tb"}))

    (doall
      (map (fn [element]
             ;(println "layout" element)
             (condp = (:el-type element)
               :node (do
                       ;(println "adding node" element)
                       (.setNode dagreGraph (:id element) (clj->js {:width nodeWidth :height nodeHeight})))
               :edge (do
                       ;(println "adding edge" element)
                       (.setEdge dagreGraph (:source element) (:target element)))))
        graph))

    (println "before" dagreGraph (.nodeCount dagreGraph) (.edgeCount dagreGraph))

    (doall
      (map (fn [n]
             (println "node" (js->clj n)))
        (.nodes dagreGraph)))
    (doall
      (map (fn [n]
             (println "edge" (js->clj n)))
        (.edges dagreGraph)))

    dagreGraph))


(defn- layout [graph]
  (let [dagreGraph (dagre-graph graph)
        nodeWidth  172
        nodeHeight 36]

    (.layout dagre dagreGraph)

    (doall
      (map (fn [element]
             (println "element" (:id element) (.node dagreGraph (clj->js (:id element))))
             (condp = (:el-type element)
               :node (let [dagreNode (.node dagreGraph (clj->js (:id element)))]
                       (assoc element :position {:x (- (.-x dagreNode) (/ nodeWidth 2))
                                                 :y (- (.-y dagreNode) (/ nodeWidth 2))}))
               :edge (assoc element :targetPosition "top"
                                    :sourcePosition "bottom")))
        graph))))


(comment
  (def graph (apply lg/digraph (compute-edges @sample-data)))
  (def dagreGraph (dagre-graph graph))

  (make-flow graph)

  (clj->js {:width 10 :height 10})
  (clj->js {:width :thing :height 10})

  (.node ("my-id"))


  ())


(defn- create-flow-node [node-id]
  ;(println "node" node-id)
  {:id       (str node-id)
   :el-type  :node
   :data     {:label (str node-id)}
   :position {}})


(defn- create-flow-edge [idx [node-id target-id :as edge]]
  ;(println "edge" edge)
  {:id       (str idx)
   :el-type  :edge
   :source   (str node-id)
   :target   (str target-id)
   :style    {:stroke-width 5 :stroke :gray}
   :animated false})


(defn- compute-edges [config]
  (->> config
    :links
    (mapcat (fn [[entity links]]
              (map (fn [[target port]]
                     [entity target])
                links)))
    (into [])))


(defn- make-flow [graph]
  (let [flow (apply conj
               (map create-flow-node (lg/nodes graph))
               (map-indexed (fn [idx node]
                              (create-flow-edge idx node))
                 (lg/edges graph)))]
    (layout flow)))




(defn- dag-panel [& {:keys [graph component-id container-id ui]}]
  (let [config-flow (make-flow graph)]
    [:div {:style {:width "60%" :height "100%"}}
     [:> ReactFlowProvider
      [:> ReactFlow {:className        component-id
                     :elements         config-flow
                     :nodeTypes        {}
                     :edgeTypes        {}
                     :zoomOnScroll     false
                     :preventScrolling false
                     :onConnect        #()}
       [:> Controls]]]]))


(defn- component-panel [& {:keys [graph component-id container-id ui]}]
  [:div "The composed UI will display here"
   (map (fn [node]
          ^{:key node} [:p (str node)])
     (lg/nodes graph))])


(defn component [& {:keys [data component-id container-id ui]}]
  (let [id           (r/atom nil)
        config-graph (apply lg/digraph (compute-edges @data))]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id (config @id data))
        (ui-utils/dispatch-local @id [:container] container-id))

      ;(log/info "coverage-plan" @id (config @id data))

      [rc/h-box :src (rc/at)
       :gap "20px"
       :width "1000px"
       :height "800px"
       :children [[dag-panel
                   :graph config-graph
                   :component-id @id
                   :container-id container-id
                   :ui ui]
                  [component-panel
                   :graph config-graph
                   :component-id @id
                   :container-id container-id
                   :ui ui]]])))




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

