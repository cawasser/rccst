(ns bh.rccst.views.catalog.example.globe.worldwind.layer.tesselation
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn tesselation [layer-name]
  ;(log/info "createLayer" layer-name)

  (let [layer (WorldWind/ShowTessellationLayer.)]
    (set! (.-displayName layer) layer-name)
    layer))





