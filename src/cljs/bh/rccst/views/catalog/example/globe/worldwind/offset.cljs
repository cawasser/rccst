(ns bh.rccst.views.catalog.example.globe.worldwind.offset
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]

            [bh.rccst.views.catalog.example.globe.worldwind.defaults :as defaults]))


(defn offset
  ([]
   (let [[x y] defaults/offset]
     (offset x y)))

  ([x y]
   (WorldWind/Offset. WorldWind/OFFSET_PIXELS x WorldWind/OFFSET_PIXELSy)))
