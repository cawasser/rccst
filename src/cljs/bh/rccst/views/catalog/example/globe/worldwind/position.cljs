(ns bh.rccst.views.catalog.example.globe.worldwind.position
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]

            [bh.rccst.views.catalog.example.globe.worldwind.defaults :as default]))


(defn position
  ([[lat lon]]
   ;(log/info "[lat lon]")
   (WorldWind/Position. lat lon default/altitude))

  ([[lat lon] alt]
   ;(log/info "[lat lon] alt")
   (WorldWind/Position. lat lon alt)))

