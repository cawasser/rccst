(ns rccst.data-source.measurements
  (:require [rccst.components.system :as system]
            [clojure.tools.logging :as log]
            [rccst.version :as version]))


(def source-id :source/measurements)


(def sample-data (atom [{:name "Page A" :uv 4000 :pv 2400 :tv 1500 :amt 2400}
                        {:name "Page B" :uv 3000 :pv 1398 :tv 1500 :amt 2210}
                        {:name "Page C" :uv 2000 :pv 9800 :tv 1500 :amt 2290}
                        {:name "Page D" :uv 2780 :pv 3908 :tv 1500 :amt 2000}
                        {:name "Page E" :uv 1890 :pv 4800 :tv 1500 :amt 2181}
                        {:name "Page F" :uv 2390 :pv 3800 :tv 1500 :amt 2500}
                        {:name "Page G" :uv 3490 :pv 4300 :tv 1500 :amt 2100}]))


(defn- fetch-data []
  (log/info "measurements fetch-data")
  @sample-data)


(defn- wrap-meta [data]
  {:title "Measurements"
   :c-o-c [{:step :generated
            :by "rccst.data-source.measurements"
            :version version/version
            :at (str (java.util.Date.))
            :signature (str (java.util.UUID/randomUUID))}]
   :metadata {:type   :tabular
              :id     :name
              :title  "Tabular Measurement Data with Metadata"
              :fields {:name :string :uv :number :pv :number :tv :number :amt :number}}
   :data data})


(defn start-listener [pub-sub]
  ; 1. start the real listener, if necessary

  ; 2. send over the most recent data as a boot-strap
  (pub-sub [:publish/data-update {:id source-id
                                  :value (wrap-meta (fetch-data))}]))


(def meta-data {source-id start-listener})