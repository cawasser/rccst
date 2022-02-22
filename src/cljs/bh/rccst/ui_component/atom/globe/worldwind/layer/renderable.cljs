(ns bh.rccst.ui-component.atom.globe.worldwind.layer.renderable
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


(defn renderable-layer [layer-name children]
  (let [layer (WorldWind/RenderableLayer.)]
    (set! (.-displayName layer) layer-name)
    (doall
      (map (fn [child]
             ;(log/info "renderable-layer adding" layer-name child)
             (.addRenderable layer child))
        children))
    layer))


; work out the logic for adding children
(comment
  (require '[globe-cljs.surface.polygon :as polygon])

  (def children [(polygon/createPolygon [0 0] {:color [255 0 0 1]})
                 (polygon/createPolygon [0 1] {:color [0 255 0 1]})
                 (polygon/createPolygon [1 0] {:color [0 0 255 1]})])

  (def layer (WorldWind/RenderableLayer.))

  (map (fn [child]
         (.addRenderable layer child))
    children)

  (def layer2 (createLayer "polygons" children))

  (count (.-renderables layer))
  (count (.-renderables layer2))

  ())