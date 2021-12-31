(ns bh.rccst.components.repl
  (:require [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :as nrepl]

            [bh.rccst.defaults :as default]))


(defn start-repl [args]
  (let [port (or (:nrepl args) default/nRepl-port)]
    (log/info "Starting nRepl at" port)
    (nrepl/start-server :port port)))
