(ns bh.rccst.data-source.common
  (:require [ring.util.http-response :as http]
            [ring.util.response :as rr]))


(defn wrapper [response]
  (http/content-type (rr/response response) "application/transit+json"))