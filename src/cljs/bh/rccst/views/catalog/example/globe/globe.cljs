(ns bh.rccst.views.catalog.example.globe.globe
  (:require ["worldwindjs" :as WorldWind]
            ["./worldwind/worldwind-react-globe.js" :as Globe]
            ["worldwind-react-globe-bs4" :as bs4]
            ["reactstrap" :as rs]
            [reagent.core :as reagent]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]                          ;????
            [taoensso.timbre :as log]
            [clojure.set :as set]
            [woolybear.ad.catalog.utils :as acu]
            [bh.rccst.views.catalog.example.globe.worldwind.layer-management :as lm]
            [bh.rccst.views.catalog.example.globe.worldwind.continental-locations :as cl]
            [bh.rccst.views.catalog.example.globe.worldwind.layer.layer :as l]
            [bh.rccst.views.catalog.example.globe.worldwind.layer.controls :as controls]
            [bh.rccst.views.catalog.example.globe.worldwind.layer.coordinates :as coords]))


(def last-this (reagent/atom {}))


(def DEFAULT_BACKGROUND_COLOR "rgb(36,74,101)")


(def projections ["3D",
                  "Equirectangular",
                  "Mercator",
                  "North Polar",
                  "South Polar",
                  "North UPS",
                  "South UPS",
                  "North Gnomonic",
                  "South Gnomonic"])


(defn- change-projection [this new-projection]
  (reset! last-this this)

  ;(log/info "change-projection" new-projection
  ;  "//// roundGlobe" (.-roundGlobe this)
  ;  "//// flatGlobe " (.-flatGlobe this))

  (if (= "3D" new-projection)
    (do
      ;(log/info "changing to roundGlobe")
      (if (not (.-roundGlobe this))
        (set! (.-roundGlobe this) (WorldWind/Globe. (WorldWind/EarthElevationModel.))))

      ; Replace the flat globe
      (if (not= (.. this -wwd -globe) (.-roundGlobe this))
        (do
          ;(log/info "setting the roundGlobe" (.-roundGlobe this))
          (set! (.. this -wwd -globe) (.-roundGlobe this))
          (.redraw (.-wwd this)))))

    (do
      ;(log/info "changing to flatGlobe")

      (if (not (.-flatGlobe this))
        (set! (.-flatGlobe this) (WorldWind/Globe2D.)))

      (set! (.. this -flatGlobe -projection)
        (condp = new-projection
          "Equirectangular" (WorldWind/ProjectionEquirectangular.)
          "Mercator" (WorldWind/ProjectionMercator.)
          "North Polar" (WorldWind/ProjectionPolarEquidistant. "North")
          "South Polar" (WorldWind/ProjectionPolarEquidistant. "South")
          "North UPS" (WorldWind/ProjectionUPS. "North")
          "South UPS" (WorldWind/ProjectionUPS. "South")
          "North Gnomonic" (WorldWind/ProjectionGnomonic. "North")
          "South Gnomonic" (WorldWind/ProjectionGnomonic. "South")))

      (if (not= (.. this -wwd -globe) (.-flatGlobe this))
        (do
          ;(log/info "setting the flatGlobe" (.-flatGlobe this))
          (set! (.. this -wwd -globe) (.-flatGlobe this))
          (.redraw (.-wwd this)))))))


(defn change-time [this globe-id new-time]
  (reset! last-this this)

  (if-let [layer (or (l/getLayer this (str globe-id " Night")) (l/getLayer this (str globe-id " Day-only")))]
    (do
      ;(log/info "change-time" (.-displayName layer) new-time)
      (set! (.-time layer) new-time)
      (.redraw (.-wwd this)))))


