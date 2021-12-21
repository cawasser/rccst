(ns bh.rccst.core
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
            [clojure.tools.nrepl.server :as nrepl]

            [bh.rccst.webserver :as server]
            [bh.rccst.websocket :as socket]
            [bh.rccst.broadcast :as broadcast]))


(def http-port 8280);5555)
(def nRepl-port 7777)


(defn new-system [args _]
  (let []
    (component/system-map
      :server (component/using (server/map->HTTPServer args) [:socket])
      :nrepl (nrepl/start-server :port (:nrepl args))
      :socket (socket/map->WebSocketServer args)
      :broadcast (component/using (broadcast/map->Broadcast args) [:socket]))))


(defn -main [& args]
  (log/info "This is the main entrypoint!")

  (component/start
    (new-system {:host              "localhost"
                 :port              http-port
                 :nrepl             nRepl-port
                 :socket-params     {:packer        :edn
                                     :csrf-token-fn nil}
                 :broadcast-timeout 5}                      ; in seconds
      {})))



; run things from the REPL
(comment
  (-main)

  (set-init (partial new-system
              {:host              "localhost"
               :port              http-port
               :nrepl             nRepl-port
               :socket-params     {:packer        :edn
                                   :csrf-token-fn nil}
               :broadcast-timeout 5}))
  (start)

  (:server system)
  (:nrepl system)
  (:socket system)
  (:broadcast system)

  (start)
  (stop)
  (reset)


  (socket/start-example-broadcaster! (:socket system))

  ())