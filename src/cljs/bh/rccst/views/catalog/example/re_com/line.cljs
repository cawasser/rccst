(ns bh.rccst.views.catalog.example.re-com.line
  (:require [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as acu]
            [re-com.core :as rc]))


(defn example []
  (acu/demo "Line"
    "You can use lines to separate parts of the UI. You can control the width and color. And they
follow the `re-com/h-box` and `re-com/v-box` container styling. Use `:gap` to set the whitespace.

See [Re-com/line](https://re-com.day8.com.au/#/line)"

    [layout/centered {:extra-classes :width-50}
     [rc/v-box
      :gap      "40px"
      :children [[rc/h-box
                  :gap      "20px"
                  :children [[rc/label :label "horizontal"]
                             [rc/line :src (rc/at) :size "2px" :color "black"]
                             [rc/label :label "inside an h-box"]]]

                 [rc/v-box
                  :gap      "10px"
                  :children [[rc/label :label "or vertical"]
                             [rc/line :src (rc/at) :size "2px" :color "red"]
                             [rc/label :label "inside a v-box"]]]]]]

    '[layout/centered {:extra-classes :width-50}
      [rc/v-box
       :gap      "40px"
       :children [[rc/h-box
                   :gap      "20px"
                   :children [[rc/label :label "horizontal"]
                              [rc/line :src (rc/at) :size "2px" :color "black"]
                              [rc/label :label "inside an h-box"]]]

                  [rc/v-box
                   :gap      "10px"
                   :children [[rc/label :label "or vertical"]
                              [rc/line :src (rc/at) :size "2px" :color "red"]
                              [rc/label :label "inside a v-box"]]]]]]))



