(ns bh.rccst.api.login
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :as sweet]

            [bh.rccst.api.common :as c]
            [bh.rccst.components.db.users :as users]))


(defn login-handlers
  "Maps handler functions to the URLs, specifically:

  - /user/users
  - /user/register
  - /user/registered?
  - /user/login

  You can experiment with these call via Swagger-UI at

    /api-docs/

  ---

  - database - the Database Component, so the handlers have access

  > See also:
  >
  > [Compojure](https://github.com/weavejester/compojure)
  > [compojure.sweet](https://github.com/metosin/compojure-api)
  "

  [database]
  (log/info "generate login-handlers")
  (let [db (:database database)] ; we only need the :database itself, not all the rest of the config
    (sweet/context "/user" []
      :tags ["user"]

      (sweet/GET "/users" _
        :return users/Users
        :summary "return all the users currently logged into the system"
        (do
          (log/info "users")
          (c/wrapper (#'users/users db))))

      (sweet/POST "/register" []
        :return users/RegisterResponse
        :body [{:keys [user-id password]} users/LoginRequest]
        :summary "log into the server"
        (do
          (log/info "register" user-id)
          (c/wrapper (#'users/register db user-id password))))

      (sweet/POST "/registered" []
        :return users/RegisterResponse
        :body [{:keys [user-id] :as user} users/User]
        :summary "is the given user-id registered?"
        (do
          (log/info "logged-in" user "////" user-id)
          (c/wrapper (#'users/user-registered? db user-id))))

      (sweet/POST "/login" []
        :return users/LoginResponse
        :body [{:keys [user-id password]} users/LoginRequest]
        :summary "log into the server"
        (do
          (log/info "login" user-id "////" password)
          (c/wrapper (#'users/login db user-id password)))))))


