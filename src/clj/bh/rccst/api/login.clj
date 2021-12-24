(ns bh.rccst.api.login
  (:require [compojure.api.sweet :as sweet]
            [schema.core :as s]
            [bh.rccst.api.common :as c]))


(s/defschema LoginRequest
  {:user-id s/Str :password s/Str})

(s/defschema LoginSuccess
  {:user-id s/Str :uuid s/Str})


(s/defschema RegisterRequest
  {:user-id s/Str})

(s/defschema RegisterSuccess
  {:user-id s/Str :result s/Keyword})




(defn- register [user-id]
  {:user-id "chris" :uuid "dummy-uuid"})


(defn- login [user-id password]
  {:user-id "chris" :uuid "dummy-uuid"})


(def login-handlers
  (sweet/context "/user" []
    :tags ["user"]

    (sweet/POST "/login" req
      :body [{:keys [user-id password]} LoginRequest]
      :return LoginSuccess
      :summary "log into the server"
      (c/wrapper (login user-id password)))

    (sweet/POST "/register" req
      :body [{:keys [user-id]} RegisterRequest]
      :return RegisterSuccess
      :summary "log into the server"
      (c/wrapper (register user-id)))))


; test out schemas
(comment
  (s/validate LoginParams {:user-id "chris" :password "dummy"})
  (s/validate LoginParams {:user-id "chris"})

  (s/validate LoginSuccess {:user-id "chris" :uuid "dummy-uuid"})

  ())