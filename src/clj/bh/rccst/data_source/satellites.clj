(ns bh.rccst.data-source.satellites
  (:require [bh.rccst.components.system :as system]
            [clojure.tools.logging :as log]
            [bh.rccst.version :as version]))


(def source-id :source/satellites)


(def sample-data (atom [{:id "ten":data "ONE ONE ONE"}
                        {:id "twenty":data "TWO TWO TWO"}
                        {:id "thirty":data "THREE THREE THREE"}
                        {:id "forty":data "FOUR FOUR FOUR"}]))

(def platforms [{:name "goes-east" :start [9 6] :path "geo" :sensors ["abi-1" "abi-meso-2" "abi-meso-10"]}
                {:name "goes-west" :start [9 2] :path "geo" :sensors ["abi-3" "abi-meso-4" "abi-meso-11"]}
                {:name "noaa-xx" :start [4 3] :path "horz" :sensors ["viirs-5"]}
                {:name "metop-yy" :start [4 1] :path "horz" :sensors ["avhhr-6"]}])

(def sensors
  [{:id "abi-1" :platform_id "goes-east", :type "image", :sensor_steering [[0 0] [0 0]], :sensor_size [10 10]}
   {:id "abi-3" :platform_id "goes-west", :type "image", :sensor_steering [[0 0] [0 0]], :sensor_size [10 10]}
   {:id "abi-meso-2" :platform_id "goes-east", :type "hidef-image", :sensor_steering [[-5 5] [-5 5]], :sensor_size [1 1]}
   {:id "abi-meso-4" :platform_id "goes-west", :type "hidef-image", :sensor_steering [[-5 5] [-5 5]], :sensor_size [1 1]}
   {:id "abi-meso-10" :platform_id "goes-east", :type "hidef-image", :sensor_steering [[-5 5] [-5 5]], :sensor_size [1 1]}
   {:id "abi-meso-11" :platform_id "goes-west", :type "hidef-image", :sensor_steering [[-5 5] [-5 5]], :sensor_size [1 1]}
   {:id "viirs-5" :platform_id "noaa-xx", :type "v/ir", :sensor_steering [[0 0] [0 0]], :sensor_size [10 2]}
   {:id "avhhr-6" :platform_id "metop-yy", :type "v/ir", :sensor_steering [[0 0] [0 0]], :sensor_size [10 2]}])


(defn- make-indexed [id m]
  {(id m) m})


(defn- merge-platform-sensors []
  (let [platforms (->> platforms
                    (map #(clojure.set/rename-keys % {:id :platform_id})))
        sensors   (->> sensors
                    (map #(clojure.set/rename-keys % {:id :sensor_id}))
                    (map #(make-indexed :sensor_id %))
                    (into {}))]
    (into []
      (flatten
        (for [p platforms]
          (for [s (:sensors p)]
            (merge (dissoc p :sensors) (get sensors s))))))))


(defn- fetch-data []
  (log/info "satellites fetch-data")
  (merge-platform-sensors))


(defn- wrap-meta [data]
  {:title "Satellites"
   :c-o-c [{:step :generated
            :by "bh.rccst.data-source.satellites"
            :version version/version
            :at (str (java.util.Date.))
            :signature (str (java.util.UUID/randomUUID))}]
   :metadata {:title "Satellites"
              :type :tabular
              :id :name
              :fields {:name :string :start :vector
                       :path :string :platform_id :string
                       :type :string :sensor_steering :vector
                       :sensor_size :vector :sensor_id :string}}
   :data data})


(defn start-listener [pub-sub]
  ; 1. start the real listener, if necessary

  ; 2. send over the most recent data as a boot-strap
  (pub-sub [:publish/data-update {:id source-id
                                  :value (wrap-meta
                                           (fetch-data))}]))


(def meta-data {source-id start-listener})