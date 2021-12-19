(ns bh.rccst.webserver
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.server :as server]
            [clojure.tools.logging :as log]

            [bh.rccst.routes :as routes]))


(defrecord HTTPServer [port server]
  component/Lifecycle

  (start [component]
    (println ";; Starting HTTP server" port server)
    (tap> "starting server")
    (let [server (server/run-server #'routes/routes {:port port})]
      (assoc component :server server)))

  (stop [component]
    (println ";; Stopping HTTP server")
    (server :timeout 100)
    (assoc component :server nil)))


(defn new-http-server
  [port]
  (map->HTTPServer {:port port}))