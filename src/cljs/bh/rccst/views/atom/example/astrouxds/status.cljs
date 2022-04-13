(ns bh.rccst.views.atom.example.astrouxds.status
  (:require [bh.rccst.ui-component.atom.astrouxds.status :as status]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]))

(defn example []
  (acu/demo
    "Status symbols"
    "Here we show the symbols used for displaying system status."
    [rc/h-box :src (rc/at)
     :gap "50px"
     :children [[rc/v-box :src (rc/at)
                 :align :center
                 :children [[:p "Off"]
                            [status/status "off"]]]
                [rc/v-box :src (rc/at)
                 :align :center
                 :children [[:p "Standby"]
                            [status/status "standby"]]]
                [rc/v-box :src (rc/at)
                 :align :center
                 :children [[:p "Normal"]
                            [status/status "normal"]]]
                [rc/v-box :src (rc/at)
                 :align :center
                 :children [[:p "Caution"]
                            [status/status "caution"]]]
                [rc/v-box :src (rc/at)
                 :align :center
                 :children [[:p "Serious"]
                            [status/status "serious"]]]
                [rc/v-box :src (rc/at)
                 :align :center
                 :children [[:p "Critical"]
                            [status/status "critical"]]]]]))




