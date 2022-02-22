(ns bh.rccst.views.catalog.example.misc.three-d-globe
  (:require [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as acu]
            [bh.rccst.ui-component.atom.globe.globe-component :as globe]))



(defn example []
  (acu/demo "3D Globe"
    "A 3D globe based on [Nasa WorldWind](https://github.com/WorldWindEarth/worldwindjs)."
     [globe/example]
    '[]))