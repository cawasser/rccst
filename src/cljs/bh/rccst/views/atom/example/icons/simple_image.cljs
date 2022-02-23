(ns bh.rccst.views.atom.example.icons.simple-image
  (:require [bh.rccst.ui-component.markdown :as markdown]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.images :as images]
            [woolybear.ad.layout :as layout]))


(defn demo-wrapper [image-size]
  [rc/v-box :src (rc/at)
   :gap "15px"
   :align :center
   :children [[markdown/markdown (str "`" image-size "`")]
              [images/image {:src           "/imgs/hammer-icon-16x16.png"
                             :extra-classes #{:is-4by1 image-size}}]]])


(defn example []
  (acu/demo "Simple Image"
    "See [Bulma/image](https://bulma.io/documentation/elements/image/) for handy Bulma classes you
  can apply to images to set the placeholder size."

    [layout/centered
     [rc/h-box :src (rc/at)
      :gap "20px"
      :children [[demo-wrapper :is-16x16]
                 [demo-wrapper :is-24x24]
                 [demo-wrapper :is-32x32]
                 [demo-wrapper :is-48x48]
                 [demo-wrapper :is-64x64]
                 [demo-wrapper :is-96x96]
                 [demo-wrapper :is-128x128]]]]

    '[layout/centered
      [rc/h-box :src (rc/at)
       :gap "20px"
       :children [[rc/v-box :src (rc/at)
                   :gap "15px"
                   :align :center
                   :children [[markdown/markdown (str "`" :is-32x32 "`")]
                              [images/image {:src           "/imgs/hammer-icon-16x16.png"
                                             :extra-classes #{:is-4by1 :is-32x32}}]]]]]]))

