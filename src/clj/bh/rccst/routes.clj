(ns bh.rccst.routes
  (:require [clojure.tools.logging :as log]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [compojure.core :refer [GET POST]]
            [ring.util.http-response :as http]
            [ring.util.response :as rr]
            [compojure.route :as route]))


(defn routes [{:as   socket
               :keys [ring-ajax-post ring-ajax-get-or-ws-handshake]}]
  (println "setting up the routes" ring-ajax-get-or-ws-handshake)
  (-> (compojure.core/routes
        (GET "/" req (http/content-type
                       (rr/resource-response "index.html" {:root "public"})
                       "text/html; charset=utf-8"))
        (route/resources "/")

        (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
        (POST "/chsk" req (ring-ajax-post req))

        (route/not-found "<h1>Page not found</h1>"))

    ;; Add necessary Ring middleware:
    (wrap-defaults site-defaults)
    (wrap-cors :access-control-allow-origin [#".*"])))

