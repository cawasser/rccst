(ns bh.rccst.components.database.users
  (:require [hugsql.adapter.next-jdbc :as next-adapter]
            [hugsql.core :as hugsql]
            [clojure.tools.logging :as log]
            [schema.core :as s]

            [bh.rccst.components.system :as system]))


; Instantiate hugsql functions, with an adapter for next-jdbc
(hugsql/def-db-fns "sql/users.sql"
  {:adapter (next-adapter/hugsql-adapter-next-jdbc)})


(defn create-users-table
  ""
  [db]
  (create-users-table! db))


(defn drop-users-table
  "drop the Users table, regardless of whether it is empty or not

  - bh.rccst.components.database - the database Component

  > See also:
  >
  > [HugSQL](https://www.hugsql.org/)
  "
  [db]
  (drop-users-table! db))


(defn init-users-table [_])

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; SCHEMAS for inputs and outputs
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(s/defschema LoginRequest
  "data required to log into the system"
  {:user-id s/Str :password s/Str})

(s/defschema LoginResponse
  "data returned by the login process.

  - :logged-in - `true` if the login was successful, `false` if not
  - :uuid - if the login was successful, this key will be present and contain the uuid associated with the user-id
  "
  {:logged-in                s/Bool
   (s/optional-key :user-id) s/Str
   (s/optional-key :uuid)    s/Str})

(s/defschema RegisterResponse
  "data returned when checking to see if the user is already registered.

  - :registered - `true` if the user is registered , `false` if not
  - :uuid - if the login was successful, this key will be present and contain the uuid associated with the user-id
  "
  {:registered            s/Bool
   (s/optional-key :uuid) s/Str})


(s/defschema User
  "string which uniquely identified the user in the system

  - :user-id
  "
  {:user-id s/Str})

(s/defschema Users
  "string which uniquely identified the user in the system

  - :users - vector of user IDs
  "
  {:users [s/Str]})


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Route Handler Functions
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn register
  "add a new user to the Users table. Check to see if the user-id already exists

  - database - (Component, Database) the database Component
  - user-id - (string) user ID, uniqueness will be enforced at the database
  - password - (string) the password for the user

  > See also:
  >
  > [Component](https://github.com/stuartsierra/component)
  > [HugSQL](https://www.hugsql.org)
  "
  [database user-id password]
  (log/info "users/register" user-id)
  (if-let [user (get-user database {:username user-id})]
    {:registered false}
    (do
      (let [uuid (str user-id "-uuid")]
        (create-new-user!
          database
          {:username user-id
           :uuid     uuid
           :pass     password})
        {:registered true :uuid uuid}))))


(defn login
  "check to see if the user-id/password combination is in the User table.

  - database - (Component, Database) the database Component
  - user-id - (string) user ID
  - password - (string) the password for the user

  > See also:
  >
  > [Component](https://github.com/stuartsierra/component)
  > [HugSQL](https://www.hugsql.org)
  "
  [database user-id password]
  (log/info "users/login" user-id "////" password)
  (if-let [user (verify-credentials database {:username user-id :pass password})]
    {:logged-in true
     :user-id user-id
     :uuid (:uuid user)}
    {:logged-in false}))


(defn users
  "return al the user in the User table.

  - database - (Component, Database) the database Component

  > See also:
  >
  > [Component](https://github.com/stuartsierra/component)
  > [HugSQL](https://www.hugsql.org)
  "
  [database]
  (log/info "users/users" database)
  {:users (into []
            (map :username (get-users database)))})


(defn user-registered?
  "check to see if the user-id is already in the User table

  - database - (Component, Database) the database Component
  - user-id - (string) user ID

  > See also:
  >
  > [Component](https://github.com/stuartsierra/component)
  > [HugSQL](https://www.hugsql.org)
  "
  [database user-id]
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
    (s/validate RegisterResponse))

  (->>
    (user-registered? database "test-user")
    (s/validate RegisterResponse))

  (->>
    (login database "test-user" "test-pwd")
    (s/validate LoginResponse))


  (def gold-register-success {:registered true :uuid "dummy-uuid"})
  (s/validate RegisterResponse gold-register-success)


  ())
