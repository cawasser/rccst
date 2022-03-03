(ns bh.rccst.ui-component.atom.resium.globe
  (:require ["resium" :refer (Viewer Entity)]
            ["cesium" :refer (Cartesian3)]
            [taoensso.timbre :as log]))

(defn globe []
      [:div#resium-globe
       [:> Viewer
        [:> Entity {:description "test"
                    :name "tokyo"
                    :position (.fromDegrees Cartesian3 -74.0707383, 40.7117244, 100)}]]])
