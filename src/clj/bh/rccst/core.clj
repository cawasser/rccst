(ns bh.rccst.core
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl :refer [reset set-init start stop system]]

            [bh.rccst.components.webserver :as server]
            [bh.rccst.components.websocket :as socket]
            [bh.rccst.components.broadcast :as broadcast]
            [bh.rccst.components.system :as system]
            [bh.rccst.components.repl :as repl]
            [bh.rccst.data-source.subscribers :as subscribers]
            
            [bh.rccst.components.websocket.publish]))


(def http-port 8280);5555)
(def nRepl-port 7777)


(defn new-system [args _]
  (let []
    (component/system-map
      :server (component/using (server/map->HTTPServer args) [:socket])
      :nrepl (repl/start-repl args)
      :socket (socket/map->WebSocketServer args)
      :broadcast (component/using (broadcast/map->Broadcast args) [:socket])
      :subscriptions (component/using (subscribers/map->Subscribers args) [:socket]))))


(defn start! []
  (reset! system/system
    (component/start
      (new-system {:host              "localhost"
                   :port              http-port
                   :nrepl             nRepl-port
                   :socket-params     {:packer        :edn
                                       :csrf-token-fn nil}
                   :broadcast-timeout 5}                      ; in seconds
        {}))))


(defn -main [& args]
  (log/info "This is the main entrypoint!")
  (start!))


; run things from the REPL
(comment
  (-main)

  (do
    (set-init (partial new-system
                {:host              "localhost"
                 :port              http-port
                 :nrepl             nRepl-port
                 :socket-params     {:packer        :edn
                                     :csrf-token-fn nil}
                 :broadcast-timeout 5}))
    (start)
    (reset! system/system system))

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