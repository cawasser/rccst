(ns bh.rccst.webserver
  (:require [com.stuartsierra.component :as component]
            [ring.adapter.jetty :as jetty]
            ;[org.httpkit.server :as server]
            [clojure.tools.logging :as log]

            [bh.rccst.routes :as routes]))


(defrecord HTTPServer [port server]
  component/Lifecycle

  (start [component]
    (log/info ";; Starting HTTP server" port server)
    (tap> "starting server")
    (let [server (jetty/run-jetty #'routes/routes {:port port :join? false})]
      (assoc component :server server)))

  (stop [component]
    (println ";; Stopping HTTP server")
    (.stop server)
    (assoc component :server nil)))


(defn new-http-server
  [port]
  (map->HTTPServer {:port port}))