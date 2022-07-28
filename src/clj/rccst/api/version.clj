(ns rccst.api.version
  (:require [compojure.api.sweet :as sweet]

            [rccst.api.common :as c]
            [rccst.data-source.version :as version]
            [rccst.data-source.version.schema :as s]))



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


