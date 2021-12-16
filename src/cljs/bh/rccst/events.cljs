(ns bh.rccst.events
  (:require
   [re-frame.core :as re-frame]
   [bh.rccst.db :as db]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))
