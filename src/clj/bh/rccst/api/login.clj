(ns bh.rccst.api.login
  (:require [compojure.api.sweet :as sweet]
            [schema.core :as s]
            [bh.rccst.api.common :as c]))


(s/defschema LoginSuccess
  {:user-id s/Str :uuid s/Str})



(def login-handler
  (sweet/GET "/login" _
    :return LoginSuccess
    :summary "log into the server"
    (c/wrapper {:user-id "dummy" :uuid "dummy-uuid"})))
