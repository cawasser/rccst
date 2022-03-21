(ns bh.rccst.ui-component.atom.worldwind.globe.react-support
  (:require ["worldwindjs" :as WorldWind]
            [reagent.core :as r]
            [reagent.dom :as rdom]
            [clojure.set :as set]
            [bh.rccst.ui-component.atom.worldwind.globe.projection :as proj]
            [bh.rccst.ui-component.atom.worldwind.globe.globe-time :as gt]
            [bh.rccst.ui-component.atom.worldwind.globe.layer :as l]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.controls :as controls]
            [bh.rccst.ui-component.atom.worldwind.globe.layer.coordinates :as coords]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.ui-component.atom.worldwind.globe.react-support")


(defn update-children [this new-children old-children]
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


(defn component-did-mount [dom-node state this]
  ;(log/info "component-did-mount" @state)

  (let [node (rdom/dom-node this)]
    ;; This will trigger a re-render of the component.
    (reset! dom-node node))

  (let [canvasId (.-id @dom-node)
        props    (r/props this)]

    ;(log/info "component-did-mount" (.-id @dom-node)
    ;  "//// props" (r/props this)
    ;  "//// children" (r/children this))

    ;Create the WorldWindow using the ID of the canvas
    (set! (.-wwd this) (WorldWind/WorldWindow. canvasId))
    (swap! state assoc :wwd (.-wwd this))

    ; Apply projection support
    (set! (.-roundGlobe this) (.-globe (.-wwd this)))

    (if (:projection props)
      (do
        ;(log/info "set-projection"  canvasId(:projection props))
        (proj/change-projection this (:projection props))))

    (doall
      (for [[idx child] (map-indexed vector (first (r/children this)))]
        (do
          ;(log/info "adding layer" idx child)
          (l/addLayer this idx child))))

    ; add the controls layer
    (if (= :max (:min-max props))
      (l/addLayer this -1 [(str canvasId " Controls") (controls/controls this (str canvasId " Controls"))]))

    ; add the coordinates layer
    (if (= :max (:min-max props))
      (l/addLayer this -1 [(str canvasId " Coordinates") (coords/coordinates this (str canvasId " Coordinates"))]))

    (if (:time props)
      (do
        ;(log/info "set-time" canvasId (:time props))
        (gt/change-time this (:id @state) (:time props))))

    ;(log/info "component-did-mount" (sort (map #(.-displayName %) (.-layers (.-wwd this)))))

    (.redraw (.-wwd this))))


(defn component-did-update [dom-node state this old-argv]
  (let [[_ new-props new-children] (r/argv this)
        [old-id old-props old-children] old-argv]

    ;(log/info "component-did-update"
    ;  "old-children" (sort (keys old-children))
      ;"//// old-props" old-props
      ;"//// new-children" (sort (keys new-children))
      ;"//// new-props" new-props)

    ;(log/info "projection"
    ;  (:projection old-props)
    ;  (:projection new-props))

    (if (not= (:projection old-props) (:projection new-props))
      (proj/change-projection this (:projection new-props)))

    (if (not= (:time old-props) (:time new-props))
      (do
        ;(log/info "update-time" (:id @state) (:time new-props))
        (gt/change-time this (:id @state) (:time new-props))))

    (update-children this new-children old-children)

    ;(log/info "component-did-update" (sort (map #(.-displayName %) (.-layers (.-wwd this)))))

    (.redraw (.-wwd this))))


