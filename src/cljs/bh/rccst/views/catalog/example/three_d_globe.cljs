(ns bh.rccst.views.catalog.example.three-d-globe
  (:require [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as acu]))



(defn example []
  (acu/demo "3D Globe"
    "A 3D globe based on [Nasa WorldWind](https://github.com/WorldWindEarth/worldwindjs)."
    [layout/centered {:extra-classes :width-50}
     [:div "A 3D Globe"]]
    '[]))