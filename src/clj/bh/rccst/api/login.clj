(ns bh.rccst.api.login
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :as sweet]

            [bh.rccst.api.common :as c]
            [bh.rccst.components.db.users :as users]))






;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Endpoints / Routes
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn login-handlers [database]
  (sweet/context "/user" []
    :tags ["user"]

    (sweet/GET "/users" _
      :return users/Users
      :summary "return all the users currently logged in"
      (do
        (log/info "users")
        (c/wrapper (users/users database))))

    (sweet/POST "/register" []
      :return users/RegisterResponse
      :body [{:keys [user-id password]} users/LoginRequest]
      :summary "log into the server"
      (do
        (log/info "register" user-id)
        (c/wrapper (users/register database user-id password))))

    (sweet/POST "/registered" []
      :return users/RegisterResponse
      :body [{:keys [user-id] :as user} users/User]
      :summary "is the given user-id registered?"
      (do
        (log/info "logged-in" user "////" user-id)
        (c/wrapper (users/user-registered? database user-id))))

    (sweet/POST "/login" []
      :return users/LoginResponse
      :body [{:keys [user-id password]} users/LoginRequest]
      :summary "log into the server"
      (do
        (log/info "login" user-id "////" password)
        (c/wrapper (users/login database user-id password))))))


