(ns bh.rccst.ui-component.molecule.composite.coverage-plan.support
  (:require [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.molecule.composite.coverage-plan.support")


(def sensor-color-pallet [[:green "rgba(0, 128, 0, .3)" [0, 128, 0, 0.3] [0.0 0.5 0.0 0.1] "#008000"]
                          [:blue "rgba(0, 0, 255, .3)" [0, 0, 255, 0.3] [0.0 0. 1.0 0.1] "#0000FF"]
                          [:orange "rgba(255, 165, 0, .3)" [255, 165, 0, 0.3] [1.0 0.65 0.0 0.3] "#FFA500"]
                          [:grey "rgba(128, 128, 128, .3)" [128, 128, 128, 0.3] [0.5 0.5 0.5 0.3] "#808080"]
                          [:cornflowerblue "rgba(100, 149, 237, .3)" [100, 149, 237, 0.3] [0.4 0.58 0.93 0.3] "#6495ED"]
                          [:darkcyan "rgba(0, 139, 139, .3)" [0, 139, 139, 0.3] [0.0 0.55 0.55 0.3] "#008B8B"]
                          [:goldenrod "rgba(218, 165, 32, .3)" [218, 165, 32, 0.3] [0.84 0.65 0.13 0.3] "#DAA520"]
                          [:khaki "rgba(240, 230, 140, .3)" [240, 230, 140, 0.3] [0.94 0.90 0.55 0.3] "#F0E68C"]
                          [:deepskyblue "rgba(0, 191, 255, .3)" [0, 191, 255, 0.3] [1.0 0.0 1.0 0.3] "#00BFFF"]
                          [:navy "rgba(0, 0, 128, .3)" [0, 0, 128, 0.3] [0.0 0.0 0.5 0.3] "#000080"]
                          [:darkred "rgba(139, 0, 0, .3)" [139, 0, 0, 0.3] [0.55 0.0 0.0 0.3] "#8B0000"]
                          [:darkseagreen "rgba(143, 188, 143, .3)" [143, 188, 143, 0.3] [0.55 0.74 0.56 0.3] "#8FBC8F"]
                          [:darkviolet "rgba(148, 0, 211, .3)" [148, 0, 211, 0.3] [0.58 0 0.83 0.3] "#9400D3"]
                          [:forestgreen "rgba(34, 139, 34, .3)" [34, 139, 34, 0.3] [1.0 0.71 0.76 0.9] "#228B22"]
                          [:orchid "rgba(218, 112, 214, .3)" [218, 112, 214, 0.3] [0.84 0.44 0.84 0.3] "#DA70D6"]
                          [:plum "rgba(221, 160, 221, .3)" [221, 160, 221, 0.3] [0.87 0.63 0.87 0.9] "#DDA0DD"]
                          [:tomato "rgba(255, 99, 71, .3)" [255, 99, 71, 0.3] [1.0 0.39 0.28 0.3] "#FF6347"]
                          [:orangered "rgba(255, 69, 0, .3)" [255, 69, 0, 0.3] [1.0 0.27 0.0 0.3] "#FF4500"]
                          [:cyan "rgba(0, 255, 255, .3)" [0, 255, 255, 0.3] [0.0 1.0 1.0 0.9] "#00FFFF"]])


; let's change to 60N->5N (65deg), 60->180W (120deg)
;
; LAT: 6.5deg per cell (- 60 (* 6.5 row)) ??
;     center @ -3deg
;
; LON: 12deg per cell (+ 60 (* 12 (- 9 lon))) ??
;     center @ +6deg
;
(defn- get-cell-lat [[row _]]
  ;(* (- 4.5 row) 20.0))
  (- 60 (* 6.5 row)))


(defn- get-cell-lat-center [cell]
  ;(- (get-cell-lat cell) 10.0))
  (- (get-cell-lat cell) 3))


(defn- get-cell-lon [[_ col]]
  ;(* (- 9 col) -20.0))
  (+ -60 (* -12 (- 9 col))))


(defn- get-cell-lon-center [cell]
  ;(+ (get-cell-lon cell) 10.0))
  (+ (get-cell-lon cell) 6.0))


; pre-gen all the cell boundaries as [lat lon] pairs, and group together for
; a complete "polygon":
;
;       (1)          (2)
; (5)  [0 0] -----> [0 1]
;        ^            |
;        |            v
;      [1 0] -----> [1 1]
;       (4)          (3)
;
(def cell-boundaries
  (into (sorted-map-by <)
    (into {}
      (for [row (range 10)
            col (range 10)]
        {[row col] [[(get-cell-lat [row col]) (get-cell-lon [row col])]
                    [(get-cell-lat [row (inc col)]) (get-cell-lon [row (inc col)])]
                    [(get-cell-lat [(inc row) (inc col)]) (get-cell-lon [(inc row) (inc col)])]
                    [(get-cell-lat [(inc row) col]) (get-cell-lon [(inc row) col])]
                    [(get-cell-lat [row col]) (get-cell-lon [row col])]]}))))


(def cell-centers
  (into (sorted-map-by <)
    (into {}
      (for [row (range 10)
            col (range 10)]
        {[row col] [(get-cell-lat-center [row col])
                    (get-cell-lon-center [row col])]}))))


