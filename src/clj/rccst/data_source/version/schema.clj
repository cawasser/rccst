(ns rccst.data-source.version.schema
  (:require [schema.core :as s]))


(s/defschema VersionResponse
  {:version s/Str
   :build-date s/Str})


