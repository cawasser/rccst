(ns bh.rccst.api.api
  (:require [compojure.api.sweet :as sweet]

            [bh.rccst.api.login :as login]
            [bh.rccst.api.subscribe :as subscribe]
            [bh.rccst.api.version :as version]
            [bh.rccst.data-source.lookup :as lookup]))


(defn api [database]
  (sweet/api
    {:swagger
     {:ui "/api-docs"
      :spec "/swagger.json"
      :data {:info {:title "RCCST API"
                    :description "Web API for the RCCTS exploratory app"}
             :tags [{:name "api", :description "general purpose endpoints"}
                    {:name "user" :description "endpoints for managing and using User accounts"}]
             :consumes ["application/transit+json" "application/edn"]
             :produces ["application/transit+json"  "application/edn"]}}}

    (sweet/context "/" []
      :tags ["api"]

      #'version/version-handler
      #'lookup/lookup-handler)

    ; "user"
    (login/login-handlers database)

    ; subscription
    #'subscribe/subscription-handlers))

    ; "initialization"

    ; "personalization"

    ; ""