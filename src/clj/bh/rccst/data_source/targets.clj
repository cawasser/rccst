(ns bh.rccst.data-source.targets
  (:require [bh.rccst.components.system :as system]
            [clojure.tools.logging :as log]
            [bh.rccst.version :as version]))


(def source-id :source/targets)


(def sample-targets {"alpha-hd"  #{[7 7 "hidef-image" 0]
                                   [7 6 "hidef-image" 1]
                                   [7 6 "hidef-image" 2]
                                   [7 5 "hidef-image" 3]}
                     "bravo-img" #{[7 2 "image" 0]
                                   [7 1 "image" 1]}
                     "fire-hd"   #{[5 3 "hidef-image" 0]
                                   [4 3 "hidef-image" 2] [5 3 "hidef-image" 2]
                                   [4 3 "hidef-image" 3] [5 3 "hidef-image" 3]}
                     "fire-ir"   #{[5 4 "v/ir" 0]
                                   [5 3 "v/ir" 1] [5 4 "v/ir" 1]
                                   [5 4 "v/ir" 2]
                                   [5 4 "v/ir" 3]}
                     "severe-hd" #{[5 6 "hidef-image" 0]
                                   [5 7 "hidef-image" 1] [6 5 "hidef-image" 1]
                                   [6 6 "hidef-image" 2]
                                   [5 7 "hidef-image" 3]}})


(defn get-targets []
  sample-targets)


(defn- format-aois []
  (->> (get-targets)
    (map (fn [[target cells]]
           {:name target :cells cells}))
    (into [])))


(defn- fetch-data []
  (log/info "targets fetch-data")
  (format-aois))


(defn- wrap-meta [data]
  {:title "Targets"
   :c-o-c [{:step :generated
            :by "bh.rccst.data-source.targets"
            :version version/version
            :at (str (java.util.Date.))
            :signature (str (java.util.UUID/randomUUID))}]
   :metadata {:title "Targets"
              :type :tabular
              :id :target
              :fields {:target :string :cells :string}}
   :data data})


(defn start-listener [pub-sub]
  ; 1. start the real listener, if necessary

  (log/info "start-listener" pub-sub)

  ; 2. send over the most recent data as a boot-strap
  (pub-sub [:publish/data-update {:id source-id
                                  :value (wrap-meta
                                           (fetch-data))}]))


(def meta-data {source-id start-listener})



