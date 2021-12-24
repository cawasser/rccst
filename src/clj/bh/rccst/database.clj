(ns bh.rccst.database
  (:require [clojure.tools.logging :as log]))


(def app-db (atom {}))
(def update-reg (atom {}))
(def query-reg (atom {}))


(defn reg-update-db [evt e-fn]
  (swap! update-reg assoc evt e-fn))


(defn publish! [[evt & args :as message]])



(defn do-update [[evt & _ :as message]]
  (let [e-fn (get @update-reg evt)]
    (log/info "do-update" evt "////" message "////" e-fn)
    (if e-fn
      (do
        (swap! app-db e-fn message)
        ; publish! to any subscribers
        (publish! message)))))





; let's try out reg-event-db
(comment
  (reg-update-db
    :dummy
    (fn [db [evt]]
      (log/info ":dummy-update" evt "////" db)
      (assoc db :dummy "dummy")))

  (do-update [:dummy])


  (reg-update-db
    :something-new
    (fn [db [_ a-value]]
      (assoc db :something-new a-value)))
  (do-update [:something-new "a new value"])
  (do-update [:something-new "another value"])


  ())