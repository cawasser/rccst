(ns bh.rccst.data-source.lookup
  (:require [clojure.tools.logging :as log]
            [ring.util.http-response :as http]
            [ring.util.response :as rr]))



(def response {:item     "This is a 'Lookup' response"
               :sub-item {:a "it is composed of several Clojure data structures"
                          :b "all nested together"
                          :c #{:like :a :set}
                          :d {:or       :embedded
                              :hashmaps "and keywords all over the place"}}})


(defn lookup-handler [req]
  (log/info "lookup-handler" req)
  (println "lookup-handler" req)
  (http/content-type (rr/response response) "application/transit+json"))
