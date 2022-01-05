(ns bh.rccst.api.api
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :as sweet]

            [bh.rccst.api.login :as login]
            [bh.rccst.api.subscribe :as subscribe]
            [bh.rccst.api.version :as version]
            [bh.rccst.api.lookup :as lookup]))


(defn api
  "Pulls together all the routes and their handlers from across the architecture.

  Mixes together the handlers for:
  - [version](/docs/api/bh.rccst.api.verison.html)
  - [lookup](/docs/api/bh.rccst.data-source.lookup.html)
    - ([schema](/docs/api/bh.rccst.data-source.lookup.schema.html))
  - [login](/docs/api/bh.rccst.api.login.html)
  - [subscribe](/docs/api/bh.rccst.api.subscripbe.html)

  ---

  - database : the Database Component, required for handlers to access durable storage via SQL
  - subscriptions : the Subscriptions Component, required to handle `/subscribe` context of URLs

  returns: the (swagger-ui enhanced) [api](https://github.com/metosin/compojure-api#api-with-schema--swagger-docs)

  > See also:
  >
  > [Compojure](https://github.com/weavejester/compojure)
  > [Compojure-sweet](https://github.com/metosin/compojure-api)
  "

  [{:keys [database]} pub-sub]
  (log/info "generate api")
  (sweet/api
    {:swagger
     {:ui "/api-docs"
      :spec "/swagger.json"
      :data {:info {:title "RCCST API"
                    :description "Web API for the RCCST exploratory app"}
             :tags [{:name "api", :description "general purpose endpoints"}
                    {:name "user" :description "endpoints for managing and using User accounts"}
                    {:name "subscribe" :description "endpoints for User Pub/Sub to data-sources"}]
             :consumes ["application/transit+json" "application/edn"]
             :produces ["application/transit+json"  "application/edn"]}}}

    (sweet/context "/" []
      :tags ["api"]

      #'version/version-handler
      #'lookup/lookup-handler)

    ; "user"
    (#'login/login-handlers database)

    ; subscription
    (#'subscribe/subscription-handlers pub-sub)))

    ; "initialization"

    ; "personalization"

    ; ""