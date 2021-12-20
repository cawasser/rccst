(ns bh.rccst.routes
  (:require [clojure.tools.logging :as log]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [compojure.core :refer [GET POST]]
            [ring.util.http-response :as http]
            [ring.util.response :as rr]
            [compojure.route :as route]

            [bh.rccst.data-source.lookup :as lookup]))


(defn routes [{:keys [ring-ajax-post ring-ajax-get-or-ws-handshake]}]
  (println "setting up the routes")

  (compojure.core/routes
    ; the websocket routes need minimal processing
    (-> (compojure.core/routes
          (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
          (POST "/chsk" req (ring-ajax-post req)))
      (wrap-defaults site-defaults)
      (wrap-cors :access-control-allow-origin [#".*"]))

    ; while the "general' routes need all the transit and parsing support
    (-> (compojure.core/routes
          (GET "/" _ (http/content-type
                       (rr/resource-response "index.html" {:root "public"})
                       "text/html; charset=utf-8"))
          (route/resources "/")

          (GET "/lookup" req (lookup/lookup-handler req))

          (route/not-found "<h1>Page not found</h1>"))

      (wrap-defaults site-defaults)
      (wrap-cors :access-control-allow-origin [#".*"])
      (wrap-restful-format :formats [:transit-json :edn])
      wrap-keyword-params
      wrap-params)))



