(ns bh.rccst.views.catalog.example.globe.globe-component
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [bh.rccst.views.catalog.example.globe.subs :as subs]
    [bh.rccst.views.catalog.example.globe.events :as events]
    [taoensso.timbre :as log]

    [bh.rccst.views.catalog.example.globe.sensor-data :as sd]

    [cljs-time.core :as cljs-time]
    [cljs-time.coerce :as coerce]

    [woolybear.ad.catalog.utils :as acu]
    [bh.rccst.views.catalog.example.globe.globe :as g]
    [bh.rccst.views.catalog.example.globe.cell.layer-support :as ls]

    ["@fortawesome/react-fontawesome" :refer (FontAwesomeIcon)]
    ["react-split-pane" :default SplitPane]))


(defn- time-slider [id current-time-t]
  [:div {:style {:display     :flex
                 :align-items :center}}
   ;:width       "50%"}}

   [:div.slidecontainer {:style {:width "50%" :margin-right "20px"}}
    [:input#myRange.slider
     {:style     {:width "100%"}
      :type      "range" :min "0" :max "9" :value @current-time-t
      :on-change #(do
                    ;(log/info "time-slider" (js/parseInt (-> % .-target .-value)))
                    (re-frame/dispatch-sync [::events/update-time id (js/parseInt (-> % .-target .-value))]))}]]

   [:h2 {:style {:width "50%"}}
    (str (coerce/to-date
           (cljs-time/plus ls/starting-date-time
             (cljs-time/hours (or @current-time-t 0)))))]])


(defn- projection-control [globe-id projections]
  [:div
   [:label {:for "projections"} "Projection:"]
   [:select#projections {:name      "projections"
                         :value     @projections
                         :on-change #(re-frame/dispatch
                                       [::events/set-projection globe-id (-> % .-target .-value)])}
    (doall
      (map (fn [p]
             ^{:key p} [:option {:value p} p])
        g/projections))]])


(defn- sensor-visibility-control [globe-id sensors selected-sensors colors]
  [:div {:style {:display        :flex
                 :flex-direction :row
                 :flex-wrap      :wrap}}
   (doall
     (for [s @sensors]
       ^{:key s}
       [:div {:style {:background-color (second (get colors s))
                      :border           "solid 2px"
                      :border-color     (first (get colors s))
                      :margin           "5px"
                      :padding          "5px"}}
        [:input {:type     "checkbox"
                 :value    s
                 :checked  (contains? @selected-sensors s)
                 :on-click #(do
                              ;(log/info "toggle sensor" @selected-sensors s (contains? @selected-sensors s))
                              (re-frame/dispatch-sync
                                [::events/toggle-sensor globe-id s]))}]
        [:label s]]))])


(defn- aoi-visibility-control [globe-id aois selected-aois]

  [:div {:style {:display        :flex
                 :flex-direction :row
                 :flex-wrap      :wrap}}
   (doall
     (for [[aoi attribs] @aois]
       ^{:key aoi}
       [:div {:style {:margin  "5px"
                      :padding "5px"}}
        [:input {:type     "checkbox"
                 :value    aoi
                 :checked  (contains? @selected-aois aoi)
                 :on-click #(do
                              ;(log/info "toggle aoi" @selected-aois aoi (contains? @selected-aois aoi))
                              (re-frame/dispatch-sync
                                [::events/toggle-aoi globe-id aoi]))}]
        [:span.icon-text
         [:span.icon [:> FontAwesomeIcon {:icon (first (:symbol attribs))}]]
         [:span aoi]]]))])


(defn globe [{:keys [id style] :as props}]
  (let [sensors          (re-frame/subscribe [::subs/sensor-types])
        selected-sensors (re-frame/subscribe [::subs/selected-sensors id])
        aois             (re-frame/subscribe [::subs/aois])
        selected-aois    (re-frame/subscribe [::subs/selected-aois id])
        colors           (ls/get-sensor-colors @sensors)
        base-layer       (re-frame/subscribe [::subs/base-layers id])
        sensor-layers    (re-frame/subscribe [::subs/sensor-layers id colors])
        aoi-layers       (re-frame/subscribe [::subs/aoi-layers id])
        projection       (re-frame/subscribe [::subs/projection id])
        time-t           (re-frame/subscribe [::subs/time id])]

    (re-frame/dispatch-sync [::events/init-widget id])

    (fn []
      [:div#globe-component {:style (merge {:padding      "10px" :border-width "3px"
                                            :border-style :solid :border-color :black
                                            :overflow     :hidden}
                                      style)}
       [:> SplitPane {:split       "vertical" :minSize 250
                      :defaultSize 500
                      :style       {:position "relative"}}
        [:> SplitPane {:split "horizontal" :minSize 150}
         [:div
          [:h1 id]
          [time-slider id time-t]]

         [:div
          [sensor-visibility-control id sensors selected-sensors colors]
          [aoi-visibility-control id aois selected-aois]]]


        [:div {:style {:width "100%" :height "100%"}}
         [projection-control id projection]
         [g/globe {:id         id
                   :min-max    :max
                   :time       (coerce/to-date
                                 (cljs-time/plus ls/starting-date-time
                                   (cljs-time/hours (or @time-t 0))))
                   :projection (or @projection "3D")
                   :style      {:background-color :black
                                :width            "100%"
                                :height           "100%"}}
          (merge @base-layer @aoi-layers @sensor-layers)]]]])))

