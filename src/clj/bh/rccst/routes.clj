(ns bh.rccst.routes
  (:require [clojure.tools.logging :as log]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]

            [bh.rccst.websocket :as socket]))



(defroutes local-routes
  (GET "/chsk" req (socket/ring-ajax-get-or-ws-handshake req))
  (POST "/chsk" req (socket/ring-ajax-post req))

  (route/not-found "<div><h1>headless Server</h1><h3>This server only provides websockets</h3><h3>dummy</h3></div>"))


(def routes
  (-> local-routes
    ;; Add necessary Ring middleware:
    (wrap-defaults site-defaults)
    (wrap-cors :access-control-allow-origin [#".*"])))

