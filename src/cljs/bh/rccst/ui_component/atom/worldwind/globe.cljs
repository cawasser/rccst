(ns bh.rccst.ui-component.atom.worldwind.globe
  (:require ["worldwindjs" :as WorldWind]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.blue-marble :as blue-marble]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.day-only :as day-only]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.compass :as compass]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.night :as night]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.star-field :as star-field]
            [bh.rccst.ui-component.atom.worldwind.globe.projection :as proj]
            [bh.rccst.ui-component.atom.worldwind.globe.react-support :as rs]
            [cljs-time.coerce :as coerce]
            [cljs-time.core :as cljs-time]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.worldwind.globe")


(def DEFAULT_BACKGROUND_COLOR "rgb(36,74,101)")


(defn base-layers [globe-id]
  {(str globe-id " Blue Marble") (blue-marble/blue-marble (str globe-id " Blue Marble"))
   ;(str globe-id " day-only") (day-only/day-only (str globe-id " day-only"))})
   (str globe-id " Night")       (night/night (str globe-id " Night"))
   (str globe-id " Compass")     (compass/compass (str globe-id " Compass"))
   (str globe-id " Star Field")  (star-field/star-field (str globe-id " Star Field"))})


(defn globe* [& {:keys [props children]}]
  (log/info "globe*" props children)

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


(defn- add-base-layers [children component-id]
  (merge (base-layers component-id)
    {}))


(defn globe [& {:keys [children component-id]}]
  (log/info "globe" children component-id)

  [globe*
   {:id         component-id
    :min-max    :max
    :time       (coerce/to-date (cljs-time/now))
    :projection "3D"
    :style      {:background-color :black
                 :width            "100%"
                 :height           "100%"}}
   (add-base-layers children component-id)])
