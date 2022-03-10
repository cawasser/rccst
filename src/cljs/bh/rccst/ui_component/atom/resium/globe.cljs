(ns bh.rccst.ui-component.atom.resium.globe
  (:require ["resium" :refer (Viewer Globe Entity PolygonGraphics)]
            ["cesium" :refer (Cartesian3 Ion Color)]
            [taoensso.timbre :as log]))

(defn globe []
      (log/info "resium Globe")
      (set! (.-defaultAccessToken Ion) "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJkYWNiMDFiNy1iYzFiLTQ2NDMtYmJlNC0zMjRiNTIzMjM5ODQiLCJpZCI6ODQ1MDAsImlhdCI6MTY0NjMyODY1Mn0.Nax1YEWqQzM_eOqHPhblhU9TO9U42VJn4wCcolAkuhM")
      [:div#resium-globe
       [:> Viewer
        [:> Globe
         [:> Entity {:description "test"
                     :name        "tokyo"}
          [:> PolygonGraphics {:hierarchy (.fromDegreesArray Cartesian3 (clj->js [-115.0 37.0
                                                                          -115.0 32.0
                                                                          -107.0 33.0
                                                                          -102.0 31.0
                                                                          -102.0 35.0]))
                               :material  (.-RED Color)}]]]]])