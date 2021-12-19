(ns bh.rccst.subscription-handlers
  (:require [re-frame.core :as rf]
            [taoensso.timbre :as log]
            [bh.rccst.events :as events]))


(defmulti -event-msg-handler :id)


(defn event-msg-handler
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))


(defmethod -event-msg-handler :default
  [{:keys [event]}]
  (log/info "Unhandled event:" event))


(defmethod -event-msg-handler :chsk/recv
  [{:keys [?data]}]
  (rf/dispatch [::events/counter ?data])
  (log/info "Push event from server:" ?data))


;(defmethod -event-msg-handler :data/sync)