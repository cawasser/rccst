(ns bh.rccst.views.atom.example.astrouxds.radio-button
  (:require [bh.rccst.ui-component.atom.astrouxds.radio-button :as rb]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]))

(defn example []
  (acu/demo
    "Radio Button"
    [rc/h-box :src (rc/at)
     :gap "50px"
     :children [[rb/radio-button ["One" "Two" "Three"]]]]))

