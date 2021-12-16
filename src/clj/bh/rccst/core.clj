(ns bh.rccst.core
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
            [clojure.tools.nrepl.server :as nrepl]

            [bh.rccst.webserver :as server]))



(defn new-system [args _]
  (let []
    (component/system-map
      :server (server/map->HTTPServer args)
      :nrepl (nrepl/start-server :port (:nrepl args)))))


(defn -main [& args]
  (log/info "This is the main entrypoint!")

  (component/start
    (new-system {:host "localhost" :port 8280} {})))



; run things from the REPL
(comment
  (-main)

  (set-init (partial new-system {:host "localhost" :port 8280 :nrepl 7777}))

  (:server system)
  (:nrepl system)

  (start)
  (stop)
  (reset)

  ())