(ns bh.rccst.ui-component.atom.resium.globe
  (:require ["resium" :refer (Viewer Entity)]
            ["cesium" :refer (Cartesian3)]
            [taoensso.timbre :as log]))

(defn globe []
      (log/info "resium Globe")
      [:div#resium-globe
       [:> Viewer
        [:> Entity {:description "test"
                    :name "tokyo"
                    :point {:pixelSize 10}
                    :position (.fromDegrees Cartesian3 139.7 35.7 100)}]]])