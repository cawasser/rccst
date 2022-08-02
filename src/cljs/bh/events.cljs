(ns bh.events
  (:require [taoensso.timbre :as log]
            [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [day8.re-frame.http-fx]))


(re-frame/reg-event-db
  ::init-locals
  (fn-traced [db [_ container init-vals]]
    ;(log/info "::init-locals" container init-vals)
    (if (get db container)
      (do
        ;(log/info "::init-locals // already exists")
        db)
      (do
        ;(log/info "::init-locals // adding")
        (assoc db container init-vals)))))


