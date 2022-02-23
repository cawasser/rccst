(ns bh.rccst.views.atom.example.utils
  (:require [re-com.core :as rc]))


(defn value-slider [config path min max step]
  [rc/h-box :src (rc/at)
   :gap "5px"
   :children [[rc/label :width "20px" :label (str path)]
              [rc/input-text :src (rc/at)
               :model (str (get @config path))
               :width "75px"
               :on-change #(swap! config assoc path %)]
              [rc/slider :src (rc/at)
               :model (get @config path)
               :min min :max max
               :width "100px"
               :step step
               :on-change #(swap! config assoc path %)]]])
