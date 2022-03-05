(ns bh.rccst.ui-component.atom.experimental.ui-element

  "some components that act as stand-ins for 'real' ui-components"

  (:require [bh.rccst.ui-component.labeled-field :as lf]
            [re-frame.core :as re-frame]
            [reagent.core :as r]))


(defn selectable-table [& {:keys [data selection component-id container-id]}]
  (let [source  (re-frame/subscribe data)
        clicked (r/atom 0)]
    (fn [& {:keys [data selection component-id container-id]}]
      [:div.card {:style {:width "100px" :height "100px"}}
       [:h2 "Selectable Table"]
       [lf/labeled-field "data" @source]
       [:button.button {:on-click #(do
                                     (swap! clicked inc)
                                     (re-frame/dispatch (conj selection @clicked)))}]])))


(defn three-d-globe [& {:keys [layers current-time component-id container-id]}]
  (let [l (re-frame/subscribe layers)
        t (re-frame/subscribe current-time)]
    (fn [& {:keys [layers current-time component-id container-id]}]
      [:div.card {:style {:width "100px" :height "100px"}}
       [:h2 "3D Globe Table"]
       [lf/labeled-field "Layers" @l]
       [lf/labeled-field "Current Time" @t]])))


(defn slider [& {:keys [value range component-id container-id]}]
  (let [v (re-frame/subscribe value)
        r (re-frame/subscribe range)]
    (fn [& {:keys [value range component-id container-id]}]
      [:div.card {:style {:width "100px" :height "100px"}}
       [:h2 "3D Globe Table"]
       [lf/labeled-field "Value" @v]
       [lf/labeled-field "Range" @r]])))


(defn label [& {:keys [value component-id container-id]}]
  (let [v (re-frame/subscribe value)]
    (fn [& {:keys [value component-id container-id]}]
      [:div.card {:style {:width "100px" :height "100px"}}
       [:h2 "3D Globe Table"]
       [lf/labeled-field "Value" @v]])))


(comment
  (def data [:sources/string])

  ())

