(ns bh.rccst.views.catalog.example.simple-image
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.images :as images]
            [re-com.core :as rc]))


(defn example []
  (acu/demo "Simple Image"
            "See https://bulma.io/documentation/elements/image/ for handy Bulma classes you
            can apply to images to set the placeholder size."
            [layout/padded
             [images/image {:src           "/imgs/hammer-icon-16x16.png"
                            :extra-classes #{:is-4by1 :adc-width-20}}]
             [rc/v-box
              :gap "5px"
              :children []]]

            '[layout/padded
              [images/image {:src           "/imgs/hammer-icon-16x16.png"
                             :extra-classes #{:is-4by1 :adc-width-20}}]]))

