(ns bh.rccst.data-source.lookup
  (:require [ring.util.http-response :as http]
            [ring.util.response :as rr]
            [compojure.api.sweet :as sweet]

            [bh.rccst.data-source.lookup.schema :as s]
            [bh.rccst.api.common :as c]))


(def response {:item     "This is a 'Lookup' response"
               :sub-item {:a "it is composed of several Clojure data structures"
                          :b "all nested together"
                          :c #{:like :a :set}
                          :d {:or       :embedded
                              :hashmaps "and keywords all over the place"}}})


(def lookup-handler
  (sweet/GET "/lookup" _
    :return s/Lookup
    :summary "lookup some data in the server"
    (c/wrapper response)))