(defn boundary-locations [cell]
  (get cell-boundaries cell))


(defn make-coverage-shape [{:keys [cell coverage time color] :as params}]
  ;(log/info "make-coverage-shape" cell coverage "//" color "//" (keys params))
  (let [[_ _ _ fill _] color
        [r g b a] fill
        outline [r g b (+ a 0.3)]]
    {:shape         :shape/polygon
     :id            (clojure.string/join "-"
                      [(:platform coverage)
                       time
                       (str cell)])
     :locations     (boundary-locations cell)
     :width         2
     :fill-color    fill
     :outline-color outline}))


(defn make-target-shape [[target-id row col ti color]]
  (log/info "make-target-shape" target-id color)
  (let [[_ _ _ c _] color
        [r g b _] c
        fill    [r g b 0.9]
        outline [r g b 1.0]]
    {:shape         :shape/circle
     :id            (clojure.string/join "-"
                      [target-id ti row col])
     :location      (get cell-centers [row col])
     :radius        300000
     :width         2
     :fill-color    fill
     :outline-color outline}))


(defn cook-coverages [satellites coverages current-time]
  ;(log/info "cook-coverages" satellites
  ; "//" coverages
  ; "//" current-time)

  (let [ret (->> coverages
              :data
              (filter #(= (:time %) current-time))
              (mapcat (fn [{:keys [coverage time cell computed_at color] :as all}]
                        (map (fn [c] {:time time :coverage c :cell cell :computed_at computed_at})
                          coverage)))
              ; TODO: need to mix in the correct color form the associated platform/sensor
              (map (fn [cvg]
                     (let [platform  (get-in cvg [:coverage :platform])
                           sensor    (get-in cvg [:coverage :sensor])
                           satellite (first (filter #(and (= platform (:platform_id %))
                                                       (= sensor (:sensor_id %)))
                                              satellites))]
                       (assoc cvg :color (:color satellite))))))]

    ;(log/info "cook-coverages (ret)" ret)
    ret))


(defn cook-targets [targets current-time]
  ;(log/info "cook-targets" targets current-time)
  (let [ret (->> targets
              seq
              (mapcat (fn [{:keys [name cells color]}]
                        (map (fn [[r c ty ti]]
                               [name r c ti color])
                          cells)))
              (filter (fn [[_ _ _ ti _]] (= ti current-time))))]
    ;(log/info "cook-targets (ret)" ret)
    ret))


(comment
  (do
    (def current-time 0)
    (def coverages [{:time        0
                     :cell        [9 7]
                     :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                     :computed_at "2021-08-02T15:16:05.558813"}]))

  (->> coverages
    ;seq
    (filter #(= (:time %) current-time))
    (mapcat (fn [{:keys [coverage time cell computed_at] :as all}]
              (map (fn [c] {:time time :coverage c :cell cell :computed_at computed_at})
                coverage))))


  (do
    (def current-time 0)
    (def coverages [{:time        0
                     :cell        [9 7]
                     :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                     :computed_at "2021-08-02T15:16:05.558813"}]))

  (->> coverages
    seq
    (filter #(= (:time %) current-time))
    (mapcat (fn [{:keys [coverage time cell computed_at] :as all}]
              (map (fn [c] {:time time :coverage c :cell cell :computed_at computed_at})
                coverage))))

  ())


(comment

  (do
    (def sample-shape {:shape      :shape/polygon :id "square"
                       :locations  [[30.0 -130.0] [30.0 -100.0]
                                    [0.0 -100.0] [0.0 -130.0]]
                       :fill-color [1 0 0 0.3] :outline-color [1 0 0 1] :width 2})
    (def coverage {:time        0
                   :cell        [9 7]
                   :coverage    {:platform "goes-west", :sensor "abi-3"}
                   :computed_at "2021-08-02T15:16:05.558813"}))


  (do
    (def id (clojure.string/join "-"
              [(get-in coverage [:coverage :platform])
               (:time coverage)
               (str (:cell coverage))]))
    (def locations (boundary-locations (:cell coverage)))
    (def fill-color (get-in sensor-color-pallet [0 2]))
    (def outline-color (get-in sensor-color-pallet [0 2]))
    (def width 2))

  {:shape      :shape/polygon :id id
   :locations  locations :width width
   :fill-color fill-color :outline-color outline-color}


  (def real-coverage [{:time        0
                       :cell        [9 7]
                       :coverage    #{{:platform "goes-west", :sensor "abi-3"} {:platform "goes-east", :sensor "abi-1"}},
                       :computed_at "2021-08-02T15:16:05.558813"}])

  (defn- make-one-shape [c]
    {:shape         :shape/polygon
     :id            (clojure.string/join "-"
                      [(get-in c [:coverage :platform])
                       (:time c)
                       (str (:cell c))])
     :locations     (boundary-locations (:cell c))
     :width         2
     :fill-color    (get-in sensor-color-pallet [0 2])
     :outline-color (get-in sensor-color-pallet [0 2])})

  (make-one-shape coverage)


  (make-coverage-shape real-coverage)





  ())

