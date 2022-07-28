(ns rccst.components.repl
  (:require [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :as nrepl]
            [com.stuartsierra.component :as component]

            [rccst.defaults :as default]))


(defrecord nRepl [nrepl-port nrepl]
  component/Lifecycle
  (start [component]

    (let [p (or nrepl-port default/nRepl-port)]
      (log/info "starting nrepl" p)
      (tap> ["starting nrepl" p])

      (assoc component :nrepl (nrepl/start-server :port p))))

  (stop [component]
    (log/info "stopping nrepl" nrepl)
    (tap> ["stopping nrepl" nrepl])

    (nrepl/stop-server nrepl)
    (assoc component :nrepl nil)))

