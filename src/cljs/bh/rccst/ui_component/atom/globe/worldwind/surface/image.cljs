(ns bh.rccst.ui-component.atom.globe.worldwind.surface.image
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.globe.worldwind.shape-attributes :as shape-attributes]))

(def default-symbol "images/symbols/default.png")

(defn image [sector symbol]
  (let [result (if symbol
                   (WorldWind/SurfaceImage. sector "images/400x230-splash-nww.png")
                   (WorldWind/SurfaceImage. sector "images/400x230-splash-nww.png"))]
    (log/info "image/image" symbol)
    result))

