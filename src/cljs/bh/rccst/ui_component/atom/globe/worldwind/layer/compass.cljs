(ns bh.rccst.ui-component.atom.globe.worldwind.layer.compass
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn compass [layer-name]
  (let [layer (WorldWind/CompassLayer.)]
    (set! (.-displayName layer) layer-name)
    layer))
