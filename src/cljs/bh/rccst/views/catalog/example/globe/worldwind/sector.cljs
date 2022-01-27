(ns bh.rccst.views.catalog.example.globe.worldwind.sector
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn sector
  ([minLat maxLat minLon maxLon]
   (log/info "sector" minLat maxLat minLon maxLon)
   (WorldWind/Sector. minLat maxLat minLon maxLon))

  ([[minLat maxLat minLon maxLon]]
   (log/info "sector [" minLat maxLat minLon maxLon "]")
   (WorldWind/Sector. minLat maxLat minLon maxLon)))