(ns bh.rccst.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))


(re-frame/reg-sub
  ::counter
  (fn [db]
    (:counter db)))


(re-frame/reg-sub
  ::set
  (fn [db]
    (:set db)))


(re-frame/reg-sub
  ::lookup
  (fn [db]
    (:lookup db)))


(re-frame/reg-sub
  ::lookup-error
  (fn [db]
    (:lookup-error db)))


(re-frame/reg-sub
  ::source
  (fn [db [_ id]]
    (get-in db [:sources id])))
