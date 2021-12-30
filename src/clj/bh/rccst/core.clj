(ns bh.rccst.core
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
            [taoensso.sente.packers.transit :as sente-transit]

            [bh.rccst.components.broadcast :as broadcast]
            [bh.rccst.components.db.db :as db]
            [bh.rccst.components.repl :as repl]
            [bh.rccst.components.system :as system]
            [bh.rccst.components.webserver :as server]
            [bh.rccst.components.websocket :as socket]
            [bh.rccst.components.websocket.publish]
            [bh.rccst.data-source.subscribers :as subscribers]))



(def http-port 8280)
(def nRepl-port 7777)
(def rccst-postgres
  "postgresql database connection spec."
  {:dbtype   "postgresql"
   :dbname   "rccst"
   :user     "postgres"
   :password "Password"
   :host     "localhost"
   :port     "5432"})

(def rccst-sqlite
  "sqlite database connection spec."
  {:dbtype   "sqlite"
   :dbname   "rccst"})


(def last-req (atom {}))

(defn- csrf-fn [ring-req]
  (log/info "csrf-fn" ring-req)
  (reset! last-req ring-req)
  (or (:anti-forgery-token ring-req)
    (get-in ring-req [:session :csrf-token])
    (get-in ring-req [:session :ring.middleware.anti-forgery/anti-forgery-token])
    (get-in ring-req [:session "__anti-forgery-token"])
    #_:sente/no-reference-csrf-token))


(defn- user-fn [ring-req]
  (:client-id ring-req))


(defn new-system [args _]
  (let []
    (component/system-map
      :database (db/map->Database args)
      :server (component/using (server/map->HTTPServer args) [:socket :database])
      :nrepl (repl/start-repl args)
      :socket (socket/map->WebSocketServer args)
      :broadcast (component/using (broadcast/map->Broadcast args) [:socket])
      :subscriptions (component/using (subscribers/map->Subscribers args) [:socket]))))


(defn start! [db-type]
  (reset! system/system
    (component/start
      (new-system {:host              "localhost"
                   :port              http-port
                   :nrepl             nRepl-port
                   :dev-mode          false
                   :db-spec           db-type
                   :socket-params     {:user-id-fn    user-fn
                                       :packer        (sente-transit/get-transit-packer)
                                       :csrf-token-fn csrf-fn}
                   :broadcast-timeout 5}                    ; in seconds
        {}))))


(defn -main [& args]
  (log/info "This is the main entrypoint!")
  (start! rccst-postgres))


; run things from the REPL
(comment
  (-main rccst-sqlite)

  (def db-type rccst-postgres)
  (def db-type rccst-sqlite)

  ; dev-mode
  (do
    (set-init (partial new-system
                {:host              "localhost"
                 :port              http-port
                 :nrepl             nRepl-port
                 :db-spec           db-type
                 :dev-mode          true
                 :socket-params     {:user-id-fn    user-fn
                                     :packer        (sente-transit/get-transit-packer)
                                     :csrf-token-fn nil}
                 :broadcast-timeout 5}))
    (start)
    (reset! system/system system))

  ; prod-mode
  (do
    (set-init (partial new-system
                {:host              "localhost"
                 :port              http-port
                 :nrepl             nRepl-port
                 :db-spec           db-type
                 :dev-mode          false
                 :socket-params     {:user-id-fn    user-fn
                                     :packer        (sente-transit/get-transit-packer)
                                     :csrf-token-fn csrf-fn}
                 :broadcast-timeout 5}))
    (start)
    (reset! system/system system))

  (def r @last-req)

  (:server system)
  (:nrepl system)
  (:socket system)
  (:broadcast system)
  (:subscriptions system)

  @(get-in system [:subscriptions :subscriptions])

  (start)
  (stop)
  (reset)

  ())


; using the jack-in Repl
(comment
  @system/system

  (:server @system/system)
  (:nrepl @system/system)
  (:socket @system/system)
  (:broadcast @system/system)


  (get-in @system/system [:subscriptions :subscriptions])

  ())