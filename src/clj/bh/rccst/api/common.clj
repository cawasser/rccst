(ns bh.rccst.api.common
  (:require [ring.util.http-response :as http]
            [ring.util.response :as rr]))


(defn wrapper [response]
  (http/content-type (rr/response response) "application/transit+json"))