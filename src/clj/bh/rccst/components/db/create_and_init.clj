(ns bh.rccst.components.db.create-and-init
  (:require [hugsql.adapter.next-jdbc :as next-adapter]
            [hugsql.core :as hugsql]

            [bh.rccst.components.db.db :as db]
            [bh.rccst.components.db.users :as users]))



; Instantiate hugsql functions, with an adapter for next-jdbc
(hugsql/def-db-fns "sql/rccst.sql"
  {:adapter (next-adapter/hugsql-adapter-next-jdbc)})



(defn create-database [db]
  (-> db
    (create-database! db)
    users/create-users-table))


(defn delete-database [db]
  (-> db
    users/drop-users-table))


(defn init-database [db]
  (-> db))


(defn create-and-init [db]
  (create-database db)
  (init-database db))


(defn rebuild-database [db]
  (delete-database db)
  (create-and-init db))