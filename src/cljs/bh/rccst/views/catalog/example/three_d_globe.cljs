(ns bh.rccst.views.catalog.example.three-d-globe
  (:require [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as acu]
            [bh.rccst.views.catalog.example.globe.globe-component :as globe]))



(defn example []
  (acu/demo "3D Globe"
    "A 3D globe based on [Nasa WorldWind](https://github.com/WorldWindEarth/worldwindjs)."
     [globe/example]
    '[]))