(defn example []
      ;(acu/demo "Nasa Worldwind Globe"
        [g/globe {:id "globe-1"
                  :style {:width "100%" :height "100%"}}])



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; RICH COMMENTS
;


; setup the sensor secetion contorls ot have the correct values
(comment
  (do
    (def globe-id "globe-1")
    (def sensors (re-frame/subscribe [::subs/sensor-types]))
    (def selected-sensors (re-frame/subscribe [::subs/selected-sensors globe-id])))

  (map (fn [s] (contains? @selected-sensors s)) @sensors)

  ())


; set up the colors for the sensors
(comment
  (def sensors (re-frame/subscribe [::subs/sensor-types]))
  (def colors (ls/get-sensor-colors @sensors))

  (def sensor-allocs @(re-frame/subscribe [::subs/sensor-allocations]))


  ())


; try updating the app-db and seeing if the globe updates
(comment
  (rf/dispatch-sync [:globe-cljs.events/add-base-layer "my-first-globe"
                     "Compass" (globe.worldwind.layer.compass/compass "Compass")])
  (rf/dispatch-sync [:globe-cljs.events/remove-base-layer "my-first-globe" "Compass"])

  (rf/dispatch-sync [:globe-cljs.events/add-base-layer "my-first-globe"
                     "Tesselation" (globe.worldwind.layer.tesselation/tesselation "Tesselation")])
  (rf/dispatch-sync [:globe-cljs.events/remove-base-layer "my-first-globe" "Tesselation"])

  (rf/dispatch-sync [:globe-cljs.events/add-base-layer "my-first-globe"
                     "Blue Marble" (globe.worldwind.layer.blue-marble/blue-marble "Blue Marble")])
  (rf/dispatch-sync [:globe-cljs.events/remove-base-layer "my-first-globe" "Blue Marble"])

  ())


; now we can try a more complicated layer: renderable with polygons!
(comment
  (do
    (require '[globe-cljs.layer.renderable :as rl])
    (require '[globe-cljs.surface.polygon :as poly])
    (def children [(poly/polygon [0 0] {:color [255 0 0 1]})
                   (poly/polygon [0 1] {:color [0 255 0 1]})
                   (poly/polygon [1 0] {:color [0 0 255 1]})])
    (def layer (rl/createLayer "polygons" children)))

  (count (.-renderables layer))

  (rf/dispatch-sync [:globe-cljs.events/add-layer "my-first-globe"
                     "polygons" (rl/createLayer
                                  "polygons"
                                  [(poly/polygon [0 0] {:color [255 0 0 1]})
                                   (poly/polygon [8 0] {:color [255 0 0 1]})
                                   (poly/polygon [0 1] {:color [0 255 0 1]})
                                   (poly/polygon [1 0] {:color [0 0 255 1]})])])
  (rf/dispatch-sync [:globe-cljs.events/remove-layer "my-first-globe" "polygons"])
  @re-frame.db/app-db

  ; now some other polygons
  (rf/dispatch-sync [:globe-cljs.events/add-layer
                     "poly-2" (rl/createLayer "poly-2"
                                [(poly/polygon [5 5]
                                   {:color [128 128 0 1]})])])
  (rf/dispatch-sync [:globe-cljs.events/remove-layer "poly-2"])

  ())


; work out how to make the time-slider change the day/night viz
(comment
  (def time-t (atom nil))
  (cljs-time/plus ls/starting-date-time (cljs-time/hours (or @time-t 0)))

  (cljs-time/to-long ls/starting-date-time)
  (js/Date.)

  (inst/with-out-str ls/starting-date-time)

  (= (with-out-str (pr ls/starting-date-time))
    "#inst \"2017-01-01\"")

  (coerce/to-date ls/starting-date-time)

  (coerce/to-date
    (cljs-time/plus ls/starting-date-time
      (cljs-time/hours (or @time-t 0))))

  ())


; can we do (consistent) renderables with surface-image?
(comment


  ())


; lets get crazy with the nodes
(comment
  (re-frame/dispatch [::events/add-element {:id       "500"
                                            :el-type  :node
                                            :data     {:label (reagent/as-element [globe "globe-2"])}
                                            :position {:x 300 :y 125}}])


  ())


(comment
  (def id "globe-1")
  (def x 100)

  (re-frame/dispatch-sync [::events/toggle-aoi id "alpha-hd"])

  ())