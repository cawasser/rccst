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
  (log/info "make-shape :shape/circle" location radius fill-color outline-color width)
  (let [[f-r f-g f-b f-a] fill-color
        [o-r o-g o-b o-a] outline-color]
    [:> Entity
     [:> EllipseGraphics {:position (.fromDegrees Cartesian3 -111.0, 40.0, 150000.0)
                          ;:position      ((fn [[lon lat]]
                          ;                  (.fromDegrees Cartesian3 lon lat 100))
                          ;                (clj->js (correct-location location)))
                          :semiMajorAxis 300000.0
                          :semiMinorAxis 300000.0
                          ;:outlineWidth  width
                          ;:outlineColor  (Color. o-r o-g o-b o-a)
                          :outline       true}]]))
                          ;:material      (.-GREEN Color)}]])) ;(Color. f-r f-g f-b f-a)}}]]))

(defmethod make-shape :shape/label [{:keys [location label font fill-color outline-color width]}]
  (let [[r g b a] fill-color]
    [:> Entity
     [:> LabelGraphics {:position     (.fromDegrees Cartesian3 -81 27)
                        :label        "Orlando"}]]))
                        ;:font         "24px Helvetica"}]]))
                        ;:fillColor    (Color. r g b a)
                        ;:outlineColor (Color. r g b a)
                        ;:outlineWidth width}]]))


(defmethod make-shape :default [_]
  [:> Entity])
