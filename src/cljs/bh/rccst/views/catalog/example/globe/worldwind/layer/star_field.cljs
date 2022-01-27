(ns bh.rccst.views.catalog.example.globe.worldwind.layer.star-field
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn star-field [layer-name]
  (let [layer (WorldWind/StarFieldLayer.)]
    (set! (.-displayName layer) layer-name)
    layer))

