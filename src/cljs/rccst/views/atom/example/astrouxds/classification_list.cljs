(ns rccst.views.atom.example.astrouxds.classification-list
  (:require [bh.ui-component.atom.astrouxds.classification-marking :as cm]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]))

(defn example []
  (acu/demo
    "Classification Markings List"
    "Here we list all the classification levels provided by AstroUX."
    [rc/v-box :src (rc/at)
     :gap "5px"
     :children [[cm/classification-marking :level "unclassified"]
                [cm/classification-marking :level "cui"]
                [cm/classification-marking :level "controlled"]
                [cm/classification-marking :level "confidential"]
                [cm/classification-marking :level "secret"]
                [cm/classification-marking :level "top-secret"]
                [cm/classification-marking :level "top-secret-sci"]]]))


