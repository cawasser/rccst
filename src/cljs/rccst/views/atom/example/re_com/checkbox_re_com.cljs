(ns rccst.views.atom.example.re-com.checkbox-re-com
  (:require [re-com.core :as rc]
            [reagent.ratom :as ratom]
            [woolybear.ad.catalog.utils :as acu]))


(defn example []
  (let [clicked? (ratom/atom false)]
    (acu/demo "Checkbox"
      [rc/checkbox
       :src (rc/at)
       :label "click me"
       :model clicked?
       :on-change #(reset! clicked? %)])))
