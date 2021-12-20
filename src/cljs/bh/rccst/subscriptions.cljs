(ns bh.rccst.subscriptions
  ;(:require-macros
  ;  [cljs.core.async.macros :as asyncm :refer (go go-loop)])
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [taoensso.timbre :as log]
    ;[cljs.core.async :as async :refer (<! >! put! chan)]
    [taoensso.sente :as sente]
    [bh.rccst.subscription-handlers :as handlers]))


(declare start!)


(re-frame/reg-event-fx
  ::start
  (fn-traced [_ _]
    (start!)))


(def router_ (atom nil))

(def ch-chsk (atom nil))
(def chsk-send! (atom nil))
(def chsk-state (atom nil))

(def config {:type     :auto
             :packer   :edn
             :protocol :http
             :host     "localhost"
             :port     8280})   ; this is the port of the "real server"


(defn state-watcher [_key _atom _old-state new-state]
  (log/warn "New state" new-state))


(defn create-client! []
  (let [{:keys [ch-recv send-fn state]} (sente/make-channel-socket-client! "/chsk" nil config)]
    (reset! ch-chsk ch-recv)
    (reset! chsk-send! send-fn)
    (add-watch state :state-watcher state-watcher)))


(defn stop-router! []
  (when-let [stop-f @router_] (stop-f)))


(defn start-router! []
  (stop-router!)
  (reset! router_ (sente/start-client-chsk-router!
                    @ch-chsk handlers/event-msg-handler)))


(defn start! []
  (log/info "starting the bh.rccst.websocket")
  (create-client!)
  (start-router!))


(comment
  (start!)


  ())