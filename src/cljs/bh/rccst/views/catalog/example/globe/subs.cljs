(ns bh.rccst.views.catalog.example.globe.subs
  (:require
    [re-frame.core :as re-frame]
    [re-frame.db :as rdb]
    [taoensso.timbre :as log]

    [bh.rccst.views.catalog.example.globe.worldwind.defaults :as defaults]))


(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))


(re-frame/reg-sub
  ::base-layers
  (fn [db [_ id]]
    (or (get-in db [:widgets id :base-layers]) [])))


(re-frame/reg-sub
  ::projection
  (fn [db [_ id]]
    (or (get-in db [:widgets id :projection]) defaults/projection)))


(re-frame/reg-sub
  ::selected-sensors
  (fn [db [_ id]]
    (or (get-in db [:widgets id :selected-sensors]) #{})))


(re-frame/reg-sub
  ::selected-aois
  (fn [db [_ id]]
    (or (get-in db [:widgets id :selected-aois]) #{})))


(re-frame/reg-sub
  ::current-sensor-cells

  (fn [[_ id] _]
    [(re-frame/subscribe [::time id])
     (re-frame/subscribe [::selected-sensors id])
     (re-frame/subscribe [::sensor-allocations])])

  (fn [[time selected-sensors sensor-allocations] [_ id]]
    (if-let [cells (->> sensor-allocations
                     (filter #(= time (:time %)))
                     (map (juxt :cell :coverage))
                     (mapcat (fn [[cell coverages]]
                               (for [c coverages]
                                 {cell (:sensor c)})))
                     (filter #(contains? selected-sensors (first (vals %)))))]
      (do
        ;(log/info "::current-sensor-cells" cells)
        cells)
      (do
        ;(log/info "::current-sensor-cells returning []")
        []))))


(re-frame/reg-sub
  ::current-aoi-cells

  (fn [[_ id] _]
    [(re-frame/subscribe [::time id])
     (re-frame/subscribe [::selected-aois id])
     (re-frame/subscribe [::aois])])

  (fn [[time-t selected-aois aois] [_ id]]
    (if-let [cells (->> aois
                     (filter (fn [[k v]] (contains? selected-aois k)))
                     (map (fn [[k v]]
                            [k ((juxt :cells :symbol) v)]))
                     (map (fn [[aoi [cells [_ symbol]]]]
                            [aoi cells symbol]))
                     (mapcat (fn [[aoi cells symbol]]
                               (map (fn [[row col _ t]]
                                      [aoi t [row col] symbol])
                                 cells)))
                     (filter (fn [[_ t _ _]] (= time-t t))))]
      (do
        ;(log/info "::current-aoi-cells" cells)
        cells)
      (do
        ;(log/info "::current-aoi-cells returning []")
        []))))


(re-frame/reg-sub
  ::time
  (fn [db [_ id]]
    (or (get-in db [:widgets id :time]) 0)))


(re-frame/reg-sub
  ::sensor-types
  (fn [db _]
    (->> db
      :sensor-allocations
      (map :coverage)
      (mapcat #(map :sensor %))
      set)))


(re-frame/reg-sub
  ::sensor-allocations
  (fn [db _]
    (or (:sensor-allocations db) [])))


(re-frame/reg-sub
  ::aois
  (fn [db _]
    (or (:aois db) [])))


(re-frame/reg-sub
  ::weather-flow-elements
  (fn [db _]
    (or (:weather-flow-elements db) [])))


(re-frame/reg-sub
  ::system-flow-elements
  (fn [db _]
    (or (:system-flow-elements db) [])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; RICH COMMENTS
;


; work out filtering for the selected aois, so we can generate cells form them
(comment
  ; goal is to produce:

  [[0 0 "/image/some-symbol.png"]
   [4 5 "/image/some-symbol.png"]
   [2 5 "/image/some-symbol.png"]]

  (do
    (def id "globe-1")
    (def time-t @(re-frame/subscribe [::time id]))
    (def selected-aois @(re-frame/subscribe [::selected-aois id]))
    (def aois @(re-frame/subscribe [::aois])))



  (->> aois
    (filter (fn [[k v]] (contains? selected-aois k)))
    (map (fn [[k v]]
           [k ((juxt :cells :symbol) v)])))

  ;    produces:
  ; first  partial transformation
  (def p-1 (->> aois
             (filter (fn [[k v]] (contains? selected-aois k)))
             (map (fn [[k v]]
                    [k ((juxt :cells :symbol) v)]))))


  (map (fn [[aoi [cells [_ symbol]]]]
         [aoi cells symbol])
    p-1)

  ;    produces:
  ; next partial transformation
  (def p-2 (map (fn [[aoi [cells [_ symbol]]]]
                  [aoi cells symbol])
             p-1))

  (map (fn [[aoi cells symbol]]
         (map (fn [[row col _ t]]
                [aoi t row col symbol])
           cells))
    p-2)

  ; combine the partials:
  (def p-3
    (->> aois
      (filter (fn [[k v]] (contains? selected-aois k)))
      (map (fn [[k v]]
             [k ((juxt :cells :symbol) v)]))
      (map (fn [[aoi [cells [_ symbol]]]]
             [aoi cells symbol]))
      (mapcat (fn [[aoi cells symbol]]
                (map (fn [[row col _ t]]
                       [aoi t [row col] symbol])
                  cells)))))


  ; now we just need to filter out the correct time-t's



  (filter (fn [[_ t _ _]] (= time-t t))
    p-3)

  ; ok, can we pull it all together?
  (def selected-aois #{"alpha-hd"})
  (->> aois
    (filter (fn [[k v]] (contains? selected-aois k)))
    (map (fn [[k v]]
           [k ((juxt :cells :symbol) v)]))
    (map (fn [[aoi [cells [_ symbol]]]]
           [aoi cells symbol]))
    (mapcat (fn [[aoi cells symbol]]
              (map (fn [[row col _ t]]
                     [aoi t [row col] symbol])
                cells)))
    (filter (fn [[_ t _ _]] (= time-t t))))

  ())