(ns bh.rccst.views.catalog.example.misc.hex-color-picker
  (:require [reagent.core :as r]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as acu]
            [re-com.core :as rc]

            [bh.rccst.ui-component.utils :as ui-utils]

            ["react-colorful" :refer [HexColorPicker]]))


(def config (r/atom "#aabbcc"))


(defn- config-panel [config]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[rc/input-text :src (rc/at)
               :model config
               :on-change #(reset! config %)]

              [rc/button :src (rc/at)
               :label "Button"
               :style {:background-color @config
                       :color (ui-utils/best-text-color
                                (ui-utils/hex->rgba @config))}]]])


(defn- component-panel [config]
  [:> HexColorPicker {:color @config
                      :on-change #(reset! config %)}])


(defn example []
  (acu/demo "Hex Color Picker"
    "Use the [react-colorful](https://github.com/omgovich/react-colorful) component to provide simple, yet powerful color picker control.

This example also includes use of a support function
`bh.rccst.ui-component.utils/best-text-color` to determine the best text color to place 'over' the given
color choice. You can see this in the example `Button` and`div` shown under the text-field that shows the
actual hex-encoded string for the chosen color.
    "
    [layout/centered {:extra-classes :width-50}
     [rc/h-box :src (rc/at)
      :gap "10px"
      :children [[config-panel config]
                 [component-panel config]]]]
    '(let [color (r/atom "#aabbcc")]
       [:> HexColorPicker {:color @color :on-change #(reset! color %)}])))