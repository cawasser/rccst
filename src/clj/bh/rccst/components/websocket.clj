(ns bh.rccst.components.websocket
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
            [clojure.core.async :as async :refer [go-loop <!]]))


(defn publish-all!
  "publish the given message (msg) to _all_ connected users.

  ---

  - msg : (typically a hash-map) the message to send to all connected users/clients

  > See also:
  >
  > [Sente](https://github.com/ptaoussanis/sente)
  "
  [send-fn connected-uids msg]

  (log/info "publish-all!" send-fn connected-uids msg)

  (let [uids (:any @connected-uids)]
    (doseq [uid uids]
      (log/info "publish! to user: " uid)
      (send-fn uid msg))))


(defrecord WebSocketServer [socket-params socket]
  component/Lifecycle
  (start [component]

    (log/info "starting Socket" socket-params socket)
    (tap> ["starting socket" socket-params socket])

    (let [{:keys [ch-recv send-fn connected-uids
                  ajax-post-fn ajax-get-or-ws-handshake-fn]}
          (sente/make-channel-socket! (get-sch-adapter) socket-params)]

      (assoc component
        :publish-all! (partial publish-all! send-fn connected-uids)
        :ring-ajax-post ajax-post-fn
        :ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn
        :ch-recv ch-recv
        :chsk-send! send-fn
        :connected-uids connected-uids)))

  (stop [component]
    (log/info "stopping socket" socket)
    (tap> ["stopping socket" socket])

    (assoc component
      :publish-all! nil
      :ring-ajax-post nil
      :ring-ajax-get-or-ws-handshake nil
      :ch-recv nil
      :chsk-send! nil
      :connected-uids nil)))


(comment
  (require '[bh.rccst.components.system :as system])

  (def send-fn (get-in @system/system [:socket :publish-all!]))

  @(get-in @system/system [:socket :connected-uids])

  (send-fn [:publish/data-update {:id :number :value 100}])
  (send-fn [:publish/data-update {:id :number :value 500}])
  (send-fn [:publish/data-update {:id :number :value 999}])

  (send-fn [:publish/data-update
            {:id :string :value "This is cool"}])

  ())
