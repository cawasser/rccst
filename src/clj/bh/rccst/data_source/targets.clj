(ns bh.rccst.data-source.targets
  (:require [bh.rccst.components.system :as system]
            [clojure.tools.logging :as log]))


(def source-id :source/targets)


(def sample-data (atom [{:id "one":data "ONE ONE ONE"}
                        {:id "two":data "TWO TWO TWO"}
                        {:id "three":data "THREE THREE THREE"}
                        {:id "four":data "FOUR FOUR FOUR"}]))


(defn- add-random [data])


(defn- fetch-data []
  (log/info "targets fetch-data")
  @sample-data)


(defn- wrap-meta [data]
  {:title "Targets"
   :c-o-c []
   :metadata {:type :tabular
              :id :id
              :fields {:id :string :data :string}}
   :data data})


(defn start-listener [pub-sub]
  ; 1. start the real listener, if necessary

  (log/info "start-listener" pub-sub)

  ; 2. send over the most recent data as a boot-strap
  (pub-sub [:publish/data-update {:id source-id
                                  :value (wrap-meta
                                           (fetch-data))}]))


(def meta-data {source-id start-listener})



