(ns rccst.subs
  (:require
   [re-frame.core :as re-frame]))


(re-frame/reg-sub
  ::logged-in?
  (fn [db]
    (:logged-in? db)))


(re-frame/reg-sub
  ::version
  (fn [db]
    (:version db)))


(re-frame/reg-sub
  ::uuid
  (fn [db]
    (:uuid db)))

