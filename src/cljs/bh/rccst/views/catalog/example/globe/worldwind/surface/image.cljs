(ns bh.rccst.views.catalog.example.globe.worldwind.surface.image
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]

            [bh.rccst.views.catalog.example.globe.worldwind.shape-attributes :as shape-attributes]))

(def default-symbol "images/symbols/default.png")

(defn image [sector symbol]
  (let [result (if symbol
                   (WorldWind/SurfaceImage. sector symbol)
                   (WorldWind/SurfaceImage. sector default-symbol))]
    (log/info "image/image" symbol)
    result))

