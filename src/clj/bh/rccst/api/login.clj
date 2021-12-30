(ns bh.rccst.api.login
  (:require [clojure.tools.logging :as log]
            [compojure.api.sweet :as sweet]
            [schema.core :as s]

            [bh.rccst.components.system :as system]

            [bh.rccst.api.common :as c]
            [bh.rccst.components.db.db :as db]))


(def users-logged-in (atom [{:user-id "dummy" :uuid "uuid" :password "dummy"}
                            {:user-id "two" :uuid "uuid" :password "dummy"}
                            {:user-id "three" :uuid "uuid" :password "dummy"}]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; SCHEMAS for inputs and outputs
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/defschema LoginRequest
  {:user-id s/Str :password s/Str})

(s/defschema LoginSuccess
  {:logged-in s/Bool
   (s/optional-key :uuid) s/Str})

(s/defschema RegisterSuccess
  {:registered s/Bool
   (s/optional-key :uuid) s/Str})


(s/defschema User
  {:user-id s/Str})

(s/defschema Users
  {:users [s/Str]})


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Route Handler Functions
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- register [database user-id password]
  (log/info "register" user-id)
  (if-let [user (db/get-user database {:username user-id})]
    {:registered false}
    (do
      (db/create-new-user!
        database
        {:username user-id
         :uuid (str user-id "-uuid")
         :pass password})
      {:registered true})))


(defn- login [database user-id password]
  (log/info "login" user-id "////" password)
  (if-let [user (db/verify-credentials database {:username user-id :pass password})]
    {:logged-in true :uuid (:uuid user)}
    {:logged-in false}))


(defn- users [database]
  (log/info "users")
  {:users (into []
            (map :username (db/get-users database)))})


(defn- user-registered? [database user-id]
  (log/info "user-registered?" user-id)
  (if-let [user (db/get-user database {:username user-id})]
    {:registered true :uuid (:uuid user)}
    {:registered false}))


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
      :return Users
      :summary "return all the users currently logged in"
      (do
        (log/info "users")
        (c/wrapper (users database))))

    (sweet/POST "/register" []
      :return RegisterSuccess
      :body [{:keys [user-id password]} LoginRequest]
      :summary "log into the server"
      (do
        (log/info "register" user-id)
        (c/wrapper (register database user-id password))))

    (sweet/POST "/registered" []
      :return RegisterSuccess
      :body [{:keys [user-id] :as user} User]
      :summary "is the given user-id registered?"
      (do
        (log/info "logged-in" user "////" user-id)
        (c/wrapper (user-registered? database user-id))))

    (sweet/POST "/login" []
      :return LoginSuccess
      :body [{:keys [user-id password]} LoginRequest]
      :summary "log into the server"
      (do
        (log/info "login" user-id "////" password)
        (c/wrapper (login database user-id password))))))


; work with the "new" database
(comment
  (def user-id "dummy")
  (def database (get-in @system/system [:database :database]))

  (db/create-database! database)

  (db/create-user-table! database)

  (db/get-user database {:username user-id})
  (db/get-users database)


  (do
    (def user-id "dummy")
    (db/create-new-user!
      database
      {:username user-id
       :uuid (str user-id "-uuid")
       :pass (str user-id "-password")})
    (db/get-user database {:username user-id})
    (db/verify-credentials
      database
      {:username user-id
       :pass (str user-id "-password")}))

  (do
    (def user-id "user-2")
    (db/create-new-user!
      database
      {:username user-id
       :uuid (str user-id "-uuid")
       :pass (str user-id "-password")})
    (db/get-user database {:username user-id})
    (db/verify-credentials
      database
      {:username user-id
       :pass (str user-id "-password")}))



  ())


; now we can test out our helper functions
(comment
  (def database (get-in @system/system [:database :database]))

  (->>
    (users database)
    (s/validate Users))

  (->>
    (register database "test-user" "test-pwd")
    (s/validate RegisterSuccess))

  (->>
    (user-registered? database "test-user")
    (s/validate RegisterSuccess))

  (->>
    (login database "test-user" "test-pwd")
    (s/validate LoginSuccess))



  ())
