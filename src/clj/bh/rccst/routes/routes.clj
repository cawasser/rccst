(ns bh.rccst.routes.routes
  (:require [clojure.tools.logging :as log]
            [compojure.route :as route]

            [bh.rccst.api.api :as api]
            [bh.rccst.routes.dev-mode :as dev-mode]
            [bh.rccst.routes.prod-mode :as prod-mode]))


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
   database pub-sub dev-mode]

  (log/info "setting up the routes" dev-mode database)

  (if dev-mode

    (dev-mode/routes
      ring-ajax-post ring-ajax-get-or-ws-handshake
      database pub-sub)

    (prod-mode/routes
      ring-ajax-post ring-ajax-get-or-ws-handshake
      database pub-sub)))




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



(comment

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
                       :store        (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))

      ; while the "general" routes need all the transit and parsing support
      (-> (compojure.core/routes
            (GET "/" _ (render "public/index.html"))
            (route/resources "/")

            (#'api/api database pub-sub)

            (route/not-found "<h1>Page not found</h1>"))

        ;need to turn "off" the built-in anti-forgery because it returns HTML, and we need transit
        (wrap-defaults
          (assoc-in site-defaults [:security :anti-forgery] false))
        (wrap-cors :access-control-allow-origin [#".*"])
        (wrap-restful-format :formats [:transit-json :edn])
        wrap-keyword-params
        wrap-params
        (wrap-session {:cookie-attrs {:max-age 3600}
                       :store        (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))) ;(do

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
                       :store        (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))

      ; while the "general" routes need all the transit and parsing support
      (-> (compojure.core/routes
            (GET "/" _ (render "public/index.html"))
            (route/resources "/")

            (#'api/api database pub-sub)

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
                       :store        (cookie-store {:key (byte-array (.getBytes "ahY9poQuaghahc7I"))})}))))



  ())