(ns bh.rccst.views.catalog.example.throbber
  (:require [reagent.core :as r]
            [woolybear.ad.catalog.utils :as acu]
            [re-com.core :as core]
            [re-com.throbber :as throb]))

(defn example []
      (acu/demo "Throbber"

                [core/h-box :src (core/at)
                 :gap "50px"
                 :children [[core/v-box :src (core/at)
                             :align :center
                             :children [[core/box :src (core/at) :align :start :child [:code ":smaller"]]
                                        [throb/throbber :src (core/at)
                                         :size :smaller
                                         :color "green"]]]
                            [core/v-box :src (core/at)
                             :align :center
                             :children [[core/box :src (core/at) :align :start :child [:code ":small"]]
                                        [throb/throbber :src (core/at)
                                         :size  :small
                                         :color "red"]]]
                            [core/v-box :src (core/at)
                             :align :center
                             :children [[core/box :src (core/at) :align :start :child [:code ":regular"]]
                                        [throb/throbber :src (core/at)]]]
                            [core/v-box :src (core/at)
                             :align :center
                             :children [[core/box :src (core/at) :align :start :child [:code ":large"]]
                                        [throb/throbber :src (core/at)
                                         :size  :large
                                         :color "blue"]]]]]))