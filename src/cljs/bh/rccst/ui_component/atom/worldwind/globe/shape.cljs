(ns bh.rccst.ui-component.atom.worldwind.globe.shape
  (:require ["worldwindjs" :as WorldWind]
            [bh.rccst.ui-component.atom.worldwind.globe.location :as location]
            [bh.rccst.ui-component.atom.worldwind.globe.shape.attributes :as attributes]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.worldwind.globe.shape")


(defmulti make-shape (fn [{:keys [shape]}] shape))


(defmethod make-shape :shape/polygon [{:keys [locations
                                              fill-color outline-color
                                              width]}]

  (log/info "polygon" locations "//" fill-color "//" outline-color "//" width)

  (let [attributes (attributes/attributes
                     {:fill-color    fill-color
                      :outline-color outline-color
                      :width         width})
        locs       (->> locations
                     (map location/location)
                     (into-array))]
    (WorldWind/SurfacePolygon. locs attributes)))


(defmethod make-shape :shape/circle [{:keys [location
                                             fill-color outline-color
                                             width radius]}]

  (log/info "circle" location "//" fill-color "//" outline-color "//" width "//" radius)

  (let [attributes (attributes/attributes
                     {:fill-color    fill-color
                      :outline-color outline-color
                      :width         width})]
    (WorldWind/SurfaceCircle. (location/location location) radius attributes)))


(defmethod make-shape :shape/polyline [{:keys [locations outline-color width]}]

  (log/info "circle" locations "//" outline-color "//" width)

  (let [attributes (attributes/attributes
                     {:outline-color outline-color
                      :width         width})
        locs       (->> locations
                     (map location/location)
                     (into-array))]
    (WorldWind/SurfacePolyline. locs attributes)))

(comment
  (do
    (def locations [[-115.0 37.0] [-115.0 32.0]
                    [-107.0 33.0] [-102.0 31.0]
                    [-102.0 35.0] [-115.0 37.0]])
    (def fill-color [0.0 0.5 0.0 0.5])
    (def outline-color [0.0 0.5 0.0 1.0])
    (def width 5))

  (def attributes (attributes/attributes
                    {:fill-color    fill-color
                     :outline-color outline-color
                     :width         width}))
  (def locs (->> locations
              (map location/location)
              (into-array)))
  (def polygon (WorldWind/SurfacePolygon. locs attributes))


  ())
