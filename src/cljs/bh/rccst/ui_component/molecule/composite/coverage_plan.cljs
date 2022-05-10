(ns bh.rccst.ui-component.molecule.composite.coverage-plan
  "provide a composed UI for a \"Coverage Plan\" which shows targets and satellite coverage areas
  on a 3D globe"
  (:require [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [cljs-time.core :as t]
            [cljs-time.coerce :as coerce]
            [bh.rccst.ui-component.molecule.composite.coverage-plan.support :as s]
            [bh.rccst.ui-component.utils :as ui-utils]
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
  [{:keys [targets satellites coverages current-time shapes]}]

  ;(log/info "fn-coverage" shapes
  ;  "//" targets
  ;  "//" satellites
  ;  "//" coverages)

  (re-frame/reg-sub
    (first shapes)
    :<- targets
    :<- satellites
    :<- coverages
    :<- current-time
    (fn [[t s c ct] _]
      ;(log/info "fn-coverage (sub)" ct "//" (filter #(= (:time %) ct) (:data c)))

      (if (or (empty? c) (empty? (:data c)))
        []
        (s/make-shape (filter #(= (:time %) ct) (:data c)))))))


(defn fn-range
  "registers the subscription for the entity defined by 'selected'. processing from
  inputs to output is performed by 'some-computation'

  - data : (vector of keywords) the subscription signal for the input data
  - container-id : (string) name of the container holding the blackboard

  builds and registers the subscription :<container>/blackboard.<selected>
  "
  [{:keys [data range]}]

  ;(log/info "fn-range" range "//" data)

  (re-frame/reg-sub
    (first range)
    :<- data
    (fn [d _]
      [0 (dec (count bh.rccst.ui-component.atom.worldwind.globe/sample-data))])))


(defn fn-current-time [{:keys [value current-time]}]
  ;(log/info "fn-current-time" value "//" current-time)

  (re-frame/reg-sub
    (first current-time)
    :<- value
    (fn [v _]
      (coerce/to-date (t/plus (t/now) (t/hours v))))))



;; components have "ports" which define their inputs and outputs:
;;
;;      you SUBSCRIBE with a :port/sink, ie, data come IN   (re-frame/subscribe ...)
;;
;;      you PUBLISH to a :port/source, ie, data goes OUT    (re-frame/dispatch ...)
;;
;;      you do BOTH with :port/source-sink (both)           should we even have this, or should we spell out both directions?
;;
;; the question about :port/source-sink arises because building the layout (the call for the UI itself) doesn't actually
;; need to make a distinction (in fact the code is a bit cleaner if we don't) and we have the callee sort it out (since it
;; needs to implement the correct usage anyway). The flow-diagram, on the other hand, is easier if we DO make the
;; distinction, so we can quickly build all the Nodes and Handles used for the diagram...


(def ui-definition {:title        "Coverage Plan"
                    :component-id :coverage-plan
                    :components   {; ui components
                                   :ui/targets                {:type :ui/component :name :bh/table}
                                   :ui/satellites             {:type :ui/component :name :bh/table}
                                   :ui/globe                  {:type :ui/component :name :ww/globe}
                                   :ui/time-slider            {:type :ui/component :name :rc/slider}
                                   :ui/current-time           {:type :ui/component :name :rc/label-md}

                                   ; remote data sources
                                   :topic/target-data         {:type :source/remote :name :source/targets}
                                   :topic/satellite-data      {:type :source/remote :name :source/satellites}
                                   :topic/coverage-data       {:type :source/remote :name :source/coverages}

                                   ; composite-local data sources
                                   :topic/selected-targets    {:type :source/local :name :selected-targets :default []}
                                   :topic/selected-satellites {:type :source/local :name :selected-satellites :default []}
                                   :topic/current-time        {:type :source/local :name :current-time :default 0} ;(js/Date.)}
                                   :topic/shapes              {:type :source/local :name :shapes}
                                   :topic/time-range          {:type :source/local :name :time-range}
                                   :topic/current-slider      {:type :source/local :name :current-slider :default 0}

                                   ; transformation functions
                                   :fn/coverage               {:type  :source/fn :name fn-coverage
                                                               :ports {:targets   :port/sink :satellites :port/sink
                                                                       :coverages :port/sink :current-time :port/sink
                                                                       :shapes    :port/source}}
                                   :fn/range                  {:type  :source/fn :name fn-range
                                                               :ports {:data :port/sink :range :port/source}}
                                   :fn/current-time           {:type  :source/fn :name fn-current-time
                                                               :ports {:value :port/sink :current-time :port/source}}}

                    :links        {; components publish to what? via which port?
                                   ;
                                   ; <source>                 {<source-port>  {<target> <target-port>
                                   ;                                           <target> <target-port>}}
                                   ;
                                   :ui/targets                {:data      {:topic/target-data :data}
                                                               :selection {:topic/selected-targets :data}}
                                   :ui/satellites             {:data      {:topic/satellite-data :data}
                                                               :selection {:topic/selected-satellites :data}}
                                   :ui/time-slider            {:value {:topic/current-slider :data}}

                                   ; transformation functions publish to what?
                                   :fn/coverage               {:shapes {:topic/shapes :data}}
                                   :fn/range                  {:range {:topic/time-range :data}}
                                   :fn/current-time           {:current-time {:topic/current-time :data}}

                                   ; topics are inputs into what?
                                   :topic/target-data         {:data {:ui/targets :data}}
                                   :topic/satellite-data      {:data {:ui/satellites :data}}
                                   :topic/selected-targets    {:data {:fn/coverage :targets}}
                                   :topic/selected-satellites {:data {:fn/coverage :satellites}}
                                   :topic/coverage-data       {:data {:fn/coverage :coverages
                                                                      :fn/range    :data}}
                                   :topic/shapes              {:data {:ui/globe :shapes}}
                                   :topic/current-time        {:data {:ui/current-time :value
                                                                      :ui/globe        :current-time}}
                                   :topic/current-slider      {:data {:fn/current-time :value
                                                                      :ui/time-slider  :value
                                                                      :fn/coverage     :current-time}}
                                   :topic/time-range          {:data {:ui/time-slider :range}}}

                    :grid-layout  [{:i :ui/targets :x 0 :y 0 :w 9 :h 7 :static true}
                                   {:i :ui/satellites :x 0 :y 7 :w 9 :h 8 :static true}
                                   {:i :ui/time-slider :x 2 :y 15 :w 6 :h 2 :static true}
                                   {:i :ui/globe :x 9 :y 0 :w 11 :h 15 :static true}
                                   {:i :ui/current-time :x 9 :y 15 :w 8 :h 2 :static true}]})



(comment
  (re-frame/dispatch [:bh.rccst.events/login "test-user" "test-pwd"])

  (re-frame/subscribe [:bh.rccst.subs/source :string])

  ())


; work out making actual shapes for the coverage data we get from the server
(comment
  (do
    (def coverages (get-in @re-frame.db/app-db [:sources :source/coverages :data]))
    (def current-time 0)

    (def time-coverage (filter #(= (:time %) current-time) coverages)))


  (ui-utils/subscribe-local
    :ui-grid-ratom-demo.coverage-plan
    [:blackboard :topic.shapes])


  ())



