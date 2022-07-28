(ns bh.ui-component.atom.worldwind.globe.shape
  (:require ["worldwindjs" :as WorldWind]
            [bh.ui-component.atom.worldwind.globe.location :as location]
            [bh.ui-component.atom.worldwind.globe.shape.attributes :as attributes]
            [taoensso.timbre :as log]))


(log/info "bh.ui-component.atom.worldwind.globe.shape")


(defn- wrap-shape [id shape]
  (let [layer (WorldWind/RenderableLayer.)]
    (set! (.-displayName layer) id)
    (.addRenderable layer shape)

    {id layer}))


(defmulti make-shape (fn [{:keys [shape]}] shape))


(defmethod make-shape :shape/polygon [{:keys [id locations
                                              fill-color outline-color
                                              width]}]

  ;(log/info "polygon" locations "//" fill-color "//" outline-color "//" width)

  (let [attributes (attributes/shape-attributes
                     {:fill-color    fill-color
                      :outline-color outline-color
                      :width         width})
        locs       (->> locations
                     (map location/location)
                     (into-array))
        polygon    (WorldWind/SurfacePolygon. locs attributes)]
    (set! (.-displayName polygon) id)
    (wrap-shape id polygon)))


(defmethod make-shape :shape/circle [{:keys [id location
                                             fill-color outline-color
                                             width radius]}]

  ;(log/info "circle" location "//" fill-color "//" outline-color "//" width "//" radius)

  (let [attributes (attributes/shape-attributes
                     {:fill-color    fill-color
                      :outline-color outline-color
                      :width         width})
        circle     (WorldWind/SurfaceCircle. (location/location location)
                     radius attributes)]
    (set! (.-displayName circle) id)
    (wrap-shape id circle)))


(defmethod make-shape :shape/polyline [{:keys [id locations outline-color width]}]

  ;(log/info "circle" locations "//" outline-color "//" width)

  (let [attributes (attributes/shape-attributes
                     {:outline-color outline-color
                      :width         width})
        locs       (->> locations
                     (map location/location)
                     (into-array))
        polyline   (WorldWind/SurfacePolyline. locs attributes)]
    (set! (.-displayName polyline) id)
    (wrap-shape id polyline)))


(defmethod make-shape :shape/label [{:keys [id label location fill-color outline-color width]}]

  ;(log/info "label" location "//" label "//" fill-color "//" width)

  (let [label      (WorldWind/GeographicText. (location/position location) label)
        attributes (attributes/text-attributes
                     {:fill-color fill-color
                      :outline-color outline-color
                      :width         width})]
    (set! (.-attributes label) attributes)
    (wrap-shape id label)))



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
