(ns bh.rccst.ui-component.atom.resium.globe
  (:require ["resium" :refer (Viewer CameraFlyTo Globe Entity PolygonGraphics PolylineGraphics PointPrimitive LabelGraphics)]
            ["cesium" :refer (Cartesian3 Ion Color CircleGeometry LabelStyle)]
            [taoensso.timbre :as log]))


(defmulti make-shape (fn [{:keys [shape]}]
                         shape))

(defmethod make-shape :polygon [{:keys [locations color]}]
           (let [[r b g a] color]
             [:> PolygonGraphics {:hierarchy (.fromDegreesArray Cartesian3 (clj->js locations))
                                  :material (Color. r g b a)}]))

(defmethod make-shape :polyline [{:keys [locations width color]}]
           (let [[r b g a] color]
             [:> PolylineGraphics {:positions (.fromDegreesArray Cartesian3 (clj->js locations))
                                   :width width
                                   :material (Color. r g b a)}]))

(defmethod make-shape :label [{:keys [locations label font fillColor outlineColor outlineWidth]}]
           (let [[r b g a] fillColor]
             [:> LabelGraphics {:position (.fromDegrees Cartesian3 -75.1641667 39.9522222)
                                :label    {:text "Aoi"}
                                :font "24px Helvetica"
                                :fillColor (Color. r g b a)
                                :outlineColor (Color. r g b a)
                                :outlineWidth 2}]))

(def example-shapes [{:shape :polygon :locations [-115.0 37.0 -115.0 32.0 -107.0 33.0 -102.0 31.0 -102.0 35.0] :color [0.5 0.5 0.5 0.8]}
                     {:shape :polyline :locations [-75 35 -125 35] :width 5 :color [1 0 0 0.8]}])

(defn globe [& {:keys [shapes]}]
      (log/info "resium Globe")
      (set! (.-defaultAccessToken Ion) "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJkYWNiMDFiNy1iYzFiLTQ2NDMtYmJlNC0zMjRiNTIzMjM5ODQiLCJpZCI6ODQ1MDAsImlhdCI6MTY0NjMyODY1Mn0.Nax1YEWqQzM_eOqHPhblhU9TO9U42VJn4wCcolAkuhM")
      [:div
       [:> Viewer
        [:> Globe
         [:> Entity {:description "west"}]]]])
          ;[(into [:<>]
          ;       (doall (map (fn [[shape]]
          ;                       (make-shape shape)))))]]]]])




(comment

  (make-shape (first example-shapes))


  (into [:<>] (map make-shape example-shapes))

  ())