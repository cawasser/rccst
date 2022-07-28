(ns rccst.components.data-sources
  (:require [rccst.data-source.coverages :as coverages]
            [rccst.data-source.satellites :as satellites]
            [rccst.data-source.targets :as targets]
            [rccst.data-source.measurements :as measurements]

            [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]))


(log/info "rccst.components.data-sources")


(defn init-registry []
  (log/info "init-registry")
  (merge {}
    targets/meta-data
    satellites/meta-data
    coverages/meta-data
    measurements/meta-data))


(defn- default-source [source-id pub-sub]
  (pub-sub [:publish/data-update {:id    source-id
                                  :value {:title "Default Data - ERROR on the server!!"
                                          :data  []}}]))


(defn- lookup [registry source-id]
  (log/info "lookup" source-id)
  (or (source-id @registry) (partial default-source source-id)))


(defrecord DataSources []
  component/Lifecycle

  (start [component]
    (log/info ";; DataSources")
    (let [registry (atom {})]
      (reset! registry (init-registry))
      ;(log/info "starting data-sources" @registry)
      (assoc component
        :registry registry
        :lookup (partial lookup registry))))

  (stop [component]
    (log/info ";; Stopping DataSources")
    (assoc component
      :registry nil
      :lookup nil)))


(defn new-data-sources
  []
  (map->DataSources {}))



(comment
  (atom (init-registry))

  (def r (atom {}))
  (reset! r (init-registry))

  ())


