(ns bh.rccst.views.atom.example.astrouxds.classification-marking
  (:require [bh.rccst.ui-component.atom.astrouxds.classification-marking :as cm]
            [re-com.core :as rc]
            [woolybear.ad.catalog.utils :as acu]))

(defn example []
  (acu/demo
    "Classification Markings"
    "Here we use the classification marking component to wrap some text in a classification banner."
    [rc/v-box :src (rc/at)
     :gap "5px"
     :children [[cm/classification-marking]
                [:p "Some text to be wrapped in classification banner."]
                [cm/classification-marking :level "unclassified"]]]))


