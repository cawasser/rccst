(ns bh.rccst.components.websocket.handler
  (:require [clojure.tools.logging :as log]))


(defmulti -client-ev-handler (fn [_ y] (:id y)))


(defn client-ev-handler [sources ev-msg]
  (-client-ev-handler sources ev-msg))


(defmethod -client-ev-handler :default
  [sources {:as ev-msg
            :keys [?reply-fn ch-recv client-id connected-uids
                   uid event id ring-req ?data send-fn]}]
  (case id
    :chsk/uidport-open (do
                         (log/info "Port open to UID: " uid))
                         ;(subman/add-empty-user uid))       ;; when new user connection opens, create a subscription for them
    :chsk/uidport-close (do
                          (log/info "Port closed to UID: " uid))
                          ;(subman/remove-user uid))       ;; when connection closes, cleanup users subscriptions.
    :chsk/ws-ping       (log/info "ws-ping" uid)
    (log/info "un-handled client event" id)))

