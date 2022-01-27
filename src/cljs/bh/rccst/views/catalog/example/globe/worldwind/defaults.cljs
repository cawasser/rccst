(ns bh.rccst.views.catalog.example.globe.worldwind.defaults
  (:require ["worldwindjs" :as WorldWind]))


(def projection "3D")

(def outline-width 2)
(def fill-color [128 128 128 1.0])
(def interior-color [128 128 128 0.3])
(def outline-color [128 128 128 1.0])

(def text-color (.-WHITE WorldWind/Color))
(def scale 1.5)
(def offset [0 0])

(def altitude 100)

