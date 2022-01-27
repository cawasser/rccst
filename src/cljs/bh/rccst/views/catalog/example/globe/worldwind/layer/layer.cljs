(ns bh.rccst.views.catalog.example.globe.worldwind.layer.layer
  (:require ["worldwindjs" :as WorldWind]
            [taoensso.timbre :as log]))


; for debugging at the repl
(def last-this (atom ()))


(defn getLayer [this layer-name]
  (let [layer (.filter (.. this -wwd -layers)
                #(= (.-displayName %) layer-name))]
    ;(log/info "getLayer"
    ;  (.-length layer)
    ;  (map #(.-displayName %) layer))
    (first layer)))


(defn addLayer [this idx [_ layer]]
  ;(log/info "addLayer" (.-displayName layer))
  (reset! last-this this)

  (.insertLayer (.-wwd this) idx layer)
  (.redraw (.-wwd this))

  layer)


(defn removeLayer [this layer-name]
  ;(log/info "removeLayer" layer-name)
  (reset! last-this this)
  (if-let [layer (getLayer this layer-name)]
    (do
      (.removeLayer (.-wwd this) layer)
      ;(log/info "removed?" (map #(.-displayName %)
      ;                       (.. this -wwd -layers)))
      (.redraw (.-wwd this)))))



; work out finding/removing layers
(comment
  (def layer-name "Compass")
  (def layer-name "Blue Marble")

  @last-this

  (.. @last-this -props -nextIndex)

  (getLayer @last-this layer-name)

  (.filter (.. @last-this -wwd -layers)
    #(= (.-displayName %) layer-name))


  (def layers (.. @last-this -wwd -layers))
  (.-length layers)
  (map #(.-displayName %) layers)

  (def layer (.filter (.. @last-this -wwd -layers)
               #(= (.-displayName %) layer-name)))
  (.-length layer)
  (map #(.-displayName %) layer)
  (js/typeof layers)

  (.. @last-this -wwd -layers -length)
  (map #(.-displayName %) (.. @last-this -wwd -layers))

  (if-let [layer (first (getLayer @last-this layer-name))]
    (do
      (.removeLayer (.-wwd @last-this) layer)
      (.redraw (.-wwd @last-this))))

  ())