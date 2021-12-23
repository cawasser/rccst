(ns bh.rccst.api.api
  (:require [compojure.api.sweet :as sweet]

            [bh.rccst.api.login :as login]
            [bh.rccst.data-source.lookup :as lookup]))


(def api
  (sweet/api
    {:swagger
     {:ui "/api-docs"
      :spec "/swagger.json"
      :data {:info {:title "Sample API"
                    :description "Compojure Api example"}
             :tags [{:name "api", :description "some apis"}]
             :consumes ["application/transit+json"]
             :produces ["application/transit+json"]}}}

    (sweet/context "/" []
      :tags ["api"]

      login/login-handler
      lookup/lookup-handler)))