(ns bh.rccst.components.webserver
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [org.httpkit.server :as server]

            [bh.rccst.routes.routes :as routes]
            [bh.rccst.defaults :as default]))


(defrecord HTTPServer
  [dev-mode port server socket database subscriptions]
  component/Lifecycle

  (start [component]
    (let [p (or port default/http-port)
          server (server/run-server (#'routes/routes socket database subscriptions dev-mode) {:port p})]
      (log/info ";; Starting HTTP server" port server)
      (tap> ["starting server" port server])

      (assoc component :server server)))

  (stop [component]
    (log/info ";; Stopping HTTP server" server)
    (tap> ["stopping http server" server])

    (server :timeout 100)
    (assoc component :server nil)))


(defn new-http-server
  [port]
  (map->HTTPServer {:port port}))

