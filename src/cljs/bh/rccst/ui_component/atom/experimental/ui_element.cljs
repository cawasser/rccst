(ns bh.rccst.ui-component.atom.experimental.ui-element

  "some components that act as stand-ins for 'real' ui-components"

  (:require [bh.rccst.ui-component.labeled-field :as lf]
            [re-frame.core :as re-frame]
            [reagent.core :as r]))


(defn selectable-table [& {:keys [data selection]}]

  (let [source  (re-frame/subscribe data)
        clicked (r/atom 0)]
    (fn [& {:keys [data selection component-id container-id]}]
      [:div.card {:style {:width "100px" :height "100px"}}
       [:h2 "Selectable Table"]
       [lf/labeled-field "data" @source]
       [:button.button {:on-click #(do
                                     (swap! clicked inc)
                                     (re-frame/dispatch (conj selection :sub-path @clicked)))}]])))


(def selection [:update])

(conj selection :new-val)
(re-frame/dispatch [:update :new-val])

(defn three-d-globe [& {:keys [layers current-time component-id container-id]}]
  (let [l (re-frame/subscribe layers)
        t (re-frame/subscribe current-time)]
    (fn [& {:keys [layers current-time component-id container-id]}]
      [:div.card {:style {:width "100px" :height "100px"}}
       [:h2 "3D Globe Table"]
       [lf/labeled-field "Layers" @l]
       [lf/labeled-field "Current Time" @t]])))


(defn slider [& {:keys [value range]}]
  (let [v (re-frame/subscribe value)
        r (re-frame/subscribe range)]
    (fn [& {:keys [value range]}]
      [:div.card {:style {:width "100px" :height "100px"}}
       [:h2 "Slider"]
       [lf/labeled-field "Value" @v]
       [lf/labeled-field "Range" @r]])))


(defn label [& {:keys [value]}]
  (let [v (re-frame/subscribe value)]

    (fn [& {:keys [value]}]

      [:div.card {:style {:width "100px" :height "100px"}}
       [:h2 "Label"]
       [lf/labeled-field "Value" @v]])))


(def meta-data {:component label
                :ports     {:value :port/sink}})

(comment
  (def data [:sources/string])

  ())

