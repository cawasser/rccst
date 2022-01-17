(ns  bh.rccst.views.catalog.example.radio-button
  (:require [woolybear.ad.catalog.utils :as acu]
            [reagent.ratom :as ratom]
            [re-com.core   :refer [at v-box gap checkbox title radio-button p]]))


(defn example []
      (let [disabled?   (ratom/atom false)
            color (ratom/atom "green")]
           (fn []
               (acu/demo "Radio Button"
                           [v-box :src (at)
                            :gap      "10px"
                            :children [(doall (for [c ["red" "green" "blue"]]
                                                   ^{:key c}
                                                   [radio-button :src (at)
                                                    :disabled? disabled?
                                                    :label       c
                                                    :value       c
                                                    :model       color
                                                    :label-style (if (= c @color) {:color       c
                                                                                   :font-weight "bold"})
                                                    :on-change   #(reset! color %)]))
                                       [v-box :src (at)
                                        :gap "10px"
                                        :style {:min-width        "150px"
                                                :padding          "15px"
                                                :border-top       "1px solid #DDD"
                                                :background-color "#f7f7f7"}
                                        :children [[title :src (at) :level :level3 :label "Interactive Parameters" :style {:margin-top "0"}]
                                                   [checkbox :src (at)
                                                    :label [:code ":disabled?"]
                                                    :model disabled?
                                                    :on-change (fn [val]
                                                                   (reset! disabled? val))]]]]]))))
