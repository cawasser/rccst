(ns bh.rccst.ui-component.atom.resium.globe
  (:require ["resium" :refer (Viewer Globe Entity)]
            ["cesium" :refer (Cartesian3 Ion)]
            [taoensso.timbre :as log]))

(defn globe []
      (log/info "resium Globe")
      (set! (.-defaultAccessToken Ion) "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJkYWNiMDFiNy1iYzFiLTQ2NDMtYmJlNC0zMjRiNTIzMjM5ODQiLCJpZCI6ODQ1MDAsImlhdCI6MTY0NjMyODY1Mn0.Nax1YEWqQzM_eOqHPhblhU9TO9U42VJn4wCcolAkuhM")
      [:div#resium-globe
       [:> Viewer
        [:> Globe [:> Entity {:description "test"
                              :name "tokyo"
                              :point {:pixelSize 10}
                              :position (.fromDegrees Cartesian3 139.7 35.7 100)}]] ]])