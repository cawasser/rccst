(ns bh.rccst.views.catalog.example.globe.worldwind.surface.image
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]

            [bh.rccst.views.catalog.example.globe.worldwind.shape-attributes :as shape-attributes]))

(def default-symbol "imgs/worldwind/symbols/default.png")

(defn image [sector symbol]
  (let [result (if symbol
                   (WorldWind/SurfaceImage. sector "imgs/worldwind/400x230-splash-nww.png")
                   (WorldWind/SurfaceImage. sector "imgs/worldwind/400x230-splash-nww.png"))]
    (log/info "image/image" symbol)
    result))

