(ns bh.rccst.routes
  (:require [clojure.tools.logging :as log]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [compojure.core :refer [GET POST]]
            [compojure.route :as route]))


(defn routes [{:as socket
               :keys [ring-ajax-post ring-ajax-get-or-ws-handshake]}]
  (println "setting up the routes" ring-ajax-get-or-ws-handshake)
  (-> (compojure.core/routes
        (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
        (POST "/chsk" req (ring-ajax-post req))

        (route/not-found "<div><h1>headless Server</h1><h3>This server only provides websockets</h3><h3>dummy</h3></div>"))

      ;; Add necessary Ring middleware:
      (wrap-defaults site-defaults)
      (wrap-cors :access-control-allow-origin [#".*"])))

