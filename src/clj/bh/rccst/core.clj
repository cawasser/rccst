(ns bh.rccst.core
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl :refer [reset set-init start stop system]]

            [bh.rccst.webserver :as server]
            [bh.rccst.websocket :as socket]
            [bh.rccst.broadcast :as broadcast]
            [bh.rccst.system :as system]
            [bh.rccst.repl :as repl]
            
            [bh.rccst.publish]))


(def http-port 8280);5555)
(def nRepl-port 7777)


(defn new-system [args _]
  (let []
    (component/system-map
      :server (component/using (server/map->HTTPServer args) [:socket])
      :nrepl (repl/start-repl args)
      :socket (socket/map->WebSocketServer args)
      :broadcast (component/using (broadcast/map->Broadcast args) [:socket]))))


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

  (start)
  (stop)
  (reset)


  (socket/start-example-broadcaster! (:socket system))

  ())


; using the jack-in Repl
(comment
  @system/system

  (:server @system/system)
  (:nrepl @system/system)
  (:socket @system/system)
  (:broadcast @system/system)

  ())