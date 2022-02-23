(ns bh.rccst.ui-component.atom.globe.worldwind.layer.coordinates
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn coordinates [this layer-name]
  (let [layer (WorldWind/CoordinatesDisplayLayer. (.-wwd this))]
    (set! (.-displayName layer) layer-name)
    layer))