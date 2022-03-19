(ns bh.rccst.ui-component.atom.worldwind.globe.globe-time
  (:require ["worldwindjs" :as WorldWind]
            [bh.rccst.ui-component.atom.worldwind.globe.layer :as l]))


(defn change-time [this globe-id new-time]
  (if-let [layer (or (l/getLayer this (str globe-id " Night"))
                   (l/getLayer this (str globe-id " Day-only")))]
    (do
      ;(log/info "change-time" (.-displayName layer) new-time)
      (set! (.-time layer) new-time)
      (.redraw (.-wwd this)))))


