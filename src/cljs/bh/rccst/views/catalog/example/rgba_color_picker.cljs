(ns bh.rccst.views.catalog.example.rgba-color-picker
  (:require [taoensso.timbre :as log]
            [reagent.core :as r]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as acu]
            [re-com.core :as rc]

            ["react-colorful" :refer [RgbaColorPicker]]))





(def config (r/atom {:r 200 :g 150 :b 35 :a 0.5}))


(defn- set-colors [config {r "r" g "g" b "b" a "a" :as color}]
  (swap! config assoc :r r :g g :b b :a a))


(defn- value-slider [config path min max step]
  [rc/h-box :src (rc/at)
   :gap "5px"
   :children [[rc/label :width "20px" :label (str path)]
              [rc/input-text :src (rc/at)
               :model (str (get @config path))
               :width "50px"
               :on-change #(swap! config assoc path %)]
              [rc/slider :src (rc/at)
               :model (get @config path)
               :min min :max max
               :width "100px"
               :step step
               :on-change #(swap! config assoc path %)]]])


(defn- config-panel [config]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[value-slider config :r 0 255 1]
              [value-slider config :g 0 255 1]
              [value-slider config :b 0 255]
              [value-slider config :a 0 1 0.01]]])


(defn- component-panel [config]
  [:> RgbaColorPicker {:color     @config
                       :on-change #(set-colors config (js->clj %))}])


(defn example []
  (acu/demo "RGBA Color Picker"
    "Use the [react-colorful](https://github.com/omgovich/react-colorful) component to provide simple, yet powerful color picker control"
    [layout/centered {:extra-classes :width-50}
     [rc/h-box :src (rc/at)
      :gap "10px"
      :children [[config-panel config]
                 [component-panel config]]]]
    '(let [color {:r 200 :g 150 :b 35 :a 0.5}]
       [:> RgbaColorPicker {:color @color :on-change #(set-colors config (js->clj %))}])))