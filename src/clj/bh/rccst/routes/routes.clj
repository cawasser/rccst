(ns bh.rccst.routes.routes
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery *anti-forgery-token*]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.cookie :refer [cookie-store]]
            [compojure.core :refer [GET POST]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.http-response :as http]
            [compojure.route :as route]
            [selmer.parser :as parser]

            [bh.rccst.api.api :as api]))


(parser/set-resource-path! (io/resource "html"))
(parser/add-tag! :csrf-field (fn [_ _] (anti-forgery-field)))


(defn- csrf-error-handler
  "csrf-error-handler is needed because we need to be sure anti-forgery only returns TRANSIT,
  not the html that's built-in

  ---

  - request - (string) the request that does NOT include the required CSRF token
  "
  [request]
  (log/info "csrf-error-handler" request)
  (http/content-type
    (http/forbidden
      {:error-text "Invalid CSRF Token"
       :token (get-in request [:headers "x-csrf-token"])})
    "application/transit+json"))


(defn- render
  "renders the HTML template located relative to resources/html, using Selmer to substitute values
  (given in the params) in place of tags embedded in the html template

  ---

  - template - (string) the 'raw' html page, typically with Selmer tags embedded
  - params - (hash-map, optional) the mapping from 'tags' to 'values' for substitution

  > See also:
  >
  > [Http-response](https://github.com/metosin/ring-http-response)
  > [Selmer](https://github.com/yogthos/Selmer)
  "
  [template & [params]]
  (http/content-type
    (http/ok
      (parser/render-file
        template
        (assoc params
          :page template
          :csrf-token *anti-forgery-token*)))
    "text/html; charset=utf-8"))


(defn routes
  "'routes' provide the middleware wrappers for all the URL endpoint and their handlers

  ---

  - 'socket parameters' - (hash-map)
    1. ring-ajax-post - (function)
    2. ring-ajax-get-or-ws-handshake - (function)
  - database - (Component, Database) the database supporting any URL handlers
  - dev-mode - (bool) if `true` use the CSRF config and wrappers, if `false` do not

  > See also:
  >
  > [Compojure](https://github.com/weavejester/compojure)
  > [Sente](https://github.com/ptaoussanis/sente)
  > [Ring](https://github.com/ring-clojure/ring)
  > [ring.middleware.anti-forgery](https://github.com/ring-clojure/ring-anti-forgery)
  > [ring.middleware.format](https://github.com/ngrunwald/ring-middleware-format)
  > [ring.middleware.defaults](https://github.com/ring-clojure/ring-defaults)
  > [ring.middleware.cors](https://github.com/r0man/ring-cors)
  "
  [{:keys [ring-ajax-post ring-ajax-get-or-ws-handshake]}
   database subscriptions dev-mode]
  (log/info "setting up the routes" dev-mode database)

  (if dev-mode
    ; dev-mode
    (compojure.core/routes
      ; the websocket routes need minimal processing
      (-> (compojure.core/routes
            (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
            (POST "/chsk" req (ring-ajax-post req)))
        ;need to turn "off" the built-in anti-forgery because it returns HTML, and we need transit
        (wrap-defaults
          (assoc-in site-defaults [:security :anti-forgery] false))
        (wrap-cors :access-control-allow-origin [#".*"])
        (wrap-session {:cookie-attrs {:max-age 3600}
                       :store (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))

      ; while the "general" routes need all the transit and parsing support
      (-> (compojure.core/routes
            (GET "/" _ (render "public/index.html"))
            (route/resources "/")

            (#'api/api database subscriptions)

            (route/not-found "<h1>Page not found</h1>"))

        ;need to turn "off" the built-in anti-forgery because it returns HTML, and we need transit
        (wrap-defaults
          (assoc-in site-defaults [:security :anti-forgery] false))
        (wrap-cors :access-control-allow-origin [#".*"])
        (wrap-restful-format :formats [:transit-json :edn])
        wrap-keyword-params
        wrap-params
        (wrap-session {:cookie-attrs {:max-age 3600}
                       :store (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})})));(do

    ; prod-mode
    (compojure.core/routes
      ; the websocket routes need minimal processing
      (-> (compojure.core/routes
            (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
            (POST "/chsk" req (ring-ajax-post req)))
        ;need to turn "off" the built-in anti-forgery because it returns HTML, and we need transit
        (wrap-defaults
          (assoc-in site-defaults [:security :anti-forgery] false))
        (wrap-cors :access-control-allow-origin [#".*"])
        (wrap-anti-forgery {:error-handler csrf-error-handler})
        (wrap-session {:cookie-attrs {:max-age 3600}
                       :store (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))

      ; while the "general" routes need all the transit and parsing support
      (-> (compojure.core/routes
            (GET "/" _ (render "public/index.html"))
            (route/resources "/")

            (#'api/api database)

            (route/not-found "<h1>Page not found</h1>"))

        ;need to turn "off" the built-in anti-forgery because it returns HTML, and we need transit
        (wrap-defaults
          (assoc-in site-defaults [:security :anti-forgery] false))
        (wrap-cors :access-control-allow-origin [#".*"])
        (wrap-restful-format :formats [:transit-json :edn])
        wrap-keyword-params
        wrap-params
        (wrap-anti-forgery {:error-handler csrf-error-handler})
        (wrap-session {:cookie-attrs {:max-age 3600}
                       :store (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})})))))


; work out how sente integrates wth the routes
(comment

  (do
    (def dev-mode true)
    (def ring-ajax-get-or-ws-handshake nil)
    (def ring-ajax-post nil)
    (def database {})
    (def subscriptions {}))


  (tap> (routes {:ring-ajax-post                nil
                 :ring-ajax-get-or-ws-handshake nil}
          database subscriptions true))



  ())

