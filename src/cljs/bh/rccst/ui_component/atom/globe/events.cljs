(ns bh.rccst.ui-component.atom.globe.events
  (:require
   [re-frame.core :as re-frame]
   [re-frame.db]
   [bh.rccst.ui-component.atom.globe.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [taoensso.timbre :as log]))



(re-frame/reg-event-db
  ::init-widget
  (fn-traced [db [_ id min-max]]
    (if-let [_ (get-in db [:widgets id])]
      db ; do nothing - we are already initialized
      (do
        (log/info "init-widget" id min-max)
        (assoc-in db [:widgets id] (db/globe-config id min-max))))))


(re-frame/reg-event-db
  ::update-time
  (fn-traced [db [_ id new-time]]
    ;(log/info "update-time" id new-time)
    (assoc-in db [:widgets id :time] new-time)))


(re-frame/reg-event-db
  ::remove-layer
  (fn-traced [db [_ id layer-name]]
    (update-in db [:widgets id :layers] dissoc layer-name)))


(re-frame/reg-event-db
  ::set-projection
  (fn-traced [db [_ id new-projection]]
    ;(log/info "set-projection" id new-projection)
    (assoc-in db [:widgets id :projection] new-projection)))


(re-frame/reg-event-db
  ::add-base-layer
  (fn-traced [db [_ id layer-name layer]]
    (update-in db [:widgets id :base-layers] merge {layer-name layer})))


(re-frame/reg-event-db
  ::remove-base-layer
  (fn-traced [db [_ id layer-name]]
    (update-in db [:widgets id :base-layers] dissoc layer-name)))


(re-frame/reg-event-db
  ::add-layer
  (fn-traced [db [_ id layer-name layer]]
    (update-in db [:widgets id :layers] merge {layer-name layer})))


(re-frame/reg-event-db
  ::remove-layer
  (fn-traced [db [_ id layer-name]]
    (update-in db [:widgets id :layers] dissoc layer-name)))


(re-frame/reg-event-db
  ::set-layers
  (fn-traced [db [_ id layer-name layer]]
    (-> db
      (update-in [:widgets id :layers] dissoc layer-name) ; remove all existing layers
      (assoc-in [:widgets id :layers] {layer-name layer}))))



; removing an element from a vector with a given "map key"
(comment

  (def current {:weather-flow-elements [{:id :a} {:id :b} {:id :c}]
                :dummy "this should stay"})
  (def element-id :b)

  (update current :weather-flow-elements remove (fn [x] (= (:id x) element-id)))

  (assoc current :weather-flow-elements (remove (fn [x]
                                                  (= (:id x) element-id))
                                          (:weather-flow-elements current)))

  ())

