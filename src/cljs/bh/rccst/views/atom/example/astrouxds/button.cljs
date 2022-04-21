(ns bh.rccst.views.atom.example.astrouxds.button
  (:require [bh.rccst.ui-component.atom.astrouxds.button :as button]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]))

(defn- button-sm []
  [button/button :label "small" :size "small"])

(defn- button-md []
  [button/button :label "medium" :size "medium"])

(defn- button-lg []
  [button/button :label "large" :size "large"])

(defn- button-lg-icon []
  [button/button :label "large w/ icon" :size "large" :icon "settings"])

(defn example []
  (acu/demo
    "Buttons"
    [rc/h-box :src (rc/at)
     :gap "50px"
     :children [[rc/v-box :src (rc/at)
                 :align :center
                 :children [[button-sm]]]
                [rc/v-box :src (rc/at)
                 :align :center
                 :children [[button-md]]]
                [rc/v-box :src (rc/at)
                 :align :center
                 :children [[button-lg]]]
                [rc/v-box :src (rc/at)
                 :align :center
                 :children [[button-lg-icon]]]]]))

