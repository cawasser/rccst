(ns bh.rccst.views.atom.example.astrouxds.radio-button
  (:require [bh.rccst.ui-component.atom.astrouxds.radio-button :as rb]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]))

(defn example []
  (acu/demo
    "Radio Button"
    [rc/h-box :src (rc/at)
     :gap "50px"
     :children [[rc/h-box :src (rc/at)
                 :align :center
                 :children [[:p "One"] [rb/radio-button :name "radios" :value "one"]]]
                [rc/h-box :src (rc/at)
                 :align :center
                 :children [[:p "Two"] [rb/radio-button :name "radios" :value "two"]]]
                [rc/h-box :src (rc/at)
                 :align :center
                 :children [[:p "Three"] [rb/radio-button :name "radios" :value "three"]]]]]))

