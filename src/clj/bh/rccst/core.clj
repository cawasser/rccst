(ns bh.rccst.core
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
            [clojure.tools.nrepl.server :as nrepl]

            [bh.rccst.webserver :as server]
            [bh.rccst.websocket :as socket]))


(def http-port 5555)
(def nRepl-port 7777)


(defn new-system [args _]
  (let []
    (component/system-map
      :server (server/map->HTTPServer args)
      :nrepl (nrepl/start-server :port (:nrepl args)))))


(defn -main [& args]
  (log/info "This is the main entrypoint!")

  (component/start
    (new-system {:host              "localhost"
                 :port              http-port
                 :nrepl             nRepl-port}
      {})))



; run things from the REPL
(comment
  (-main)

  (set-init (partial new-system
              {:host              "localhost"
               :port              http-port
               :nrepl             nRepl-port}))

  (:server system)
  (:nrepl system)

  (start)
  (stop)
  (reset)


  (socket/start-example-broadcaster!)

  ())