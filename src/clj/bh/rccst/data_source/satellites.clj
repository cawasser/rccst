(ns bh.rccst.data-source.satellites
  (:require [bh.rccst.components.system :as system]
            [clojure.tools.logging :as log]))


(def source-id :source/satellites)


(def sample-data (atom [{:id "ten":data "ONE ONE ONE"}
                        {:id "twenty":data "TWO TWO TWO"}
                        {:id "thirty":data "THREE THREE THREE"}
                        {:id "forty":data "FOUR FOUR FOUR"}]))


(defn- add-random [data])


(defn- fetch-data []
  (log/info "satellites fetch-data")
  @sample-data)


(defn- wrap-meta [data]
  {:title "Satellites"
   :c-o-c []
   :metadata {:type :tabular
              :id :id
              :fields {:id :string :data :string}}
   :data data})


(defn start-listener [pub-sub]
  ; 1. start the real listener, if necessary

  ; 2. send over the most recent data as a boot-strap
  (pub-sub [:publish/data-update {:id source-id
                                  :value (wrap-meta
                                           (fetch-data))}]))


(def meta-data {source-id start-listener})