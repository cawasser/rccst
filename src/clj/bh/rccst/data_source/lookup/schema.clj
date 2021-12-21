(ns bh.rccst.data-source.lookup.schema
  (:require [schema.core :as s]))


(s/defschema EmbeddedMap
  {:or s/Keyword :hashmaps s/Str})


(s/defschema EmbeddedSet
  #{s/Keyword})


(s/defschema Subitem
  {:a s/Str :b s/Str :c EmbeddedSet :d EmbeddedMap})


(s/defschema Lookup
  {:item     s/Str
   :sub-item Subitem})

