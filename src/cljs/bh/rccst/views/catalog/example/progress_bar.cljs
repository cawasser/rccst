(ns bh.rccst.views.catalog.example.progress-bar
  (:require [reagent.core :as r]
            [woolybear.ad.catalog.utils :as acu]
            [re-com.core :as rc]))


(def progress (r/atom 75))
(def striped? (r/atom false))


(defn config-panel []
  [rc/v-box :src (rc/at)
   :gap "10px"
   :style {:min-width        "200px"
           :padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [[rc/h-box :src (rc/at)
               :gap "10px"
               :children [[rc/box :src (rc/at) :align :start :child [:code ":model"]]
                          [rc/slider :src (rc/at)
                           :model progress
                           :min 0
                           :max 100
                           :width "200px"
                           :on-change #(reset! progress %)]
                          [rc/label :src (rc/at) :label @progress]]]
              [rc/checkbox :src (rc/at)
               :label [rc/box :src (rc/at) :align :start :child [:code ":striped?"]]
               :model striped?
               :on-change #(reset! striped? %)]]])


(defn- component-panel []
  [rc/v-box :src (rc/at)
   :align-self :center
   :children [[rc/progress-bar :src (rc/at)
               :model progress
               :width "400px"
               :striped? @striped?]]])


(defn example []
      (acu/demo "Progress Bar"
                [rc/v-box :src (rc/at)
                 :gap "20px"
                 :children [[rc/h-box :src (rc/at)
                             :justify :between
                             :children [[config-panel]
                                        [component-panel]]]]]

                '(let [striped? (reagent/atom false)
                       progress (reagent/atom 75)]
                   [re-com/progress-bar :src (re-com/at)
                    :model progress
                    :width "350px"
                    :striped? @striped?])))

