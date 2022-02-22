(ns bh.rccst.ui-component.atom.globe.worldwind.layer.blue-marble
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn blue-marble [layer-name]
  (let [layer (WorldWind/BMNGLayer.)]
    (set! (.-displayName layer) layer-name)
    layer))

