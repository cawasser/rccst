(ns  bh.rccst.views.catalog.example.checkbox-re-com
  (:require [woolybear.ad.catalog.utils :as acu]
            [reagent.ratom :as ratom]
            [re-com.core     :refer [at checkbox]]))


(defn example []
      (let [clicked?      (ratom/atom false)]
           (acu/demo "Checkbox"
                     [checkbox
                      :src       (at)
                      :label     "click me  "
                      :model     clicked?
                      :on-change #(reset! clicked? %)])))