(ns bh.rccst.views.catalog.example.color-picker
  (:require [reagent.core :as r]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as acu]
            [re-com.core :as rc]

            ["react-colorful" :refer [HexColorPicker]]))


(def config (r/atom "#aabbcc"))


(defn- config-panel [config]
  [rc/input-text :src (rc/at)
   :model config
   :on-change #(reset! config %)])


(defn- component-panel [config]
  [:> HexColorPicker {:color @config
                      :on-change #(reset! config %)}])


(defn example []
  (acu/demo "Color Picker"
    "Use the [react-colors]() component to provide simple, yet powerful color picker control"
    [layout/centered {:extra-classes :width-50}
     [rc/h-box :src (rc/at)
      :gap "10px"
      :children [[config-panel config]
                 [component-panel config]]]]
    '(let [color (r/atom "#aabbcc")]
       [:> HexColorPicker {:color @color :on-change #(reset! color %)}])))