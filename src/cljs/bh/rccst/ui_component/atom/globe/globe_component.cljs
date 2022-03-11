(ns bh.rccst.ui-component.atom.globe.globe-component
  (:require
    [reagent.core :as reagent]
    [re-frame.core :as re-frame]
    [bh.rccst.ui-component.atom.globe.subs :as subs]
    [bh.rccst.ui-component.atom.globe.events :as events]
    [taoensso.timbre :as log]

    [cljs-time.core :as cljs-time]
    [cljs-time.coerce :as coerce]

    [bh.rccst.ui-component.atom.globe.globe :as g]
    [bh.rccst.ui-component.atom.globe.cell.layer-support :as ls]

    ["@fortawesome/react-fontawesome" :refer (FontAwesomeIcon)]
    ["react-split-pane" :default SplitPane]))


(defn- time-slider [id current-time-t]
  [:div.control {:style {:display     :flex
                         :position :relative}}


     [:div.textdate {:style {:position :absolute :top "60px" :z-index "2"}}
       [:h2 {:style {:width "100%" :text-align :center}}
        (str (coerce/to-date
               (cljs-time/plus ls/starting-date-time
                 (cljs-time/hours (or @current-time-t 0)))))]]])


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




(defn globe [{:keys [id style] :as props}]
  (let [base-layer       (re-frame/subscribe [::subs/base-layers id])
        projection       (re-frame/subscribe [::subs/projection id])
        time-t           (re-frame/subscribe [::subs/time id])]

    (re-frame/dispatch-sync [::events/init-widget id])

    (fn []
      [:div#globe-component {:style (merge {:padding      "10px" :border-width "3px"
                                            :border-style :solid :border-color :black
                                            :overflow     :hidden}
                                      style)}
       ;[:> SplitPane {:split       "horizontal"
       ;               :minSize 100
       ;               :defaultSize 400
       ;               :style       {:position "relative"}}

        [:div {:style {:width "100%" :height "80%"}}
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
          (merge @base-layer)]]
        [:div
         [:br]
         [time-slider id time-t]]])))

(defn example []
      [globe {:id "globe-1"
                :style {:width "100%" :height "700px"}}])
