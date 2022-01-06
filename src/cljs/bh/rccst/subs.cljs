(ns bh.rccst.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))


(re-frame/reg-sub
  ::logged-in?
  (fn [db]
    (:logged-in? db)))


(re-frame/reg-sub
  ::version
  (fn [db]
    (:version db)))


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


(re-frame/reg-sub
  ::subscribed
  (fn [db [_ source]]
    (contains? (:subscribed db) source)))


(re-frame/reg-sub
  ::subscribe-error
  (fn [db _]
    (:subscribe-error db)))
