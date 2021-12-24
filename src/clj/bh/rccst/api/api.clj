(ns bh.rccst.api.api
  (:require [compojure.api.sweet :as sweet]

            [bh.rccst.api.login :as login]
            [bh.rccst.api.version :as version]
            [bh.rccst.data-source.lookup :as lookup]))


(def api
  (sweet/api
    {:swagger
     {:ui "/api-docs"
      :spec "/swagger.json"
      :data {:info {:title "RCCST API"
                    :description "Web API for the RCCTS exploratory app"}
             :tags [{:name "api", :description "general purpose endpoints"}
                    {:name "user" :description "endpoints for managing and using User accounts"}]
             :consumes ["application/transit+json"]
             :produces ["application/transit+json"]}}}

    (sweet/context "/" []
      :tags ["api"]

      version/version-handler
      lookup/lookup-handler)

    ; "user"
    login/login-handlers))

    ; "initialization"

    ; "personalization"

    ; ""