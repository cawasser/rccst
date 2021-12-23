(ns bh.rccst.broadcast
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [clojure.core.async :as async :refer [go-loop <!]]))


(defn ->milliseconds [t]
  (* t 1000))


(defn broadcast! [socket msg]
  (let [uids (:any @(:connected-uids socket))]
    (doseq [uid uids]
      (log/info "broadcast to user: " uid)
      ((:chsk-send! socket) uid
       [:some/broadcast (assoc msg :to-whom uid)]))))


(defn- last-3-conj [last-3 i]
  (swap! last-3 (fn [v]
                  (as-> v x
                    (conj x i)
                    (if (> (count x) 3)
                      (drop 1 x)
                      x)
                    (apply sorted-set x)))))


(defrecord Broadcast [broadcast-timeout broadcast socket]
  component/Lifecycle

  (start [component]
    (log/info ";; Broadcast" broadcast-timeout)
    (tap> "starting broadcast")
    (let [last-3 (atom #{0})
          broadcast-loop (go-loop [i 0]
                           (<! (async/timeout (->milliseconds broadcast-timeout)))
                           (broadcast! socket {:what-is-this "An async broadcast pushed from server"
                                               :how-often    (str "Every " broadcast-timeout " seconds")
                                               :last-3       (last-3-conj last-3 i)
                                               :i            i})
                           (recur (inc i)))]
      (assoc component :broadcast broadcast-loop)))

  (stop [component]
    (log/info ";; Stopping broadcast")
    ; need some way to turn the go-loop off...
    (assoc component :broadcast nil)))


(defn new-broadcast
  [timeout]
  (map->Broadcast {:broadcast-timeout timeout}))


