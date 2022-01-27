(ns bh.rccst.views.catalog.example.globe.worldwind.location
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn location [[lat lon]]
  (WorldWind/Location. lat lon))
