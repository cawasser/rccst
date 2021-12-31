(ns bh.rccst.components.db.users
  (:require [hugsql.adapter.next-jdbc :as next-adapter]
            [hugsql.core :as hugsql]
            [clojure.tools.logging :as log]
            [schema.core :as s]

            [bh.rccst.components.system :as system]))


; Instantiate hugsql functions, with an adapter for next-jdbc
(hugsql/def-db-fns "sql/users.sql"
  {:adapter (next-adapter/hugsql-adapter-next-jdbc)})


(defn create-users-table [db]
  (create-users-table! db))


(defn drop-users-table [db]
  (drop-users-table! db))


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
  {:logged-in             s/Bool
   (s/optional-key :uuid) s/Str})

(s/defschema RegisterSuccess
  {:registered            s/Bool
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

(defn register [database user-id password]
  (log/info "users/register" user-id)
  (if-let [user (get-user database {:username user-id})]
    {:registered false}
    (do
      (create-new-user!
        database
        {:username user-id
         :uuid     (str user-id "-uuid")
         :pass     password})
      {:registered true})))


(defn login [database user-id password]
  (log/info "users/login" user-id "////" password)
  (if-let [user (verify-credentials database {:username user-id :pass password})]
    {:logged-in true :uuid (:uuid user)}
    {:logged-in false}))


(defn users [database]
  (log/info "users/users" database)
  {:users (into []
            (map :username (get-users database)))})


(defn user-registered? [database user-id]
  (log/info "users/user-registered?" user-id)
  (if-let [user (get-user database {:username user-id})]
    {:registered true :uuid (:uuid user)}
    {:registered false}))




; work with the "new" database
(comment
  (def user-id "dummy")
  (def database (get-in @system/system [:database :database]))

  (create-database! database)

  (create-user-table! database)

  (get-user database {:username user-id})
  (get-users database)


  (do
    (def user-id "dummy")
    (create-new-user!
      database
      {:username user-id
       :uuid     (str user-id "-uuid")
       :pass     (str user-id "-password")})
    (get-user database {:username user-id})
    (verify-credentials
      database
      {:username user-id
       :pass     (str user-id "-password")}))

  (do
    (def user-id "user-2")
    (create-new-user!
      database
      {:username user-id
       :uuid     (str user-id "-uuid")
       :pass     (str user-id "-password")})
    (get-user database {:username user-id})
    (verify-credentials
      database
      {:username user-id
       :pass     (str user-id "-password")}))



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
