(ns bh.rccst.components.websocket
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
            [clojure.core.async :as async :refer [go-loop <!]]))


(defrecord WebSocketServer [socket-params socket]
  component/Lifecycle
  (start [component]

    (log/info "starting Socket" socket-params socket)
    (tap> "starting socket")

    (let [{:keys [ch-recv send-fn connected-uids
                  ajax-post-fn ajax-get-or-ws-handshake-fn]}
          (sente/make-channel-socket! (get-sch-adapter) socket-params)]

      (assoc component
        :ring-ajax-post ajax-post-fn
        :ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
        :ch-recv ch-recv
        :chsk-send! send-fn
        :connected-uids connected-uids)))

  (stop [component]
    (log/info "stopping socket" socket)
    (assoc component
      :ring-ajax-post nil
      :ring-ajax-get-or-ws-handshake nil
      :ch-recv nil
      :chsk-send! nil
      :connected-uids nil)))


(defn broadcast! [socket msg]
  (let [uids (:any @(:connected-uids socket))]
    (doseq [uid uids]
      (log/info "broadcast to user: " uid)
      ((:chsk-send! socket) uid
       [:some/broadcast (assoc msg :to-whom uid)]))))


(defn start-example-broadcaster!
  "As an example of server>user async pushes, setup a loop to broadcast an
  event to all connected users every 10 seconds"
  [socket]

  (log/info "start-example-broadcaster!")

  (go-loop [i 0]
    (<! (async/timeout 1000))
    (broadcast! socket
      {:what-is-this "An async broadcast pushed from server"
       :how-often    "Every 1 second"
       :i            i})
    (recur (inc i))))



