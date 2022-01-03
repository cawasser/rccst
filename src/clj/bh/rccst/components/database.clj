(ns bh.rccst.components.database
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]))


;;;;;;;;;;;;;;;;;
;; HELPFUL LINKS:
;
; https://www.hugsql.org
;
; https://github.com/seancorfield/usermanager-example
;
;;;;;;;;;;;;;;;;;



(defrecord Database [db-spec database]
  component/Lifecycle
  (start [this]
    (log/info "starting Database" db-spec)
    (if database
      ; already initialized
      this

      ; start things up
      (do
        (assoc this :database (jdbc/get-datasource db-spec)))))

  (stop [this]
    (assoc this :database nil)))


(defn new-database
  [db-spec]
  (map->Database {:db-spec db-spec}))




; fooling around at the repl
(comment
  (def db-spec
    "postgresql database connection spec."
    {:dbtype   "postgresql"
     :dbname   "rccst"
     :user     "postgres"
     :password "Password"
     :host     "localhost"
     :port     "5432"})


  (def db-spec
    "sqlite database connection spec."
    {:dbtype   "sqlite"
     :dbname   "rccst"})

  ())