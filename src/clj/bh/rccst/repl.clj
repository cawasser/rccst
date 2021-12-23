(ns bh.rccst.repl
  (:require [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :as nrepl]))


(defn start-repl [args]
  (let [port (:nrepl args)]
    (log/info "Starting nRepl at" port)
    (nrepl/start-server :port port)))
