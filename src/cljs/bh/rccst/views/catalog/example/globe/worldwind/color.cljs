(ns bh.rccst.views.catalog.example.globe.worldwind.color
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))

; colors
(defn yellow [alpha] [128 128 0 alpha])
(defn white [alpha] [255 255 255 alpha])
(defn blue [alpha] [0 0 255 alpha])
(defn red [alpha] [255 0 0 alpha])
(defn green [alpha] [0 255 0 alpha])
(defn black [alpha] [0 0 0 alpha])



(defn color
  ([r g b a]
   (WorldWind/Color. r g b a))

  ([[r g b a]]
   (WorldWind/Color. r g b a)))
