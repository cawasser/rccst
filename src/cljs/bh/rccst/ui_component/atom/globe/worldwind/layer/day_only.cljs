(ns bh.rccst.ui-component.atom.globe.worldwind.layer.day-only
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn day-only [layer-name]
  (let [layer (WorldWind/AtmosphereLayer.)]
    (set! (.-displayName layer) layer-name)
    (set! (.-opacity layer) 0.7)
    (set! (.-nightEnabled layer) false)
    layer))
