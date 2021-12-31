(ns bh.rccst.core
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [com.stuartsierra.component.repl :refer [reset set-init start stop system]]
            [taoensso.sente.packers.transit :as sente-transit]

            [bh.rccst.components.broadcast :as broadcast]
            [bh.rccst.components.db.db :as db]
            [bh.rccst.components.repl :as repl]
            [bh.rccst.components.system :as system]
            [bh.rccst.components.webserver :as server]
            [bh.rccst.components.websocket :as socket]
            [bh.rccst.components.websocket.publish]
            [bh.rccst.data-source.subscribers :as subscribers]
            [bh.rccst.defaults :as default]))


(def rccst-postgres
  "postgresql database connection spec."
  {:dbtype   "postgresql"
   :dbname   "rccst"
   :user     "postgres"
   :password "Password"
   :host     "localhost"
   :port     "5432"})


(def rccst-sqlite
  "sqlite database connection spec."
  {:dbtype   "sqlite"
   :dbname   "rccst"})


(def last-req
  "last-req is uses to grab the most recent request made by a client so we can explore
  at the repl"
  (atom {}))


(defn- csrf-fn
  "Returns the CSRF token which might have been encoded in one of several places in an
  http request. This function is passed as part of :socket-params to the Sente websocket Component
  initializer.

  - ring-req: the http request from a client (this has already been processed by all the middleware
              wrappers (see https://github.com/ring-clojure/ring/wiki/Concepts#middleware)

   returns: the CSRF token as a string

   > See also:
   >
   > [Sente](https://github.com/ptaoussanis/sente)
   > [CSRF Anti-forgery](https://docs.abp.io/en/abp/3.3/CSRF-Anti-Forgery)
   "

  [ring-req]
  (log/info "csrf-fn" ring-req)
  (reset! last-req ring-req)
  (or (:anti-forgery-token ring-req)
    (get-in ring-req [:session :csrf-token])
    (get-in ring-req [:session :ring.middleware.anti-forgery/anti-forgery-token])
    (get-in ring-req [:session "__anti-forgery-token"])
    #_:sente/no-reference-csrf-token))


(defn- user-fn
  "Returns the :client-id embedded in a socket message from a Client. The :client-id is used
  by Sente to keep track of each connection to the client. In our case, this will be the :user-id
  when the client logs into the App

  - ring-req: the http request, encoded asn a has-map which is expected to contain the :client-id key

  returns: the client identifier as a string

  > See also:
  >
  > [Sente](https://github.com/ptaoussanis/sente)
  "

  [ring-req]
  (:client-id ring-req))


(defn new-system
  "Constructs a system-map, per Stuart Sierra Component. This brings together all the various
  'stateful' Components of the system:

   - _Database_       - durable SQL storage
   - _Web Server_     - handling URL Endpoints
   - _Socket Server_  - data Pub/Sub
   - _nRepl_          - providing a remote Repl for interactive development
   - _Broadcast_      - a timer-based publication mechanism (temporary)
   - _Subscription_   - mechanism for managing Client subscriptions to data-sources

  > These Components form a Directed Acyclic Graph (DAG) based upon their interdependencies.
  ![DAG](/docs/figures/system-component-dag.png)

  - args - (hash-map) configuration items for the various Components, expects the following keys:
    1. :host - (string) DNS name for the Web Server, defaults to `localhost`
    2. :port - (integer) TCP port number for the Web Server, defaults to 8280
    3. :nrepl - (integer) TCP port for the embedded [nREPL](https://nrepl.org/nrepl/index.html), defaults to 7777
    4. :dev-mode - (boolean) `true` or `false`. If `true` hen we start _without_ CSRF anti-forgery. Use this when
                   you want to do development and have the `/api-docs/` (Swagger-UI) page support `POST`
                   and `PUT` endpoint examples.
    5. :db-spec - (hash-map) database configuration information. Two are currently provided:
      1. rccst-postgres - configuration for a locally hosted [Postgres](https://www.postgresql.org) database
      2. rccst-sqlite - configuration for a locally hosted [SQLite](https://sqlite.org/index.html) database.
    6. :socket-params - (hash-map) configuration parameters for the [Sente](https://github.com/ptaoussanis/sente)-based websocket.
    Expected keys are:
      1. :client-fn - a function to extract the client-id from the incoming HTTP request
      2. :packer - function to marshall data into /out-of the socket. We currently are using [Transit](https://github.com/cognitect/transit-clj),
      hence `(sente-transit/get-transit-packer)`
      3. :csfr-token-fn - function to return the CSRF, if using CSRF Anti-forgery, `nil` of not.
    7. :broadcast-timeout - (integer) number of seconds between 'publish' events, defaults to 5 seconds

  > See also:
  >
  > [Component](https://github.com/stuartsierra/component)
  > [nREPL](https://nrepl.org/nrepl/index.html)
  > [Postgres](https://www.postgresql.org)
  > [Sente](https://github.com/ptaoussanis/sente)
  > [SQLite](https://sqlite.org/index.html)
  "

  [args _]
  (let []
    (component/system-map
      :database (db/map->Database args)
      :server (component/using (server/map->HTTPServer args) [:socket :database])
      :nrepl (repl/start-repl args)
      :socket (socket/map->WebSocketServer args)
      :broadcast (component/using (broadcast/map->Broadcast args) [:socket])
      :subscriptions (component/using (subscribers/map->Subscribers args) [:socket]))))


(defn start!
  "Starts the system, defined by `(new-system)`. Presumes a _production_-type environment, so
  it will use CSRF tokens.

  > Note: the `/api-docs/` (Swagger-UI) does **NOT** support CSRf on `POST` or `POST` endpoints!
  >
  > If you want to work with the complete API using `/api-docs/`, you will need to start the server
  > manually at the Repl, so you can access _dev-mode_ (See the Rich Comments).

  - db-type - which database server to use, we support 2: [_Postgres_}(https://www.postgresql.org)
  and [_Sqlite_](https://sqlite.org/index.html)

  returns: an atom holding the started `SystemMap` _object_ returned from Component.

  > See also:
  >
  > [Component](https://github.com/stuartsierra/component)
  "

  [db-type]
  (reset! system/system
    (component/start
      (new-system {:host              "localhost"
                   :port              default/http-port
                   :nrepl             default/nRepl-port
                   :dev-mode          false
                   :db-spec           db-type
                   :socket-params     {:user-id-fn    user-fn
                                       :packer        (sente-transit/get-transit-packer)
                                       :csrf-token-fn csrf-fn}
                   :broadcast-timeout default/broadcast-timeout}                    ; in seconds
        {}))))


(defn -main
  "Runs the app.

  - args - a vector of command-line arguments. Currently, ignored.

  calls `start!` to build the Components and start them running

  > Se also:
  >
  > [Component](https://github.com/stuartsierra/component)
  "
  [& args]
  (log/info "RCCST Starting up...")
  (start! rccst-postgres))


; run things from the REPL
(comment
  ; this will start in PROD-MODE!
  (-main rccst-sqlite)

  (def db-type rccst-postgres)
  (def db-type rccst-sqlite)

  ; dev-mode!
  (do
    (set-init (partial new-system
                {:host              "localhost"
                 ;:port              default/http-port
                 ;:nrepl             default/nRepl-port
                 :db-spec           db-type
                 :dev-mode          true
                 :socket-params     {:user-id-fn    user-fn
                                     :packer        (sente-transit/get-transit-packer)
                                     :csrf-token-fn nil}}))
                 ;:broadcast-timeout default/broadcast-timeout}))
    (start)
    (reset! system/system system))

  ; prod-mode!
  (do
    (set-init (partial new-system
                {:host              "localhost"
                 :port              default/http-port
                 :nrepl             default/nRepl-port
                 :db-spec           db-type
                 :dev-mode          false
                 :socket-params     {:user-id-fn    user-fn
                                     :packer        (sente-transit/get-transit-packer)
                                     :csrf-token-fn csrf-fn}
                 :broadcast-timeout default/broadcast-timeout}))
    (start)
    (reset! system/system system))

  (def r @last-req)

  (:server system)
  (:nrepl system)
  (:socket system)
  (:broadcast system)
  (:subscriptions system)

  @(get-in system [:subscriptions :subscriptions])

  (start)
  (stop)
  (reset)

  ())


; using the jack-in Repl
(comment
  @system/system

  (:server @system/system)
  (:nrepl @system/system)
  (:socket @system/system)
  (:broadcast @system/system)


  (get-in @system/system [:subscriptions :subscriptions])

  ())