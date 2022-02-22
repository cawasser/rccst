(ns bh.rccst.views.catalog.example.globe.subs
  (:require
    [re-frame.core :as re-frame]
    [re-frame.db :as rdb]
    [taoensso.timbre :as log]

    [bh.rccst.views.catalog.example.globe.worldwind.defaults :as defaults]))


(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))


(re-frame/reg-sub
  ::base-layers
  (fn [db [_ id]]
    (or (get-in db [:widgets id :base-layers]) [])))


(re-frame/reg-sub
  ::projection
  (fn [db [_ id]]
    (or (get-in db [:widgets id :projection]) defaults/projection)))



(re-frame/reg-sub
  ::time
  (fn [db [_ id]]
    (or (get-in db [:widgets id :time]) 0)))
