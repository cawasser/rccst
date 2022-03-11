(ns bh.rccst.ui-component.atom.experimental.ui-element

  "some components that act as stand-ins for 'real' ui-components"

  (:require [bh.rccst.ui-component.labeled-field :as lf]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [re-com.core :as rc]
            [taoensso.timbre :as log]
            [bh.rccst.ui-component.utils :as ui-utils]))


(defn selectable-table [& {:keys [data selection]}]

  (log/info "selectable-table data" data "// selection" selection)

  (let [source  (re-frame/subscribe data)
        clicked (r/atom 0)]
    (fn [& {:keys [data selection component-id container-id]}]
      [:div.card {:style {:width "300px" :height "200px"}}
       [:h2 "Selectable Table"]
       [lf/labeled-field "data" @source]
       [:button.button {:on-click #(do
                                     (swap! clicked inc)
                                     (re-frame/dispatch (conj selection :sub-path @clicked)))}
        "Click!"]])))


(defn three-d-globe [& {:keys [layers current-time component-id container-id]}]

  (log/info "three-d-globe layers" layers "// current-time" current-time)

  (let [l (re-frame/subscribe layers)
        t (re-frame/subscribe current-time)]
    (fn [& {:keys [layers current-time component-id container-id]}]
      [:div.card {:style {:width "500px" :height "500px"}}
       [:h2 "3D Globe Table"]
       [lf/labeled-field "Layers" @l]
       [lf/labeled-field "Current Time" @t]])))


(defn slider [& {:keys [value range]}]

  (log/info "slider value" value "// range" range)

  (let [v (re-frame/subscribe value)
        r (re-frame/subscribe range)]
    (fn [& {:keys [value range]}]
      (let [[min max] @r]
        [:div.card {:style {:width "300px" :height "100px"}}
         [:h2 "Slider"]
         [rc/slider
          :src (rc/at)
          :model (r/atom @v)
          :min min
          :max max
          :width "200px"
          :on-change #(do
                        (log/info "slider" (str %) "//")
                        (re-frame/dispatch-sync (conj value %)))
          :disabled? false]
         [lf/labeled-field "Value" @v]
         [lf/labeled-field "Range" @r]]))))



(comment

  @(re-frame/subscribe [:coverage-plan-demo.component.blackboard.topic.current-time])

  (re-frame/dispatch-sync [:coverage-plan-demo.component.blackboard.topic.current-time 45])


  ())

(defn label [& {:keys [value]}]

  (log/info "label value" value)

  (let [v (re-frame/subscribe value)]
    (fn [& {:keys [value]}]
      [:div.card {:style {:width "300px" :height "100px"}}
       [:h2 "Label"]
       [lf/labeled-field "Value" @v]])))



(comment
  (def data [:sources/string])

  ())

