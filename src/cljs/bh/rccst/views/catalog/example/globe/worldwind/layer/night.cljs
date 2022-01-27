(ns bh.rccst.views.catalog.example.globe.worldwind.layer.night
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn night [layer-name]
  (let [layer (WorldWind/AtmosphereLayer.)]
    (set! (.-displayName layer) layer-name)
    (set! (.-opacity layer) 0.9)
    (set! (.-nightEnabled layer) true)
    layer))
