(ns bh.rccst.ui-component.atom.worldwind.globe.location
  (:require ["worldwindjs" :as WorldWind]))


(defn location
  ([[lat lon]]
   (WorldWind/Location. lat lon))

  ([lat lon]
   (WorldWind/Location. lat lon)))




