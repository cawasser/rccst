(ns bh.rccst.api.version
  (:require [compojure.api.sweet :as sweet]

            [bh.rccst.api.common :as c]
            [bh.rccst.data-source.version :as version]
            [bh.rccst.data-source.version.schema :as s]))



(def version-handler
  "handler for `/version` URL

  > See also:
  >
  > [Compojure](https://github.com/weavejester/compojure)
  > [compojure.sweet](https://github.com/metosin/compojure-api)
  "
  (sweet/GET "/version" _
    :return s/VersionResponse
    :summary "get the version number from the server"
    (c/wrapper (#'version/get-version))))


