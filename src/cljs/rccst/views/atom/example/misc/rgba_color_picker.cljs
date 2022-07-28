(ns rccst.views.atom.example.misc.rgba-color-picker
  (:require [bh.ui-component.utils.color :as color]
            [rccst.views.atom.example.utils :as utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]

            ["react-colorful" :refer [RgbaColorPicker]]))


(log/info "rccst.views.atom.example.misc.rgba-color-picker")


(def config (r/atom {:r 200 :g 150 :b 35 :a 0.5}))


(defn- set-colors [config {r "r" g "g" b "b" a "a" :as color}]
  (swap! config assoc :r r :g g :b b :a a))


(defn- config-panel [config]
  [rc/v-box :src (rc/at)
   :gap "5px"
   :children [[utils/value-slider config :r 0 255 1]
              [utils/value-slider config :g 0 255 1]
              [utils/value-slider config :b 0 255]
              [utils/value-slider config :a 0 1 0.01]
              [rc/h-box :src (rc/at)
               :gap "5px"
               :children [[rc/button :src (rc/at)
                           :label "Button"
                           :style {:background-color (color/rgba->hex @config)
                                   :color            (color/best-text-color @config)}]
                          [:div
                           {:style {:width            "100px"
                                    :text-align       :center
                                    :margin           :auto
                                    :padding          "5px"
                                    :background-color (color/hash->rgba @config)
                                    :color            (color/best-text-color-alpha @config)}}
                           "div w/alpha"]]]]])


(defn- component-panel [config]
  [:> RgbaColorPicker {:color     @config
                       :on-change #(set-colors config (js->clj %))}])


(defn example []
  (acu/demo "RGBA Color Picker"
    "Use the [react-colorful](https://github.com/omgovich/react-colorful) component to provide simple, yet powerful color picker control

This example also includes use of a support function
`bh.ui-component.utils/best-text-color-alpha` to determine the best text color to place 'over' the given
color choice. You can see this in the example `Button` shown under the text-field that shows the
actual hex-encoded string for the chosen color.


> Note: `buttons` do _NOT_ support alpha-channel colors, but `divs` do, so you may see
> a difference in the color choices between the two components.
    "
    [layout/centered {:extra-classes :width-50}
     [rc/h-box :src (rc/at)
      :gap "10px"
      :children [[config-panel config]
                 [component-panel config]]]]
    '(let [color {:r 200 :g 150 :b 35 :a 0.5}]
       [:> RgbaColorPicker {:color @color :on-change #(set-colors config (js->clj %))}])))
