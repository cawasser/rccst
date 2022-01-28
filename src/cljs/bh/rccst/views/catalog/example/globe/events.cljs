(ns bh.rccst.views.catalog.example.globe.events
  (:require
   [re-frame.core :as re-frame]
   [re-frame.db]
   [bh.rccst.views.catalog.example.globe.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [taoensso.timbre :as log]))


(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   (log/info ":initialize-db")
   db/default-db))


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


(re-frame/reg-event-db
  ::toggle-sensor

  (fn-traced [db [_ globe-id selection]]
    (let [current-set (get-in db [:widgets globe-id :selected-sensors])
          the-fn (if (contains? current-set selection) disj conj)]
      (update-in db [:widgets globe-id :selected-sensors] the-fn selection))))


(re-frame/reg-event-db
  ::toggle-aoi

  (fn-traced [db [_ globe-id selection]]
    (let [current-set (get-in db [:widgets globe-id :selected-aois])
          the-fn (if (contains? current-set selection) disj conj)]
      (update-in db [:widgets globe-id :selected-aois] the-fn selection))))


(re-frame/reg-event-db
  ::add-weather-element

  (fn-traced [db [_ new-element]]
    (log/info "::add-weather-element" new-element)
    (update db :weather-flow-elements conj new-element)))


(re-frame/reg-event-db
  ::remove-weather-element

  (fn-traced [db [_ element-id]]
    (let [current (:weather-flow-elements db)]
      (assoc db :weather-flow-elements (remove (fn [x]
                                                 (= (:id x) element-id))
                                         current)))))


(re-frame/reg-event-db
  ::update-weather-element

  (fn-traced [db [_ {:keys [id] :as element}]]
    (let [current (:weather-flow-elements db)]
      (-> db
        (assoc :weather-flow-elements (remove (fn [x]
                                                (= (:id x) id))
                                        current))
        (update :weather-flow-elements conj element)))))



(re-frame/reg-event-db
  ::add-system-element

  (fn-traced [db [_ new-element]]
    (log/info "::add-system-element" new-element)
    (update db :system-flow-elements conj new-element)))


(re-frame/reg-event-db
  ::remove-system-element

  (fn-traced [db [_ element-id]]
    (let [current (:system-flow-elements db)]
      (assoc db :system-flow-elements (remove (fn [x]
                                                (= (:id x) element-id))
                                        current)))))


(re-frame/reg-event-db
  ::update-system-element

  (fn-traced [db [_ {:keys [id] :as element}]]
    (let [current (:system-flow-elements db)]
      (-> db
        (assoc :system-flow-elements (remove (fn [x]
                                               (= (:id x) id))
                                       current))
        (update :system-flow-elements conj element)))))


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

