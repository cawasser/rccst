(ns bh.rccst.ui-component.atom.globe.cell.layer-support
  (:require [taoensso.timbre :as log]

            [re-frame.core :as re-frame]
            [re-frame.db :as rdb]
            [cljs-time.core :as cljs-time]
            [cljs-time.coerce :as coerce]

            [bh.rccst.ui-component.atom.globe.subs :as subs]

            [bh.rccst.ui-component.atom.globe.cell.util :as cell]
            [bh.rccst.ui-component.atom.globe.worldwind.location :as location]
            [bh.rccst.ui-component.atom.globe.worldwind.position :as position]
            [bh.rccst.ui-component.atom.globe.worldwind.sector :as sector]
            [bh.rccst.ui-component.atom.globe.worldwind.geographic-text :as geo-text]
            [bh.rccst.ui-component.atom.globe.worldwind.text-attributes :as text-attr]
            [bh.rccst.ui-component.atom.globe.worldwind.layer.renderable :as rl]

            [bh.rccst.ui-component.atom.globe.worldwind.surface.polygon :as poly]
            [bh.rccst.ui-component.atom.globe.worldwind.surface.image :as image]
            [bh.rccst.ui-component.atom.globe.worldwind.surface.circle :as circle]))


(def sensor-color-pallet [[:green "rgba(0, 128, 0, .3)" [0.0 0.5 0.0 0.3]] ; "abi-3"
                          [:blue "rgba(0, 0, 255, .3)" [0.0 0. 1.0 0.3]] ; "abi-1"
                          [:orange "rgba(255, 165, 0, .3)" [1.0 0.65 0.0 0.3]] ; "avhhr-6"
                          [:grey "rgba(128, 128, 128, .3)" [0.5 0.5 0.5 0.3]] ; "viirs-5"
                          [:cornflowerblue "rgba(100, 149, 237, .3)" [0.4 0.58 0.93 0.3]] ; "abi-meso-11"
                          [:darkcyan "rgba(0, 139, 139, .3)" [1.0 0.55 0.55 0.3]] ; "abi-meso-4"
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


(def starting-date-time (cljs-time/date-time 2022 1 15 12 0 0 0))


(defn get-sensor-colors [sensors]
  (if (seq sensors)
    (zipmap sensors (cycle sensor-color-pallet))
    []))


(defn convert-cell-to-sector [cell]
  (let [[lat lon] (get cell/cell-centers cell)]
    [(- lat 2) (+ lat 2) (- lon 4) (+ lon 4)]))


(defn make-sector [cell]
  (let [[minLat maxLat minLon maxLon] (convert-cell-to-sector cell)]
    (sector/sector minLat maxLat minLon maxLon)))


(defn- make-polygon [colors cell]
  (let [[pos sensor] (first cell)
        cell-text (str pos)
        boundaries (cell/cell-boundaries pos)
        locations  (->> boundaries
                     (map (fn [location]
                            (location/location location)))
                     (into-array))
        center (position/position (get cell/cell-centers pos))
        [color-name _ ww-color] (get colors sensor)
        layer-name (str sensor "-" cell-text)]

    ;(log/info "make-polygon" pos sensor color-name ww-color)
    {layer-name
     (rl/renderable-layer layer-name
       [(poly/polygon locations {:color ww-color})])}))
        ;(geo-text/geographic-text center layer-name
        ;  (text-attr/text-attributes))])}))


(defn- make-image [[aoi time-t pos symbol]]
  (let [center (position/position (get cell/cell-centers pos))
        boundaries (cell/cell-boundaries pos)
        locations  (->> boundaries
                     (map (fn [location]
                            (location/location location)))
                     (into-array))
        sector (make-sector pos)
        layer-name (str symbol "-" pos)]

    ;(log/info "make-image" time-t pos aoi symbol)

    {layer-name
     (rl/renderable-layer layer-name
       [
        ;(poly/polygon locations {:color [128 128 128 0.5]})])}))
        (circle/circle center 300000 {:color [128 128 128 0.5]})
        ;(image/image sector symbol)])}))
        (geo-text/geographic-text center (str aoi) (text-attr/text-attributes))])}))


(re-frame/reg-sub
  ::subs/sensor-layers

  (fn [[_ id _] _]
    (re-frame/subscribe [::subs/current-sensor-cells id]))

  (fn [cells [_ id colors]]
    (->> cells
      (map #(make-polygon colors %))
      (into {}))))


(re-frame/reg-sub
  ::subs/aoi-layers

  (fn [[_ id _] _]
    (re-frame/subscribe [::subs/current-aoi-cells id]))

  (fn [cells [_ id]]
    (->> cells
      (map #(make-image %))
      (into {}))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; RICH COMMENTS
;

; work out generating layers from subscription/signal graph
(comment
  (def children [(globe-cljsglobe.worldwind.surface.polygon/polygon [0 0] {:color [255 0 0 1]})
                 (globe-cljsglobe.worldwind.surface.polygon/polygon [0 1] {:color [0 255 0 1]})
                 (globe-cljsglobe.worldwind.surface.polygon/polygon [1 0] {:color [0 0 255 1]})])

  @(re-frame/subscribe [::subs/current-cells])
  @(re-frame/subscribe [::subs/current-cells])
  @(re-frame/subscribe [::subs/sensor-layers])


  (def cell @(re-frame/subscribe [::subs/current-cells]))

  (merge {(str cell)
          (rl/createLayer (str cell)
            [(globe-cljsglobe.worldwind.surface.polygon/polygon cell {:color [128 128 0 0.3]})])}
    (:layers @re-frame.db/app-db))

  ())


; what are we getting from
(comment
  (def id "globe-1")
  @(re-frame/subscribe [::subs/current-aoi-cells id])

  ())
