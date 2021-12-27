(ns bh.rccst.events
  (:require [taoensso.timbre :as log]
            [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]

            [bh.rccst.db :as db]
            [bh.rccst.csrf :refer [?csrf-token]]))


(def default-header {:timeout         8000
                     :format          (ajax/transit-request-format)
                     :response-format (ajax/transit-response-format)
                     :headers         {"x-csrf-token" ?csrf-token}})


(re-frame/reg-event-fx
  ::initialize-db
  (fn-traced [_ _]
    {:dispatch [::get-version]
     :db db/default-db}))


(re-frame/reg-event-fx
  ::get-version
  (fn-traced [_ _]
    (log/info "::get-version")
    {:http-xhrio (merge default-header
                   {:method     :get
                    :uri        "/version"
                    :on-success [::version-success]
                    :on-failure [::bad-lookup-result]})}))


(re-frame/reg-event-db
  ::version-success
  (fn-traced [db [_ {:keys [version]}]]
    (log/info "::version-success" version)
    (assoc db :version version)))


(re-frame/reg-event-db
  ::version-failure
  (fn-traced [db [_ _]]
    (assoc db :version "unknown")))


(re-frame/reg-event-db
  ::name
  (fn-traced [db [_ name]]
    (assoc db :name name)))


(re-frame/reg-event-db
  ::update-counter
  (fn-traced [db [_ content]]
    (log/info "::update-counter" (:i content))
    (assoc db :counter (:i content)
      :set (:last-3 content))))


(re-frame/reg-event-db
  ::data-update
  (fn-traced [db [_ {:keys [id value]}]]
    (log/info "::data-update" id value)
    (assoc-in db [:sources id] value)))


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
    {:http-xhrio (merge default-header
                   {:method     :get
                    :uri        "/lookup"
                    :on-success [::good-lookup-result]
                    :on-failure [::bad-lookup-result]})}))


(re-frame/reg-event-db
  ::good-subscribe-result
  (fn-traced [db [_ source result]]
    (let [current (:subscribed db)]
      (log/info ":good-subscribe-result" source result
        "////" current)
      (assoc db
        :subscribed (set (conj current source))
        :subscribe-error ""))))


(re-frame/reg-event-db
  ::bad-subscribe-result
  (fn-traced [db [_ source result]]
    (log/info "::bad-subscribe-result" source result)
    (assoc db
      :subscribe-error result)))

(re-frame/reg-event-fx
  ::subscribe-to
  (fn-traced [_ [_ source]]
    (log/info "::subscribe-to" source
      "////" {:user-id "client" :data-sources source}
      "////" ?csrf-token)
    {:http-xhrio (merge default-header
                   {:method          :post
                    :uri             "/subscribe/data-source"
                    :params          {:user-id "client" :data-sources source}
                    :on-success      [::good-subscribe-result source]
                    :on-failure      [::bad-subscribe-result source]})}))


; some events to dispatch from the REPL
(comment
  (re-frame/dispatch [::name "Black Hammer"])
  (re-frame/dispatch [::initialize-db])

  (re-frame/dispatch [::get-version])

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


  (re-frame/dispatch [::subscribe-to :dummy])

  ())