(ns bh.rccst.views.atom.example.astrouxds.button
  (:require [bh.rccst.ui-component.atom.astrouxds.button :as button]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]))

(defn- button-sm []
  [button/button :label "small" :size "small"])

(defn example []
  (acu/demo
    "Buttons"
    [rc/v-box :src (rc/at)
     :gap "5px"
     :children [[button-sm]]]))

    ;'[:div
    ;  [label/label :value (r/atom "This is a \"regular\" label")]
    ;  [label/label-sm :value (r/atom "This is a \"small\" label")]
    ;  [label/label-md :value (r/atom "This is a \"medium\" label")]
    ;  [label/label-lg :value (r/atom "This is a \"large\" label")]
    ;  [label/label-hg :value (r/atom "This is a \"huge\" label")]]))
