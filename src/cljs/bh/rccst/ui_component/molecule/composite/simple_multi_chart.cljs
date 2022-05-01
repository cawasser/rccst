(ns bh.rccst.ui-component.molecule.composite.simple-multi-chart
  (:require [bh.rccst.ui-component.atom.chart.bar-chart-2 :as chart]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.ui-component.utils.locals :as l]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.molecule.composite.simple-multi-chart")


(def sample-data chart/sample-data)


(def default-config-data {:brush true
                          :uv    {:include true, :fill "#ff0000", :stackId "b"}
                          :pv    {:include true, :fill "#228B22", :stackId "b"}
                          :tv    {:include true, :fill "#ADD8E6", :stackId "a"}
                          :amt   {:include true, :fill "#800000", :stackId "a"}})


(defn- compute-data-config [data]
  (merge {:brush true}
    (->> (get-in data [:metadata :fields])
      (filter (fn [[k v]] (= :number v)))
      keys
      (map-indexed (fn [idx a]
                     {a {:include true
                         :stroke  (color/get-color idx)
                         :fill    (color/get-color idx)
                         :stackId ""}}))
      (into {}))))


(defn fn-make-config [{:keys [data config-data container-id] :as params}]
  (log/info "fn-make-config" config-data)
  (re-frame/reg-sub
    (first config-data)
    :<- data
    (fn [d _]
      (doall
        ; TODO: need a way to have :topic.config passed in somehow...
        (l/update-local-path-values container-id [:blackboard :topic.config] (compute-data-config d))))))


(def ui-definition
  {:components  {:ui/bar-chart   {:type :ui/component :name :rechart/bar-2}
                 :ui/line-chart  {:type :ui/component :name :rechart/line-2}
                 :topic/data     {:type :source/local :name :topic/data :default sample-data}
                 :topic/config   {:type :source/local :name :topic/config :default {}}
                 :fn/make-config {:type  :source/fn :name fn-make-config
                                  :ports {:data :port/sink :config-data :port/source-sink}}}
   :links       {:topic/data     {:data {:ui/bar-chart   :data
                                         :ui/line-chart  :data
                                         :fn/make-config :data}}
                 :topic/config   {:data {:ui/line-chart :config-data
                                         :ui/bar-chart  :config-data}}
                 :fn/make-config {:config-data {:topic/config :data}}}

   :grid-layout [{:i :ui/line-chart :x 0 :y 0 :w 7 :h 11 :static true}
                 {:i :ui/bar-chart :x 7 :y 0 :w 7 :h 11 :static true}]})


(def source-code '(let [def {:components  {:ui/bar-chart   {:type :ui/component :name :rechart/bar-2}
                                           :ui/line-chart  {:type :ui/component :name :rechart/line-2}
                                           :topic/data     {:type :source/local :name :topic/data :default sample-data}
                                           :topic/config   {:type :source/local :name :topic/config :default {}}
                                           :fn/make-config {:type  :source/fn :name fn-make-config
                                                            :ports {:data :port/sink :config-data :port/source-sink}}}
                             :links       {:topic/data     {:data {:ui/bar-chart   :data
                                                                   :ui/line-chart  :data
                                                                   :fn/make-config :data}}
                                           :topic/config   {:data {:ui/line-chart :config-data
                                                                   :ui/bar-chart  :config-data}}
                                           :fn/make-config {:config-data {:topic/config :data}}}

                             :grid-layout [{:i :ui/line-chart :x 0 :y 0 :w 7 :h 11 :static true}
                                           {:i :ui/bar-chart :x 7 :y 0 :w 7 :h 11 :static true}]}]
                    [grid-widget/component
                     :data def
                     :component-id (h/path->keyword container-id "widget")]))

