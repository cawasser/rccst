(ns bh.rccst.websocket
  (:require [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
            [clojure.core.async :as async :refer [go-loop <!]]))



(let [{:keys [ch-recv send-fn connected-uids
              ajax-post-fn ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket! (get-sch-adapter)
        {:packer :edn :csrf-token-fn nil})]

  ; these will be replaced when we move to Component
  (def ring-ajax-post ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-chsk ch-recv)                                     ; ChannelSocket's receive channel
  (def chsk-send! send-fn)                                  ; ChannelSocket's send API fn
  (def connected-uids connected-uids))                      ; Watchable, read-only atom


(defn broadcast! [msg]
  (let [uids (:any @connected-uids)]
    (doseq [uid uids]
      (println "broadcast to user: " uid)
      (chsk-send! uid
        [:some/broadcast (assoc msg :to-whom uid)]))))


(defn start-example-broadcaster!
  "As an example of server>user async pushes, setup a loop to broadcast an
  event to all connected users every 10 seconds"
  []

  (println "start-example-broadcaster!")

  (go-loop [i 0]
    (<! (async/timeout 1000))
    (broadcast! {:what-is-this "An async broadcast pushed from server"
                 :how-often    "Every 1 second"
                 :i            i})
    (recur (inc i))))


