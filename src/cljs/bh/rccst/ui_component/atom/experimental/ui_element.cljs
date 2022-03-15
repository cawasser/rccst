(ns bh.rccst.ui-component.atom.experimental.ui-element

  "some components that act as stand-ins for 'real' ui-components"

  (:require [bh.rccst.ui-component.labeled-field :as lf]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            [bh.rccst.ui-component.utils :as ui-utils]))


(defn selectable-table [& {:keys [data selection]}]

  ;(log/info "selectable-table data" data "// selection" selection)

  (let [source  (re-frame/subscribe data)
        clicked (r/atom 0)]
    (fn [& {:keys [data selection component-id container-id]}]
      ;(log/info "selectable-table RENDER")
      [:div.card {:style {:width "300px" :height "200px"}}
       [:h2 {:style {:text-align :center}} "Selectable Table"]
       [:div {:style {:width "200px" :margin :auto}}
        [lf/labeled-field "data" @source]
        [:button.button {:on-click #(do
                                      (swap! clicked inc)
                                      (re-frame/dispatch (conj selection @clicked)))}
         "Click!"]]])))


(defn three-d-globe [& {:keys [layers current-time component-id container-id]}]

  ;(log/info "three-d-globe layers" layers "// current-time" current-time)

  (let [l (re-frame/subscribe layers)
        t (re-frame/subscribe current-time)]
    (fn [& {:keys [layers current-time component-id container-id]}]
      ;(log/info "three-d-globe RENDER")
      [:div.card {:style {:width "500px" :height "500px"}}
       [:h2 {:style {:text-align :center}} "3D Globe Table"]
       [:div {:style {:width "200px" :margin :auto}}
        [rc/input-textarea
         :src (rc/at)
         :model (r/atom (str @l))
         :rows 5
         :on-change #()]
        [lf/labeled-field "Current Time" @t]]])))


(defn slider [& {:keys [value range]}]

  ;(log/info "slider value" value "// range" range)

  (let [v (re-frame/subscribe value)
        r (re-frame/subscribe range)]
    (fn [& {:keys [value range]}]
      ;(log/info "slider RENDER")
      (let [[min max] @r]
        [:div.card {:style {:width "300px" :height "100px"}}
         [:h2 {:style {:text-align :center}} "Slider"]
         [rc/slider
          :src (rc/at)
          :model (str @v)
          :min min
          :max max
          :width "90%"
          :style {:margin-left :auto :margin-right :auto}
          :on-change #(do
                        (log/info "slider" (str %))
                        (re-frame/dispatch-sync (conj value %)))
          :disabled? false]
         [:div {:style {:width "200px" :margin :auto}}
          [lf/labeled-field "Value" @v]
          [lf/labeled-field "Range" @r]]]))))


(defn label [& {:keys [value]}]

  ;(log/info "label value" value)

  (let [v (re-frame/subscribe value)]
    ;(log/info "label RENDER")
    (fn [& {:keys [value]}]
      [:div.card {:style {:width "300px" :height "100px"}}
       [:h2 {:style {:text-align :center}} "Label"]
       [:div {:style {:width "200px" :margin :auto}}
        [lf/labeled-field "Value" @v]]])))



(comment
  (def data [:sources/string])

  ())

