(ns rccst.components.database.create-and-init
  (:require [hugsql.adapter.next-jdbc :as next-adapter]
            [hugsql.core :as hugsql]

            [rccst.components.database.users :as users]))



; Instantiate hugsql functions, with an adapter for next-jdbc
(hugsql/def-db-fns "sql/rccst.sql"
  {:adapter (next-adapter/hugsql-adapter-next-jdbc)})


(defn create-database
  "Creates the database and all the tables."
  [db]
  (-> db
    (create-database! db)
    users/create-users-table))


(defn delete-database
  "delete all the tables and the database itself."
  [db]
  (-> db
    users/drop-users-table))


(defn init-database
  "loading all the starting data"
  [db]
  (-> db
    users/init-users-table))


(defn create-and-init
  "create the database and all the tables, then load all the starting data"
  [db]
  (create-database db)
  (init-database db))


(defn rebuild-database
  "delete and re-create the database and all the tables"
  [db]
  (delete-database db)
  (create-and-init db))