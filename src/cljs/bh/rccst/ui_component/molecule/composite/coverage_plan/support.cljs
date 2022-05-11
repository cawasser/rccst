(ns bh.rccst.ui-component.molecule.composite.coverage-plan.support)



(def sensor-color-pallet [[:green "rgba(0, 128, 0, .3)" [0.0 0.5 0.0 0.1]] ; "abi-3"
                          [:blue "rgba(0, 0, 255, .3)" [0.0 0. 1.0 0.1]] ; "abi-1"
                          [:orange "rgba(255, 165, 0, .3)" [1.0 0.65 0.0 0.3]] ; "avhhr-6"
                          [:grey "rgba(128, 128, 128, .3)" [0.5 0.5 0.5 0.3]] ; "viirs-5"
                          [:cornflowerblue "rgba(100, 149, 237, .3)" [0.4 0.58 0.93 0.3]] ; "abi-meso-11"
                          [:darkcyan "rgba(0, 139, 139, .3)" [0.0 0.55 0.55 0.3]] ; "abi-meso-4"
                          [:goldenrod "rgba(218, 165, 32, .3)" [0.84 0.65 0.13 0.3]] ; "abi-meso-10"
                          [:khaki "rgba(240, 230, 140, .3)" [0.94 0.90 0.55 0.3]] ; "abi-meso-2"
                          [:deepskyblue "rgba(0, 191, 255, .3)" [1.0 0.0 1.0 0.3]]
                          [:navy "rgba(0, 0, 128, .3)" [0.0 0.0 0.5 0.3]]
                          [:cyan "rgba(0, 255, 255, .3)" [0.0 1.0 1.0 0.9]]
                          [:darkred "rgba(139, 0, 0, .3)" [0.55 0.0 0.0 0.3]]
                          [:darkseagreen "rgba(143, 188, 143, .3)" [0.55 0.74 0.56 0.3]]
                          [:darkviolet "rgba(148, 0, 211, .3)" [0.58 0 0.83 0.3]]
                          [:forestgreen "rgba(34, 139, 34, .3)" [1.0 0.71 0.76 0.9]]
                          [:orchid "rgba(218, 112, 214, .3)" [0.84 0.44 0.84 0.3]]
                          [:plum "rgba(221, 160, 221, .3)" [0.87 0.63 0.87 0.9]]
                          [:tomato "rgba(255, 99, 71, .3)" [1.0 0.39 0.28 0.3]]
                          [:orangered "rgba(255, 69, 0, .3)" [1.0 0.27 0.0 0.3]]])

(def dummy-sensor-color-pallet
  {"abi-3"       [:green "rgba(0, 128, 0, .3)" [0.0 0.5 0.0 0.1]] ; "abi-3"
   "abi-1"       [:blue "rgba(0, 0, 255, .3)" [0.0 0. 1.0 0.1]] ; "abi-1"
   "avhhr-6"     [:orange "rgba(255, 165, 0, .3)" [1.0 0.65 0.0 0.3]] ; "avhhr-6"
   "viirs-5"     [:grey "rgba(128, 128, 128, .3)" [0.5 0.5 0.5 0.3]] ; "viirs-5"
   "abi-meso-11" [:cornflowerblue "rgba(100, 149, 237, .3)" [0.4 0.58 0.93 0.3]] ; "abi-meso-11"
   "abi-meso-4"  [:darkcyan "rgba(0, 139, 139, .3)" [0.0 0.55 0.55 0.3]] ; "abi-meso-4"
   "abi-meso-10" [:goldenrod "rgba(218, 165, 32, .3)" [0.84 0.65 0.13 0.3]] ; "abi-meso-10"
   "abi-meso-2"  [:khaki "rgba(240, 230, 140, .3)" [0.94 0.90 0.55 0.3]]}) ; "abi-meso-2"


(defn- get-sensor-colors [sensors]
  (if (seq sensors)
    (zipmap sensors (cycle sensor-color-pallet))
    []))


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


(defn- make-one-shape [c]
  (let [fill    (get-in dummy-sensor-color-pallet [(get-in c [:coverage :sensor]) 2])
        [r g b a] fill
        outline [r g b (+ a 0.3)]]
    {:shape         :shape/polygon
     :id            (clojure.string/join "-"
                      [(get-in c [:coverage :platform])
                       (:time c)
                       (str (:cell c))])
     :locations     (boundary-locations (:cell c))
     :width         2
     :fill-color    fill
     :outline-color outline}))


(defn make-shape [c]
  (mapcat (fn [{:keys [coverage time cell computed_at] :as all}]
            (map #(make-one-shape {:time time :coverage % :cell cell :computed_at computed_at})
              coverage))
    c))


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
    ; TODO: compute the colors correctly across all the sensors/platform combinations so they
    ; are consistent
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


  (make-shape real-coverage)





  ())

