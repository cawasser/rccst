(ns bh.rccst.events
  (:require [taoensso.timbre :as log]
            [re-frame.core :as re-frame]
            [bh.rccst.db :as db]))


(re-frame/reg-event-db
  ::initialize-db
  (fn [_ _]
    db/default-db))


(re-frame/reg-event-db
  ::name
  (fn [db [_ name]]
    (assoc db :name name)))


(re-frame/reg-event-db
  ::add-to-set
  (fn [db [_ new-value]]
    (log/info "add-to-set" (:set db) "<-" new-value)
    (update db :set conj new-value)))


(re-frame/reg-event-db
  ::remove-from-set
  (fn [db [_ new-value]]
    (log/info "remove-from-set" (:set db) "<-" new-value)
    (update db :set disj new-value)))


(re-frame/reg-event-db
  ::union-set
  (fn [db [_ new-values]]
    (log/info "union-set" (:set db) "<-" new-values)
    (update db :set clojure.set/union new-values)))


(re-frame/reg-event-db
  ::diff-set
  (fn [db [_ new-values]]
    (log/info "diff-set" (:set db) "<-" new-values)
    (update db :set clojure.set/difference new-values)))



; some events to dispatch from the REPL
(comment
  (re-frame/dispatch [::name "Black Hammer"])
  (re-frame/dispatch [::initialize-db])

  @re-frame.db/app-db

  (def db {:set #{0 1 2}})
  (update db :set conj 7)

  (re-frame/dispatch [::add-to-set 15])
  (re-frame/dispatch [::union-set #{25 35 45}])

  ; this one doesn't add anything as they are duplicates
  (re-frame/dispatch [::union-set #{0 35}])

  ; try removing some items that exist
  (re-frame/dispatch [::remove-from-set 1])

  ; try removing some items that exist
  (re-frame/dispatch [::diff-set #{0 35}])

  ; and these also do nothing since they aren't in the set anyway
  (re-frame/dispatch [::diff-set #{9 200}])

  ())