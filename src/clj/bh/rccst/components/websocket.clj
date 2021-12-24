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
