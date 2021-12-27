(ns bh.rccst.components.webserver
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :as server]

            [bh.rccst.api.routes :as routes]))


(defrecord HTTPServer [dev-mode port server socket]
  component/Lifecycle

  (start [component]
    (log/info ";; Starting HTTP server" port server)
    (tap> "starting server")
    (let [server (server/run-server (routes/routes socket dev-mode) {:port port})]
      (assoc component :server server)))

  (stop [component]
    (log/info ";; Stopping HTTP server")
    (server :timeout 100)
    (assoc component :server nil)))


(defn new-http-server
  [port]
  (map->HTTPServer {:port port}))

