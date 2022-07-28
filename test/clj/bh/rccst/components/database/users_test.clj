(ns clj.rccst.components.database.users-test
  (:require [clojure.tools.logging :as log]
            [clojure.test :refer [deftest is testing use-fixtures]]
            [schema.core :as schema]
            [next.jdbc :as jdbc]

            [rccst.components.database.users :as sut]))


(defonce database (atom nil))
(def db-spec {:dbtype "sqlite" :dbname "rccst"})

(def gold-register-success {:registered true :uuid "dummy-uuid"})
(def gold-register-failed {:registered false})
(def gold-login-success {:logged-in true :user-id "dummy" :uuid "dummy-uuid"})
(def gold-login-failed {:logged-in false})
(def gold-users-empty {:users []})
(def gold-users-full {:users ["dummy" "dummy-2" "dummy-3"]})




(defn db-fixture
  "Provides a clean 'users' table in a SQLite database, so we can be sure the only data that
  exists is what each test function puts there. (learn more about [test-fixtures](https://practical.li/clojure/testing/unit-testing/fixtures.html))

  per the [docs](https://clojure.github.io/clojure/clojure.test-api.html), we tell the test-runner to wrap this
  function around each individual test via:

  `(use-fixtures :each db-fixture)`

  ***

  - test-function : the function to run against the nice, clean 'users' table

  > See also:
  >
  > [clojure.test](https://clojure.github.io/clojure/clojure.test-api.html)
  > [kaocha](https://github.com/lambdaisland/kaocha)
  > [practicalli](https://practical.li/clojure/testing/unit-testing/)
  "
  [test-function]
  (if-let [db (jdbc/get-datasource db-spec)]
    (do
      (reset! database db)
      (sut/drop-users-table @database)                      ; just to be sure
      (sut/create-users-table @database)                    ; nice, empty table
      (test-function)                                       ; run the test
      (sut/drop-users-table @database)                      ; cleanup
      (reset! database nil))))


(deftest register
  (is (= gold-register-failed
        (sut/user-registered? @database {:user-id "dummy"})))

  (let [result (sut/register @database "dummy" "pwd")]
    (is (= gold-register-success result))
    (is (= gold-register-success
          (schema/validate sut/RegisterResponse result)))))


(deftest login
  (is (= gold-login-failed
        (sut/login @database "dummy" "pwd")))

  (sut/register @database "dummy" "pwd")
  (let [result (sut/login @database "dummy" "pwd")]
    (is (= gold-login-success result))
    (is (= gold-login-success
          (schema/validate sut/LoginResponse result)))))


(deftest users
  (is (= gold-users-empty
        (sut/users @database)))

  ; register 3 users
  (sut/register @database "dummy" "pwd")
  (sut/register @database "dummy-2" "pwd")
  (sut/register @database "dummy-3" "pwd")

  (let [result (sut/users @database)]
    (is (= gold-users-full result))
    (is (= gold-users-full
          (schema/validate sut/Users result)))))


(deftest user-registered?
  (is (= gold-register-failed
        (sut/user-registered? @database {:user-id "dummy"})))

  (sut/register @database "dummy" "pwd")
  (let [result (sut/user-registered? @database "dummy")]
    (is (= gold-register-success result))
    (is (= gold-register-success
          (schema/validate sut/RegisterResponse result)))))


(use-fixtures :each db-fixture)


; working out the fixture at the repl
(comment
  (reset! database (jdbc/get-datasource db-spec))

  (do
    (sut/drop-users-table @database)
    (sut/create-users-table @database))

  (def result (sut/register @database "dummy" "pwd"))

  (is (= gold-register-success result))
  (is (= gold-register-success
        (schema/validate sut/RegisterResponse
          gold-register-success)))


  (do
    (sut/register @database "dummy" "pwd")
    (sut/register @database "dummy-2" "pwd")
    (sut/register @database "dummy-3" "pwd"))

  (reset! database nil)

  ())