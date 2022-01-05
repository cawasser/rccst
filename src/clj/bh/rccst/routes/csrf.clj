(ns bh.rccst.routes.csrf
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :as http]))


(defn csrf-error-handler
  "csrf-error-handler is needed because we need to be sure anti-forgery only returns TRANSIT,
  not the html that's built-in

  ---

  - request - (string) the request that does NOT include the required CSRF token
  "
  [request]
  (log/info "csrf-error-handler" request)
  (http/content-type
    (http/forbidden
      {:error-text "Invalid CSRF Token"
       :token (get-in request [:headers "x-csrf-token"])})
    "application/transit+json"))