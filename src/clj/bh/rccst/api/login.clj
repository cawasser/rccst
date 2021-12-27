(ns bh.rccst.api.login
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :as sweet]
            [schema.core :as s]
            [bh.rccst.api.common :as c]))


(def users-logged-in (atom [{:user-id "dummy" :uuid "uuid" :password "dummy"}
                            {:user-id "two" :uuid "uuid" :password "dummy"}
                            {:user-id "three" :uuid "uuid" :password "dummy"}]))


(s/defschema LoginRequest
  {:user-id s/Str :password s/Str})

(s/defschema LoginSuccess
  {:logged-in s/Bool
   (s/optional-key :uuid) s/Str})

(s/defschema RegisterSuccess
  {:registered s/Bool})


(s/defschema User
  {:user-id s/Str})

(s/defschema Users
  {:users [s/Str]})



(defn- register [user-id password]
  (log/info "register" user-id)
  (if-let [user (first (filter #(= user-id (:user-id %)) @users-logged-in))]
    {:registered false}
    (do
      (swap! users-logged-in
        conj {:user-id user-id :uuid "dummy-uuid" :password password})
      {:registered true})))


(defn- login [user-id password]
  (log/info "login" user-id "////" password)
  (if-let [user (first (filter #(= user-id (:user-id %)) @users-logged-in))]
    (if (= password (:password user))
      {:logged-in true :uuid (:uuid user)}
      {:logged-in false})))


(defn- users []
  (log/info "users")
  {:users (into [] (map :user-id @users-logged-in))})


(defn- user-logged-in? [user-id]
  (log/info "user-logged-in?" user-id)
  (if-let [user (first (filter #(= user-id (:user-id %)) @users-logged-in))]
    {:logged-in true :uuid (:uuid user)}
    {:logged-in false}))



(def login-handlers
  (sweet/context "/user" []
    :tags ["user"]

    ;(sweet/POST "/echo-user" []
    ;  :return User
    ;  :body [user User]
    ;  :summary "echoes a User"
    ;  (c/wrapper user))

    (sweet/GET "/users" _
      :return Users
      :summary "return all the users currently logged in"
      (do
        (log/info "users")
        (c/wrapper (users))))

    (sweet/POST "/logged-in" []
      :return LoginSuccess
      :body [{:keys [user-id] :as user} User]
      :summary "is the given user-id logged into the server?"
      (do
        (log/info "logged-in" user "////" user-id)
        (c/wrapper (user-logged-in? user-id))))

    (sweet/POST "/login" []
      :return LoginSuccess
      :body [{:keys [user-id password]} LoginRequest]
      :summary "log into the server"
      (do
        (log/info "login" user-id "////" password)
        (c/wrapper (login user-id password))))

    (sweet/POST "/register" []
      :return RegisterSuccess
      :body [{:keys [user-id password]} LoginRequest]
      :summary "log into the server"
      (do
        (log/info "register" user-id)
        (c/wrapper (register user-id password))))))


