(ns bh.rccst.api.common
  (:require [ring.util.http-response :as http]
            [ring.util.response :as rr]))


(defn wrapper
  " Wraps the http response with the `transit+json` content type. This ensures _ALL_ responses
  will be `transit+json

  ---

  - response - (hash-map) the HTTP response being sent back to the requester

  > See also:
  >
  > [Ring](https://github.com/ring-clojure/ring)
  > [Http-response](https://github.com/metosin/ring-http-response)
  ? [Transit](https://github.com/cognitect/transit-clj)
  "
  [response]
  (http/content-type (rr/response response) "application/transit+json"))