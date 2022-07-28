(ns rccst.views.atom.example.misc.three-d-globe
  (:require [bh.ui-component.atom.globe.globe-component :as globe]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "3D Globe"
    "A 3D globe based on [Nasa WorldWind](https://github.com/WorldWindEarth/worldwindjs)."
    [globe/example]
    '[]))
