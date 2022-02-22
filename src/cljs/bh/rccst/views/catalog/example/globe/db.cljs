(ns bh.rccst.views.catalog.example.globe.db
  (:require [bh.rccst.views.catalog.example.globe.worldwind.layer.blue-marble :as blue-marble]
            [bh.rccst.views.catalog.example.globe.worldwind.layer.compass :as compass]
            [bh.rccst.views.catalog.example.globe.worldwind.layer.star-field :as star-field]
            [bh.rccst.views.catalog.example.globe.worldwind.layer.night :as night]

            [taoensso.timbre :as log]))


(defn globe-config [globe-id min-max]
  (log/info "globe-config" globe-id min-max)
  {:projection       "3D"
   :base-layers      (merge {(str globe-id " Blue Marble") (blue-marble/blue-marble (str globe-id " Blue Marble"))
                             (str globe-id " Night")       (night/night (str globe-id " Night"))}
                       (if (= :min min-max)
                         {}
                         {(str globe-id " Compass")    (compass/compass (str globe-id " Compass"))
                          (str globe-id " Star Field") (star-field/star-field (str globe-id " Star Field"))}))
   :selected-sensors #{}
   :selected-aois    #{}
   :layers           {}
   :time             0})



