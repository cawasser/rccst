(ns bh.rccst.ui-component.atom.worldwind.globe.shape.attributes
  (:require ["worldwindjs" :as WorldWind]
            [bh.rccst.ui-component.atom.worldwind.globe.color :as color]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.worldwind.globe.shape.attributes")


(defn attributes [{:keys [fill-color outline-color width]}]

  (log/info "attributes" fill-color "//" outline-color "//" width)

  (let [attributes (WorldWind/ShapeAttributes.)]
    (set! (.-interiorColor attributes) (color/color (or fill-color color/default-fill-color)))
    (set! (.-outlineColor attributes) (color/color (or outline-color color/default-outline-color)))
    (set! (.-outlineWidth attributes) (or width color/default-width))

    attributes))


(comment
  (color/color [0.0 0.5 0.0 0.3])


  ())