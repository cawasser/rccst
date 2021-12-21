(ns bh.rccst.data-source.lookup
  (:require [ring.util.http-response :as http]
            [ring.util.response :as rr]

            [compojure.api.sweet :as sweet]
            [schema.core :as s]))


(s/defschema EmbeddedMap
  {:or s/Keyword :hashmaps s/Str})


(s/defschema EmbeddedSet
  #{s/Keyword})


(s/defschema Subitem
  {:a s/Str :b s/Str :c EmbeddedSet :d EmbeddedMap})


(s/defschema Lookup
  {:item     s/Str
   :sub-item Subitem})


(def response {:item     "This is a 'Lookup' response"
               :sub-item {:a "it is composed of several Clojure data structures"
                          :b "all nested together"
                          :c #{:like :a :set}
                          :d {:or       :embedded
                              :hashmaps "and keywords all over the place"}}})


(def lookup-handler
  (sweet/GET "/lookup" _
    :return Lookup
    :summary "lookup some data in the server"
    (http/content-type (rr/response response) "application/transit+json")))
