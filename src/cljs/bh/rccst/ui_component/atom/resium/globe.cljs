(ns bh.rccst.ui-component.atom.resium.globe
  (:require ["resium" :refer (Viewer CameraFlyTo Globe Entity PolygonGraphics PolylineGraphics PointPrimitive LabelGraphics)]
            ["cesium" :refer (Cartesian3 Ion Color CircleGeometry LabelStyle)]
            [taoensso.timbre :as log]))


(def sample-data [{:shape      :shape/polygon :id "square"
                   :locations [[30.0 -130.0] [30.0 -100.0]
                               [0.0 -100.0] [0.0 -130.0]]
                   :fill-color [1 0 0 0.3] :outline-color [1 0 0 1] :width 2}
                  {:shape      :shape/polygon :id "5-sided"
                   :locations [[37 -115.0] [32.0 -115.0] [33.0 -107.0]
                               [31.0 -102.0] [35.0 -102.0] [37.0 -115.0]]
                   :fill-color [1 0 0 0.6] :outline-color [1 0 0 1] :width 2}
                  {:shape :shape/polyline :id "line1" :locations [[35 -75] [35 -125]]
                   :outline-color [1 1 0 1.0] :width 5}
                  ;{:shape      :shape/circle :id "circle"
                  ; :location [28.538336 -81.379234] :radius 1000000
                  ; :fill-color [0 1 0 0.5] :outline-color [1 1 1 1] :width 2}
                  {:shape :shape/polyline :id "line2" :locations [[22 -55] [45 -105] [36 -125.7]]
                   :outline-color [1 0.5 0.78 1.0] :width 5}])


(defn- correct-locations
  "Cesium/Resium locations are [lon lat] while Worldwind locations are [lat lon], so we need this
  function to do the conversion (easier to go in this direction)
  "
  [locations]
  (->> locations
    (map (fn [[lat lon]] [lon lat]))
    flatten))


(defmulti make-shape (fn [{:keys [shape]}] shape))


(defmethod make-shape :shape/polygon [{:keys [locations fill-color]}]
  (let [[r b g a] fill-color]
    [:> Entity
     [:> PolygonGraphics {:hierarchy (.fromDegreesArray Cartesian3 (clj->js (correct-locations locations)))
                          :material  (Color. r g b a)}]]))


(defmethod make-shape :shape/polyline [{:keys [locations width outline-color]}]
  (let [[r b g a] outline-color]
    [:> Entity
     [:> PolylineGraphics {:positions (.fromDegreesArray Cartesian3 (clj->js (correct-locations locations)))
                           :width     width
                           :material  (Color. r g b a)}]]))


(defmethod make-shape :shape/label [{:keys [locations label font fill-color outline-color width]}]
  (let [[r b g a] fill-color]
    [:> Entity
     [:> LabelGraphics {:position     (.fromDegrees Cartesian3 -75.1641667 39.9522222)
                        :label        {:text "Aoi"}
                        :font         "24px Helvetica"
                        :fillColor    (Color. r g b a)
                        :outlineColor (Color. r g b a)
                        :outlineWidth width}]]))


(defn globe [& {:keys [shapes]}]
  ;(log/info "resium Globe" shapes)

  (set! (.-defaultAccessToken Ion) "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJkYWNiMDFiNy1iYzFiLTQ2NDMtYmJlNC0zMjRiNTIzMjM5ODQiLCJpZCI6ODQ1MDAsImlhdCI6MTY0NjMyODY1Mn0.Nax1YEWqQzM_eOqHPhblhU9TO9U42VJn4wCcolAkuhM")

  [:> Viewer
   [:> Globe
    (into [:<>]
      (doall (map-indexed (fn [idx shape]
                            ^{:keys idx}(make-shape shape))
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