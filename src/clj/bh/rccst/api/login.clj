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
  {:user-id s/Str :uuid s/Str})


(s/defschema RegisterRequest
  {:user-id s/Str})

(s/defschema RegisterSuccess
  {:user-id s/Str :result s/Keyword})


(s/defschema User
  {:user-id s/Str})

(s/defschema Logged-inUser
  {:user-id s/Str :uuid s/Str :password s/Str})

(s/defschema Users
  {:users [s/Str]})

(s/defschema Logged-inSuccess
  {:logged-in s/Bool})


(defn- register [user-id]
  (log/info "register" user-id)
  {:user-id user-id :uuid "dummy-uuid"})


(defn- login [user-id password]
  (log/info "login" user-id "////" password)
  {:user-id user-id :password password})


(defn- users []
  {:users (into [] (map :user-id @users-logged-in))})


(defn- user-logged-in? [user-id]
  (if (empty? (filter #(= user-id (:user-id %)) @users-logged-in))
    {:logged-in false}
    {:logged-in true}))



(s/defschema Pizza
  {:name                         s/Str
   (s/optional-key :description) s/Str
   :size                         (s/enum :L :M :S)
   :origin                       {:country (s/enum :FI :PO)
                                  :city    s/Str}})


(def login-handlers
  (sweet/context "/user" []
    :tags ["user"]


    (sweet/POST "/echo" []
      :return Pizza
      :body [pizza Pizza]
      :summary "echoes a Pizza"
      (c/wrapper pizza))

    (sweet/GET "/users" _
      :return Users
      :summary "return all the users currently logged in"
      (do
        (log/info "users")
        (c/wrapper (users))))

    (sweet/GET "/logged-in" [req]
      :body [{:keys [user-id]} User]
      :return Logged-inSuccess
      :summary "is the given user-id logged into the server?"
      (do
        (log/info "logged-in?" req "////" user-id)
        (c/wrapper (users-logged-in user-id))))

    (sweet/POST "/login" [req]
      :body [{:keys [user-id password]} LoginRequest]
      :return LoginSuccess
      :summary "log into the server"
      (do
        (log/info "login" req "////" user-id "////" password)
        (c/wrapper (login user-id password))))

    (sweet/POST "/register" [req]
      :body [{:keys [user-id]} RegisterRequest]
      :return RegisterSuccess
      :summary "log into the server"
      (do
        (log/info "register" user-id "////" req)
        (c/wrapper (register user-id))))))


; test out schemas
(comment
  (s/validate LoginParams {:user-id "chris" :password "dummy"})
  (s/validate LoginParams {:user-id "chris"})

  (s/validate LoginSuccess {:user-id "chris" :uuid "dummy-uuid"})


  (def user-id "chris")
  (s/validate User {:user-id user-id})
  (s/validate Logged-inSuccess (user-logged-in? user-id))


  ())