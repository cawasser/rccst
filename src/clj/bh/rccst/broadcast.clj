(ns bh.rccst.broadcast
  (:require [clojure.core.async :as async :refer [go-loop <!]]))


(defn broadcast! [socket msg]
  (let [uids (:any @(:connected-uids socket))]
    (doseq [uid uids]
      (println "broadcast to user: " uid)
      ((:chsk-send! socket) uid
        [:some/broadcast (assoc msg :to-whom uid)]))))


(defn start-example-broadcaster!
  "As an example of server>user async pushes, setup a loop to broadcast an
  event to all connected users every 10 seconds"
  [socket]

  (println "start-example-broadcaster!")

  (go-loop [i 0]
    (<! (async/timeout 1000))
    (broadcast! socket {:what-is-this "An async broadcast pushed from server"
                        :how-often    "Every 1 second"
                        :i            i})
    (recur (inc i))))