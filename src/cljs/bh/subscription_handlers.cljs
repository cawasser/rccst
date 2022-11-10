(ns bh.subscription-handlers
  (:require [re-frame.core :as rf]
            [taoensso.timbre :as log]
            [bh.data-source-handler :as dh]
            [day8.re-frame.tracing :refer-macros [fn-traced]]))


(rf/reg-event-db
  ::fully-opened-socket-start
  (fn-traced [db [_ id]]
             (log/info "::sub-handler/fully-opened-websocket-start" id)
             (assoc db :pub-sub-started? true)))


(defmulti -event-msg-handler :id)


(defn event-msg-handler
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))


(defmethod -event-msg-handler :default
  [{:keys [event]}]
  (log/info "Unhandled event:" event))


(defmethod -event-msg-handler :chsk/recv
  [{:keys [_ ?data] :as msg}]

  ;(log/info "Push event from server:" ?data)
  (dh/data-source-msg-handler ?data))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (let [[old-state-map new-state-map] ?data]
    (if (:first-open? new-state-map)
      (do (rf/dispatch-sync [::fully-opened-socket-start])
        (log/info "Channel socket successfully established!: " new-state-map))
      (log/info "Channel socket state change: " new-state-map))))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (log/info "WS Handshake for: " ?uid " data: " ?handshake-data)))

(defmethod -event-msg-handler :chsk/ws-ping
  [{:keys [event]}]
  (log/info "WS Ping"))

