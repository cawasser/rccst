(ns bh.rccst.broadcast
  (:require [com.stuartsierra.component :as component]
            [clojure.core.async :as async :refer [go-loop <!]]))


(defn ->milliseconds [t]
  (* t 1000))


(defn broadcast! [socket msg]
  (let [uids (:any @(:connected-uids socket))]
    (doseq [uid uids]
      (println "broadcast to user: " uid)
      ((:chsk-send! socket) uid
       [:some/broadcast (assoc msg :to-whom uid)]))))


(defrecord Broadcast [broadcast-timeout broadcast socket]
  component/Lifecycle

  (start [component]
    (println ";; Broadcast" broadcast-timeout)
    (tap> "starting broadcast")
    (let [broadcast-loop (go-loop [i 0]
                           (<! (async/timeout (->milliseconds broadcast-timeout)))
                           (broadcast! socket {:what-is-this "An async broadcast pushed from server"
                                               :how-often    (str "Every " broadcast-timeout " seconds")
                                               :i            i})
                           (recur (inc i)))]
      (assoc component :broadcast broadcast-loop)))

  (stop [component]
    (println ";; Stopping broadcast")
    ; need some way to turn the go-loop off...
    (assoc component :broadcast nil)))


(defn new-broadcast
  [timeout]
  (map->Broadcast {:broadcast-timeout timeout}))