(defn- update-children [this new-children old-children]
  (let [new-keys (set (keys new-children))
        old-keys (set (keys old-children))
        added    (set/difference new-keys old-keys)
        removed  (set/difference old-keys new-keys)]

    ; remove old stuff
    (if removed
      (do
        ;(log/info "component-did-update removing" removed)
        (doall (map #(l/removeLayer this %) removed))
        (.redraw (.-wwd this))))

    ; add new stuff
    (if added
      (do
        ;(log/info "component-did-update adding" added)
        (doall
          (for [[idx child] (map-indexed vector added)]
            (do
              ;(log/info "adding" idx child)
              (l/addLayer this idx [child (get new-children child)]))))
        (.redraw (.-wwd this))))))


(defn- component-did-mount [dom-node state this]
  ;(log/info "component-did-mount" @state)

  (reset! last-this this)

  (let [node (rdom/dom-node this)]
    ;; This will trigger a re-render of the component.
    (reset! dom-node node))

  (let [canvasId (.-id @dom-node)
        props    (reagent/props this)]

    ;(log/info "component-did-mount" (.-id @dom-node)
    ;  "//// props" (reagent/props this)
    ;  "//// children" (reagent/children this))

    ;Create the WorldWindow using the ID of the canvas
    (set! (.-wwd this) (WorldWind/WorldWindow. canvasId))
    (swap! state assoc :wwd (.-wwd this))

    ; Apply projection support
    (set! (.-roundGlobe this) (.-globe (.-wwd this)))


    (if (:projection props)
      (do
        ;(log/info "set-projection"  canvasId(:projection props))
        (change-projection this (:projection props))))

    (doall
      (for [[idx child] (map-indexed vector (first (reagent/children this)))]
        (do
          ;(log/info "adding layer" idx child)
          (l/addLayer this idx child))))

    ; add the controls layer
    (l/addLayer this -1 [(str canvasId " Controls") (controls/controls this (str canvasId " Controls"))])

    ; add the coordinates layer
    (l/addLayer this -1 [(str canvasId " Coordinates") (coords/coordinates this (str canvasId " Coordinates"))])

    (if (:time props)
      (do
        ;(log/info "set-time" canvasId (:time props))
        (change-time this (:id @state) (:time props))))

    ;(log/info "component-did-mount" (sort (map #(.-displayName %) (.-layers (.-wwd this)))))

    (.redraw (.-wwd this))))


(defn- component-did-update [dom-node state this old-argv]
  (let [[_ new-props new-children] (reagent/argv this)
        [old-id old-props old-children] old-argv]

    (reset! last-this this)

    ;(log/info "component-did-update"
    ;  "old-children" (sort (keys old-children))
      ;"//// old-props" old-props
      ;"//// new-children" (sort (keys new-children)))
      ;"//// new-props" new-props)

    ;(log/info "projection"
    ;  (:projection old-props)
    ;  (:projection new-props))

    (if (not= (:projection old-props) (:projection new-props))
      (change-projection this (:projection new-props)))

    (if (not= (:time old-props) (:time new-props))
      (do
        ;(log/info "update-time" (:id @state) (:time new-props))
        (change-time this (:id @state) (:time new-props))))

    (update-children this new-children old-children)

    ;(log/info "component-did-update" (sort (map #(.-displayName %) (.-layers (.-wwd this)))))

    (.redraw (.-wwd this))))


(defn globe [props & children]
  ;(log/info "globe" props children)

  (let [state    (atom {:children children})
        dom-node (reagent/atom nil)]

    (reagent/create-class
      {:display-name         (:id props)

       :constructor          (fn [this props children]
                               ;(log/info "constructor" props
                               ;  "////" (reagent/props this)
                               ;  "////" (reagent/children this))
                               (swap! state assoc
                                 :wwd ()
                                 :canvasId (or (:id (reagent/props this)) (str "canvas_" (js/Date.now)))
                                 :id (or (:id (reagent/props this)) (str "canvas_" (js/Date.now)))
                                 :isValid false
                                 :isDropArmed false
                                 :projection (or (:projection (reagent/props this)) (nth projections 0))
                                 :layers (reagent/children this)))

       :component-did-mount  (partial component-did-mount dom-node state)

       :component-did-update (partial component-did-update dom-node state)

       :reagent-render
       (fn [props & children]
         @dom-node
         (let [cursor          (if (:isDropArmed @state) "crosshair" "default")
               backgroundColor (or (:backgroundColor @state) DEFAULT_BACKGROUND_COLOR)]

           [:canvas (merge props {:id (:canvasId @state)})
            "Your browser does not support HTML5 Canvas."]))})))

(defn example []
      (let [globeRef (reagent/atom nil)
            layersRef (reagent/atom nil)
            layers (lm/make-layers)]
           (acu/demo
             "Nasa Worldwind 3D Globe"

             (reagent/create-element
               (reagent/create-class
                 {:display-name "Globe"

                  ;:component-did-mount
                  ;  (fn [comp]
                  ;    (r/set-state this {:globe @globeRef}))

                  :reagent-render
                                (fn []
                                    [:div#all {:style {:width "100%" :height "100%" :overflow "hidden"}}
                                     [:div#nav {:style {:width "100%" }}
                                      [:> bs4/NavBar {:logo  ""
                                                      :title ""
                                                      :items [(reagent/as-element
                                                                [:> bs4/NavBarItem {:key      "lyr"
                                                                                    :title    "Layers"
                                                                                    :icon     "list"
                                                                                    :collapse @layersRef}])]}]]

                                     [:div#contain {:style {:width "100%" :height "100%"}}
                                      [:> rs/Container {:fluid "lg"
                                                        :style {:width  "100%"
                                                                :height "100%"}}
                                       [:div#globe {:style     {:width        "100%"
                                                               :height       "100%"
                                                               :text-align   :center
                                                               :border-style :none}
                                                   :className "globe"}

                                         [:> Globe (merge {:ref    #(if (not= @globeRef %) (reset! globeRef %) globeRef)
                                                           :layers layers}
                                                          (:n-america cl/start-loc))]]

                                       [:div.overlayCards.noninteractive
                                        [:> rs/CardColumns
                                         [:> bs4/LayersCard {:ref        #(reset! layersRef %)
                                                             :categories ["overlay" "base"]
                                                             :globe      @globeRef}]]]]]])})))))





; can we get the local data from the [:widgets id] subtree in app-db?
(comment
  (def dom-node (atom (rdom/dom-node @last-this)))

  (def canvasId (.-id @dom-node))
  (def props (reagent/props @last-this))

  (reagent/children @last-this)

  ())


; change projections
(comment
  (def new-projection "Mercator")

  @last-this

  (if (not (.-flatGlobe @last-this))
    (set! (.-flatGlobe @last-this) (WorldWind/Globe2D.)))

  (set! (.. @last-this -flatGlobe -projection)
    (WorldWind/ProjectionEquirectangular.))

  ())


; change the time on a day or night layer, if there is one
(comment
  (def globe-id "globe-1")
  (or (l/getLayer @last-this (str globe-id " Night"))
    (l/getLayer @last-this (str globe-id " Day-only")))


  ())