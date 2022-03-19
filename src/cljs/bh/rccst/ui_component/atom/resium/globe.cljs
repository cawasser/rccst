(ns bh.rccst.ui-component.atom.resium.globe
  (:require ["resium" :refer (Viewer CameraFlyTo Globe Entity PolygonGraphics PolylineGraphics LabelGraphics)]
            ["cesium" :refer (Cartesian3 Ion Color LabelStyle)]
            [taoensso.timbre :as log]))

(defn globe []
  (log/info "resium Globe")
  (set! (.-defaultAccessToken Ion) "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJkYWNiMDFiNy1iYzFiLTQ2NDMtYmJlNC0zMjRiNTIzMjM5ODQiLCJpZCI6ODQ1MDAsImlhdCI6MTY0NjMyODY1Mn0.Nax1YEWqQzM_eOqHPhblhU9TO9U42VJn4wCcolAkuhM")
  [:div#resium-globe
   [:> Viewer
    [:> Globe
     [:> Entity {:description "test"
                 :name        "west coast"}
      [:> PolygonGraphics {:hierarchy (.fromDegreesArray Cartesian3 (clj->js [-115.0 37.0
                                                                              -115.0 32.0
                                                                              -107.0 33.0
                                                                              -102.0 31.0
                                                                              -102.0 35.0]))
                           :material  (.-RED Color)}]
      [:> PolylineGraphics {:positions (.fromDegreesArray Cartesian3 (clj->js [-75 35 -125 35]))
                            :width     5
                            :material  (.-YELLOW Color)}]

      ; To Do: get this label displayed
      [:> LabelGraphics {:position     (.fromDegrees Cartesian3 -75.1641667 39.9522222)
                         :label        {:text "Aoi"}
                         :font         "24px Helvetica"
                         :fillColor    (.-SKYBLUE Color)
                         :outlineColor (.-BLACK Color)
                         :outlineWidth 2}]]]]])