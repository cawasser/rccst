(ns rccst.api.lookup
  (:require [compojure.api.sweet :as sweet]

            [rccst.api.common :as c]
            [rccst.data-source.lookup :as lookup]
            [rccst.data-source.lookup.schema :as sc]))


(def lookup-handler
  (sweet/GET "/lookup" _
    :return sc/Lookup
    :summary "returns some static data so we can test transit"
    (c/wrapper (lookup/lookup))))
