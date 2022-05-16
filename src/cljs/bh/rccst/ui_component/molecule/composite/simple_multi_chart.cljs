(ns bh.rccst.ui-component.molecule.composite.simple-multi-chart
  (:require [bh.rccst.ui-component.atom.chart.bar-chart :as chart]
            [bh.rccst.ui-component.utils.color :as color]
            [bh.rccst.ui-component.utils.helpers :as h]
            [bh.rccst.ui-component.utils.locals :as l]
            [re-frame.core :as re-frame]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.molecule.composite.simple-multi-chart")


(def sample-data chart/sample-data)


(def ui-definition
  {:components  {:ui/bar-chart   {:type :ui/component :name :rechart/bar-2}
                 :ui/line-chart  {:type :ui/component :name :rechart/line-2}
                 :topic/data     {:type :source/local :name :topic/data :default sample-data}}
   :links       {:topic/data     {:data {:ui/bar-chart   :data
                                         :ui/line-chart  :data}}}
   :grid-layout [{:i :ui/line-chart :x 0 :y 0 :w 10 :h 11 :static true}
                 {:i :ui/bar-chart :x 10 :y 0 :w 10 :h 11 :static true}]})


(def source-code '(let [def {:components  {:ui/bar-chart   {:type :ui/component :name :rechart/bar-2}
                                           :ui/line-chart  {:type :ui/component :name :rechart/line-2}
                                           :topic/data     {:type :source/local :name :topic/data :default sample-data}}
                             :links       {:topic/data     {:data {:ui/bar-chart   :data
                                                                   :ui/line-chart  :data}}
                                           :topic/config   {:data {:ui/line-chart :config-data
                                                                   :ui/bar-chart  :config-data}}}
                             :grid-layout [{:i :ui/line-chart :x 0 :y 0 :w 7 :h 11 :static true}
                                           {:i :ui/bar-chart :x 7 :y 0 :w 7 :h 11 :static true}]}]
                    [grid-widget/component
                     :data def
                     :component-id (h/path->keyword container-id "widget")]))

