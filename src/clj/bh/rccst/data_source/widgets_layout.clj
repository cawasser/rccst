(ns bh.rccst.data-source.widgets-layout
  (:require [bh.rccst.components.system :as system]
            [clojure.tools.logging :as log]
            [bh.rccst.version :as version]))


(def source-id :source/widgets-layout)


(def sample-layout {:widgets {:chart-remote-data-demo.widget              {:blackboard {:defs {:source {:components  {:topic/measurements {:type :source/remote,
                                                                                                                                           :name :source/measurements},
                                                                                                                      :ui/bar-chart       {:type :ui/component,
                                                                                                                                           :name :rechart/bar-2}},
                                                                                                        :links       {:topic/measurements {:data {:ui/bar-chart :data}}},
                                                                                                        :grid-layout [{:i      :ui/bar-chart,
                                                                                                                       :x      0,
                                                                                                                       :y      0,
                                                                                                                       :w      14,
                                                                                                                       :h      11,
                                                                                                                       :static true}],
                                                                                                        ; #loom.graph.BasicEditableDigraph
                                                                                                        :graph       {:nodeset #{:topic/measurements
                                                                                                                                 :ui/bar-chart},
                                                                                                                      :adj     {:topic/measurements #{:ui/bar-chart}},
                                                                                                                      :in      {:ui/bar-chart #{:topic/measurements}}},
                                                                                                        :denorm      {:topic/measurements {:inputs  {},
                                                                                                                                           :outputs {:ui/bar-chart [:data
                                                                                                                                                                    :data]},
                                                                                                                                           :params  {}},
                                                                                                                      :ui/bar-chart       {:inputs  {:topic/measurements [:data
                                                                                                                                                                          :data]},
                                                                                                                                           :outputs nil,
                                                                                                                                           :params  {}}},
                                                                                                        :nodes       #{:topic/measurements :ui/bar-chart},
                                                                                                        :edges       [[:topic/measurements :ui/bar-chart]]},
                                                                                               :dag    {:open-details ""}}},
                                                                           :container  nil,
                                                                           :layout     [{:i :ui/bar-chart, :x 0, :y 0, :w 14, :h 11, :static true}]},
                              :chart-remote-data-demo.widget.ui.bar-chart {:tv                {:include true, :fill "#82ca9d", :stackId ""},
                                                                           :brush             false,
                                                                           :y-axis            {:include true, :dataKey "", :orientation :left, :scale "auto"},
                                                                           :sub               [],
                                                                           :grid              {:include         true,
                                                                                               :strokeDasharray {:dash "3", :space "3"},
                                                                                               :stroke          "#a9a9a9"},
                                                                           :legend            {:include       true,
                                                                                               :layout        "horizontal",
                                                                                               :align         "center",
                                                                                               :verticalAlign "bottom"},
                                                                           :amt               {:include true, :fill "#ff00ff", :stackId ""},
                                                                           :tab-panel         {:value     :chart-remote-data-demo.widget.ui.bar-chart/config,
                                                                                               :data-path [:widgets
                                                                                                           :chart-remote-data-demo.widget.ui.bar-chart
                                                                                                           :tab-panel]},
                                                                           :pv                {:include true, :fill "#ffc107", :stackId ""},
                                                                           :container         nil,
                                                                           :x-axis            {:include     true,
                                                                                               :dataKey     :name,
                                                                                               :orientation :bottom,
                                                                                               :scale       "auto"},
                                                                           :pub               [],
                                                                           :uv                {:include true, :fill "#8884d8", :stackId ""},
                                                                           :tooltip           {:include true},
                                                                           :isAnimationActive true}}

                    :layout [{:i :chart-remote-data-demo.widget :x 0 :y 0 :w 10 :h 10 :static false}]})


(defn get-layout []
  sample-layout)


(defn- format-layout []
  (->> (get-layout)))


(defn- fetch-data []
  (log/info "widgets-layout fetch-data")
  (format-layout))


(defn- wrap-meta [data]
  {:title "Widgets"
   :c-o-c [{:step :generated
            :by "bh.rccst.data-source.widgets-layout"
            :version version/version
            :at (str (java.util.Date.))
            :signature (str (java.util.UUID/randomUUID))}]
   :metadata {:title "Widgets"
              :type :layout}
   :data data})


(defn start-listener [pub-sub]
  ; 1. start the real listener, if necessary

  (log/info "start-listener" pub-sub)

  ; 2. send over the most recent data as a boot-strap
  (pub-sub [:publish/data-update {:id source-id
                                  :value (wrap-meta
                                           (fetch-data))}]))


(def meta-data {source-id start-listener})

