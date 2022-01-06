(ns bh.rccst.subscriptions
  ;(:require-macros
  ;  [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [taoensso.timbre :as log]
    [taoensso.sente :as sente]
    [taoensso.sente.packers.transit :as sente-transit]

    [bh.rccst.subscription-handlers :as handlers]
    [bh.rccst.csrf :refer [?csrf-token]]
    [bh.rccst.events :refer [default-header]]))


(declare start!)
(declare stop-router!)


(re-frame/reg-event-fx
  ::start
  (fn-traced [_ [_ id]]
    (start! id)))

(re-frame/reg-event-fx
  ::stop
  (fn-traced [_ _]
    (stop-router!)))

(re-frame/reg-event-fx
  ::cancel-all
  (fn-traced [{:keys [db]} _]
    (let [user-id (:user-id db)]
      {:http-xhrio (merge default-header
                     {:message :post
                      :url "/subscribe/cancel-all"
                      :params {:user-id user-id}})})))


(def router_ (atom nil))

(def ch-chsk (atom nil))
(def chsk-send! (atom nil))
(def chsk-state (atom nil))

(def config {:type     :auto
             :packer   (sente-transit/get-transit-packer) ;:edn
             :protocol :http
             :host     "localhost"
             :port     8280})   ; this is the port of the "real server"


(defn state-watcher [_key _atom _old-state new-state]
  (log/warn "New state" new-state))


(defn create-client! [id]
  (log/info "create-client" id ?csrf-token)
  (let [{:keys [ch-recv send-fn state]} (sente/make-channel-socket-client!
                                          "/chsk"
                                          ?csrf-token
                                          (assoc config :client-id id))]
    (reset! ch-chsk ch-recv)
    (reset! chsk-send! send-fn)
    (add-watch state :state-watcher state-watcher)))


(defn stop-router! []
  (when-let [stop-f @router_] (stop-f)))


(defn start-router! []
  (stop-router!)
  (reset! router_ (sente/start-client-chsk-router!
                    @ch-chsk handlers/event-msg-handler)))


(defn start! [id]
  (log/info "starting the bh.rccst.websocket" id)
  (create-client! id)
  (start-router!))


(comment
  (start!)


  ())