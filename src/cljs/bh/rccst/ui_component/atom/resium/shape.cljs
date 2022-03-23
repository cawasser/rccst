(ns bh.rccst.ui-component.atom.resium.shape
  (:require ["resium" :refer (Viewer CameraFlyTo Globe Entity EllipseGraphics PolygonGraphics PolylineGraphics PointPrimitive LabelGraphics)]
            ["cesium" :refer (Cartesian3 Ion Color CircleGeometry LabelStyle)]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.resium.shape")


(defn- correct-locations
  "Cesium/Resium locations are [lon lat] while Worldwind locations are [lat lon], so we need this
  function to do the conversion (easier to go in this direction)
  "
  [locations]
  (->> locations
    (map (fn [[lat lon]] [lon lat]))
    flatten))


(defn- correct-location [[lat lon]]
  [lon lat])


(defn- cartesian3
  ([[lon lat]] (.fromDegrees Cartesian3 lon lat))

  ([lon lat] (.fromDegrees Cartesian3 lon lat)))


(defmulti make-shape (fn [{:keys [shape]}] shape))


(defmethod make-shape :shape/polygon [{:keys [locations fill-color]}]
  (let [[r b g a] fill-color]
    [:> Entity
     [:> PolygonGraphics {:hierarchy (.fromDegreesArray Cartesian3 (clj->js (correct-locations locations)))
                          :material  (Color. r g b a)}]]))


(defmethod make-shape :shape/polyline [{:keys [locations width outline-color]}]
  (let [[r g b a] outline-color]
    [:> Entity
     [:> PolylineGraphics {:positions (.fromDegreesArray Cartesian3 (clj->js (correct-locations locations)))
                           :width     width
                           :material  (Color. r g b a)}]]))


(defmethod make-shape :shape/circle [{:keys [location radius fill-color outline-color width]}]
  (let [[f-r f-g f-b f-a] fill-color
        [o-r o-g o-b o-a] outline-color]
    [:> Entity {:position (cartesian3 (correct-location location))}
     [:> EllipseGraphics {:semiMajorAxis radius
                          :semiMinorAxis radius
                          :outlineColor  (Color. o-r o-g o-b o-a)
                          :outline       true
                          :material      (Color. f-r f-g f-b f-a)}]])) ;(Color. f-r f-g f-b f-a)}}]]))


(defmethod make-shape :shape/label [{:keys [location label font fill-color outline-color width]}]
  (let [[f-r f-g f-b f-a] fill-color
        [o-r o-g o-b o-a] outline-color]
    [:> Entity {:position (cartesian3 (correct-location location))}
     [:> LabelGraphics {:label        label
                        :font         (or font "24px Helvetica")
                        :fillColor    (Color. f-r f-g f-b f-a)
                        :outlineColor (Color. o-r o-g o-b o-a)
                        :outlineWidth width
                        :show         true}]]))


(defmethod make-shape :default [_]
  [:> Entity])
