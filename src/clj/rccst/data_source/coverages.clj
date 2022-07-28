(ns rccst.data-source.coverages
  (:require [rccst.components.system :as system]
            [clojure.tools.logging :as log]
            [rccst.version :as version]
            [clojure.java.io :as io]
            [clojure.edn :as edn]))


(def source-id :source/coverages)


(def default-data-filename "public/default-data/default-coverages.edn")


(comment

  (->> default-data-filename
    io/resource
    slurp
    edn/read-string)

  ())


(def sample-data (->> default-data-filename
                   io/resource
                   slurp
                   edn/read-string))


(defn- add-random [data])


(defn- fetch-data []
  (log/info "coverages fetch-data")
  sample-data)


(defn- wrap-meta [data]
  {:title "Coverages"
   :c-o-c [{:step :generated
            :by "rccst.data-source.coverages"
            :version version/version
            :at (str (java.util.Date.))
            :signature (str (java.util.UUID/randomUUID))}]
   :metadata {:type :tabular
              :id :id
              :fields {:id :string :data :string}}
   :data data})


(defn start-listener [pub-sub]
  ; 1. start the real listener, if necessary

  ; 2. send over the most recent data as a boot-strap
  (pub-sub [:publish/data-update {:id source-id
                                  :value (wrap-meta (fetch-data))}]))


(def meta-data {source-id start-listener})
