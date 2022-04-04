(ns bh.rccst.ui-component.atom.worldwind.globe
  (:require ["worldwindjs" :as WorldWind]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.blue-marble :as blue-marble]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.compass :as compass]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.night :as night]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.star-field :as star-field]
            [bh.rccst.ui-component.atom.worldwind.globe.projection :as proj]
            [bh.rccst.ui-component.atom.worldwind.globe.react-support :as rs]
            [bh.rccst.ui-component.atom.worldwind.globe.shape :as shape]
            [bh.rccst.ui-component.utils.helpers :as h]
            [cljs-time.coerce :as coerce]
            [cljs-time.core :as cljs-time]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.worldwind.globe")


(def DEFAULT_BACKGROUND_COLOR "rgb(36,74,101)")


(def sample-data [
                  {:shape      :shape/polygon :id "square"
                   :locations  [[30.0 -130.0] [30.0 -100.0]
                                [0.0 -100.0] [0.0 -130.0]]
                   :fill-color [1 0 0 0.3] :outline-color [1 0 0 1] :width 2}
                  {:shape      :shape/polygon :id "5-sided"
                   :locations  [[37 -115.0] [32.0 -115.0] [33.0 -107.0]
                                [31.0 -102.0] [35.0 -102.0] [37.0 -115.0]]
                   :fill-color [1 0 0 0.6] :outline-color [1 0 0 1] :width 2}
                  {:shape         :shape/polyline :id "line1" :locations [[35 -75] [35 -125]]
                   :outline-color [1 1 0 1.0] :width 5}
                  {:shape      :shape/circle :id "circle"
                   :location   [28.538336 -81.379234] :radius 1000000
                   :fill-color [0 1 0 0.5] :outline-color [1 1 1 1] :width 2}
                  {:shape         :shape/polyline :id "line2" :locations [[22 -55] [45 -105] [36 -125.7]]
                   :outline-color [1 0.5 0.78 1.0] :width 5}
                  {:shape      :shape/label :id "orlando" :location [28.538336 -81.379234] :label "Orlando"
                   :fill-color [1 0.9 0.0 1.0] :outline-color [1 0.9 0.0 1.0] :width 1}])


(defn- base-layers [globe-id]
  {(str globe-id " Blue Marble") (blue-marble/blue-marble (str globe-id " Blue Marble"))
   ;(str globe-id " day-only") (day-only/day-only (str globe-id " day-only"))})
   (str globe-id " Night")       (night/night (str globe-id " Night"))
   (str globe-id " Compass")     (compass/compass (str globe-id " Compass"))
   (str globe-id " Star Field")  (star-field/star-field (str globe-id " Star Field"))})


(defn- build-child-layer [id children]
  ;(log/info "build-child-layer" id children)

  (let [layer (WorldWind/RenderableLayer.)]
    (set! (.-displayName layer) id)

    (doall
      (map (fn [child]
             ;(log/info "build-child-layer (adding)" child)
             (.addRenderable layer (shape/make-shape child)))
        children))

    ;(log/info "build-child-layer (built)" id (count (.-renderables layer)))

    {id layer}))


(defn- globe* [props & children]
  ;(log/info "globe-render" props children)

  (let [state    (atom {:children children})
        dom-node (r/atom nil)]

    (r/create-class
      {:display-name         (:id props)

       :constructor          (fn [this props children]
                               ;(log/info "constructor" props
                               ;  "////" (r/props this)
                               ;  "////" (r/children this))
                               (swap! state assoc
                                 :wwd ()
                                 :canvasId (or (:id (r/props this)) (str "canvas_" (js/Date.now)))
                                 :id (or (:id (r/props this)) (str "canvas_" (js/Date.now)))
                                 :isValid false
                                 :isDropArmed false
                                 :projection (or (:projection (r/props this)) (nth proj/projections 0))
                                 :layers (r/children this)))

       :component-did-mount  (partial rs/component-did-mount dom-node state)

       :component-did-update (partial rs/component-did-update dom-node state)

       :reagent-render
       (fn [props & children]
         @dom-node
         (let [cursor          (if (:isDropArmed @state) "crosshair" "default")
               backgroundColor (or (:backgroundColor @state) DEFAULT_BACKGROUND_COLOR)]

           [:canvas (merge props {:id (:canvasId @state)})
            "Your browser does not support HTML5 Canvas."]))})))


(defn- globe-inter [& {:keys [shapes time component-id]}]
  (let [shape-layers (->> shapes (map shape/make-shape) (into {}))
        all-layer    (merge
                       {}
                       (base-layers component-id)
                       shape-layers)]

    (log/info "globe-inter" shapes "//" time "//" (count (.-renderables shape-layers)))

    [globe*
     {:id         component-id
      :time       time ;(coerce/to-date (cljs-time/now))
      :projection "3D"
      :style      {:background-color :black
                   :border-radius    "3px"
                   :width            "100%"
                   :height           "100%"}}
     all-layer]))


(defn globe [& {:keys [shapes current-time component-id container-id]}]

  (let [s (h/resolve-value shapes)
        t (h/resolve-value current-time)]

    (log/info "globe OUTER" shapes current-time "//" @s @t "//" component-id)

    (fn []
      ;(log/info "globe INNER" shapes time component-id)

      [globe-inter
       :shapes @s
       :time @t
       :component-id component-id
       :container-id container-id])))


(def meta-data {:ww/globe {:component globe
                           :ports     {:shapes :port/sink
                                       :current-time   :port/sink}}})


(comment
  (def component-id "dummy")
  (def children sample-data)
  (def dummy (build-child-layer component-id children))

  (count (.-renderables dummy))

  ())