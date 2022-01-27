(ns bh.rccst.views.catalog.example.globe.worldwind.surface.polygon
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]

            [bh.rccst.views.catalog.example.globe.worldwind.shape-attributes :as shape-attributes]))


(defn polygon [locations props]
  (let [[r g b a] (:color props)
        attributes (shape-attributes/shape-attributes
                     {:interior-color [r g b a]
                      :outline-color [r g b 1.0]
                      :outline-width 2})]
    ;(log/info "polygon" locations props)

    (WorldWind/SurfacePolygon. locations attributes)))
