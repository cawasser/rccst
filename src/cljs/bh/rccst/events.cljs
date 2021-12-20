(ns bh.rccst.events
  (:require [taoensso.timbre :as log]
            [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [bh.rccst.db :as db]))


(re-frame/reg-event-db
  ::initialize-db
  (fn-traced [_ _]
    db/default-db))


(re-frame/reg-event-db
  ::name
  (fn-traced [db [_ name]]
    (assoc db :name name)))


(re-frame/reg-event-db
  ::server-update
  (fn-traced [db [_ [event-type data :as ?data]]]
    ;(log/info "::server-update" event-type (:i data))
    (assoc db :counter (:i data)
      :set (:last-3 data))))


(re-frame/reg-event-db
  ::add-to-set
  (fn-traced [db [_ new-value]]
    (log/info "add-to-set" (:set db) "<-" new-value)
    (update db :set conj new-value)))


(re-frame/reg-event-db
  ::remove-from-set
  (fn-traced [db [_ new-value]]
    (log/info "remove-from-set" (:set db) "<-" new-value)
    (update db :set disj new-value)))


(re-frame/reg-event-db
  ::union-set
  (fn-traced [db [_ new-values]]
    (log/info "union-set" (:set db) "<-" new-values)
    (update db :set clojure.set/union new-values)))


(re-frame/reg-event-db
  ::diff-set
  (fn-traced [db [_ new-values]]
    (log/info "diff-set" (:set db) "<-" new-values)
    (update db :set clojure.set/difference new-values)))


(re-frame/reg-event-db
  ::good-lookup-result
  (fn-traced [db [_ result]]
    (assoc db
      :lookup result
      :lookup-error "")))


(re-frame/reg-event-db
  ::bad-lookup-result
  (fn-traced [db [_ result]]
    (assoc db
      :lookup ""
      :lookup-error result)))


(re-frame/reg-event-fx
  ::lookup
  (fn-traced [_ _]
    (log/info "::lookup")
    {:http-xhrio {:method :get
                  :uri             "/lookup"
                  :timeout         8000                                       ;; optional see API docs
                  :response-format (ajax/transit-request-format)  ;; IMPORTANT!: You must provide this.
                  :on-success      [::good-lookup-result]
                  :on-failure      [::bad-lookup-result]}}))


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