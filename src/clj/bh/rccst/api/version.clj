(ns bh.rccst.api.version
  (:require [clojure.tools.logging :as log]
            [compojure.core :as compojure]
            [compojure.api.sweet :as sweet]
            [schema.core :as s]
            [bh.rccst.api.common :as c]))


(s/defschema VersionResponse
  {:version s/Str})


(defn- get-version []
  (log/info "get-version")
  {:version "0.0.1"})


(def version-handler
  (sweet/GET "/version" _
    :return VersionResponse
    :summary "get the version number from the server"
    (c/wrapper (get-version))))


; test schema
(comment
  (s/validate VersionResponse (get-version))
  (s/validate VersionResponse {})
  (s/validate VersionResponse nil)

  ())