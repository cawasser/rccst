(ns rccst.views.atom.example.re-com.radio-button
  (:require [re-com.core :as rc]
            [reagent.ratom :as ratom]
            [woolybear.ad.catalog.utils :as acu]))


(defn component [disabled? color]
  (doall (for [c ["red" "green" "blue"]]
           ^{:key c}
           [rc/radio-button :src (rc/at)
            :disabled? disabled?
            :label c
            :value c
            :model color
            :label-style (if (= c @color) {:color       c
                                           :font-weight "bold"})
            :on-change #(reset! color %)])))


(defn config-panel [disabled?]
  [rc/v-box :src (rc/at)
   :gap "10px"
   :style {:min-width        "150px"
           :padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[rc/title :src (rc/at) :level :level3 :label "Interactive Parameters" :style {:margin-top "0"}]
              [rc/checkbox :src (rc/at)
               :label [:code ":disabled?"]
               :model disabled?
               :on-change (fn [val]
                            (reset! disabled? val))]]])


(def sample-data
  {:disabled? false
   :color     "green"})


(defn example []
  (let [disabled? (ratom/atom false)
        color (ratom/atom "green")]
    (fn []
      (acu/demo "Radio Button"
        [rc/h-box :src (rc/at)
         :gap "10px"
         :children [(config-panel disabled?)
                    (component disabled? color)]]))))

