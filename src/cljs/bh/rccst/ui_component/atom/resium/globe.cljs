(ns bh.rccst.ui-component.atom.resium.globe
  (:require ["resium" :refer (Viewer CameraFlyTo Globe Entity PolygonGraphics PolylineGraphics PointPrimitive LabelGraphics LabelCollection Label)]
            ["cesium" :refer (Cartesian3 Ion Color CircleGeometry LabelStyle)]
            [bh.rccst.ui-component.atom.resium.shape :as s]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.resium.globe")


(def sample-data [
                  {:shape      :shape/polygon :id "square"
                   :locations [[30.0 -130.0] [30.0 -100.0]
                               [0.0 -100.0] [0.0 -130.0]]
                   :fill-color [1 0 0 0.3] :outline-color [1 0 0 1] :width 2}
                  {:shape      :shape/polygon :id "5-sided"
                   :locations [[37 -115.0] [32.0 -115.0] [33.0 -107.0]
                               [31.0 -102.0] [35.0 -102.0] [37.0 -115.0]]
                   :fill-color [1 0 0 0.6] :outline-color [1 0 0 1] :width 2}
                  {:shape :shape/polyline :id "line1" :locations [[35 -75] [35 -125]]
                   :outline-color [1 1 0 1.0] :width 5}
                  {:shape      :shape/circle :id "circle"
                   :location [28.538336 -81.379234] :radius 1000000
                   :fill-color [0 1 0 0.5] :outline-color [1 1 1 1] :width 2}
                  {:shape :shape/polyline :id "line2" :locations [[22 -55] [45 -105] [36 -125.7]]
                   :outline-color [1 0.5 0.78 1.0] :width 5}
                  {:shape :shape/label :id "orlando" :location [28.538336 -81.379234] :text "Orlando"
                   :fill-color [1 0.5 0.78 1.0] :outline-color [1 0.5 0.78 1.0] :width 1}])


(defn globe [& {:keys [shapes]}]
  ;(log/info "resium Globe" shapes)

  (set! (.-defaultAccessToken Ion) "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJkYWNiMDFiNy1iYzFiLTQ2NDMtYmJlNC0zMjRiNTIzMjM5ODQiLCJpZCI6ODQ1MDAsImlhdCI6MTY0NjMyODY1Mn0.Nax1YEWqQzM_eOqHPhblhU9TO9U42VJn4wCcolAkuhM")

  [:> Viewer
   [:> Globe
    ;[:> LabelCollection
    ; [:> Label {:text "SAMPLE"
    ;            :position (.fromDegrees Cartesian3 -81 28)}]]
    (into [:<>]
      (doall (map-indexed (fn [idx shape]
                            ^{:keys idx}(s/make-shape shape))
               shapes)))]])




(comment
  (def shapes example-shapes)

  (make-shape (first example-shapes))


  [:> Globe
   (into [:<>]
     (doall (map-indexed (fn [idx shape]
                           ^{:keys idx}(make-shape shape))
              shapes)))]

  ())