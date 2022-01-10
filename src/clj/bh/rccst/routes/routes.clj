(ns bh.rccst.routes.routes
  (:require [clojure.tools.logging :as log]

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



