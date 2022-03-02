(ns bh.rccst.ui-component.molecule.composite.coverage-plan
  "provide a composed UI for a \"Coverage Plan\" which shows targets and satellite coverage areas
  on a 3D globe"
  (:require [loom.graph :as lg]))



(defn subscribe [a b c])
(defn publish [a b c])

(def selectable-table)
(def globe)
(def slider)
(def label)


; assume the ui components have the following meta-data:
;
;      you PUBLISH using a :port/sink
;
;      you SUBSCRIBE via a :port/source
;
;      you do BOTH with :port/source-sink
;
(def selectable-table-meta-data {:component selectable-table
                                 :ports     {:data      :port/source-sink
                                             :selection :port/source}})

(def globe-meta-data {:component globe
                      :ports     {:coverages    :port/source
                                  :current-time :port/source}})

(def slider-meta-data {:component slider
                       :ports     {:value :port/source-sink
                                   :range :port/sink}})

(def label-meta-data {:component label
                      :ports     {:value :port/sink}})



; we can define the "Coverage Plan" as:
;
;    note: make-coverage and make-range are function (in this namespace)
;

(defn make-coverage [& _])
(defn make-range [& _])

(def h-box [])
(def v-box [])

(def composite-def
  {:title      "Coverage Plan"
   :component-id :coverage-plan
   :components {; ui components
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
                                            :name  make-coverage
                                            :ports {:targets    :port/sink
                                                    :satellites :port/sink
                                                    :coverages  :port/sink
                                                    :selected   :port/source}}
                :fn/range                  {:type  :source/fn
                                            :name  make-range
                                            :ports {:data  :port/sink
                                                    :range :port/source}}}

   :links       {; ui components
                 :ui/targets      {:data      :topic/target-data
                                   :selection :topic/selected-targets}
                 :ui/satellites   {:data      :topic/satellite-data
                                   :selection :topic/selected-satellites}
                 :ui/globe        {:coverages :topic/coverages
                                   :time      :topic/current-time}
                 :ui/time-slider  {:time  :topic/current-time
                                   :range :topic/time-range}
                 :ui/current-time {:value :topic/current-time}

                 ; transformation functions
                 :fn/coverage     {:targets    :topic/selected-targets
                                   :satellites :topic/selected-satellites
                                   :coverages  :topic/coverage-data
                                   :selected   :topic/selected-coverages}
                 :fn/range        {:data  :topic/coverages
                                   :range :topic/time-range}}

   :layout     [v-box
                [h-box
                 [v-box [:ui/targets] [:ui/satellites] [:ui/time-slider]]
                 [v-box [:ui/globe] [:ui/current-time]]]]})


(comment
  {:fn/coverage (make-coverage
                  :targets (subscribe :source/local :topic/selected-targets)
                  :satellites (subscribe :source/local :topic/selected-satellites)
                  :coverages (subscribe :source/remote :topic/coverages)
                  :selected (publish :source/local :topic/selected-coverages))


   :fn/range    (make-range
                  :data (subscribe :source/remote :topic/coverages)
                  :selected (publish :source/local :topic/time-range))

   :ui/targets [selectable-table
                :component-id :coverage-plan/targets
                :container-id :coverage-plan
                :data (pub-sub :source/remote :topic/target-data)
                :selected (publish :source/local :topic/selected-targets)]

   :ui/satellites [selectable-table
                   :component-id :coverage-plan/satellites
                   :container-id :coverage-plan
                   :data (pub-sub :source/remote :topic/satellite-data)
                   :selected (publish :source/local :topic/selected-targets)]}

  :ui/globe [globe
             :component-id :coverage-plan/globe
             :container-id :coverage-plan
             :coverages (subscribe :source/local :topic/selected-coverages)
             :current-time (subscribe :source/local :topic/current-time)]

  :ui/time-slider [slider
                   :component-id :coverage-plan/slider
                   :container-id :coverage-plan
                   :value (pub-sub :source/local :topic/current-time)
                   :range (subscribe :source/local :topic/time-range)]

  :ui/current-time [label
                    :component-id :coverage-plan/label
                    :container-id :coverage-plan
                    :value (subscribe :source/local :topic/current-time)]

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
(comment
  (def nodes (->> composite-def :components keys (into [])))
  (def edges (->> composite-def
               :links
               (mapcat (fn [[k v]]
                         (map (fn [[name type]]
                                [k type])
                           v)))
               (into [])))

  (def g (apply lg/graph nodes edges))


  ())
