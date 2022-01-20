(ns bh.rccst.views.catalog.example.progress-bar
  (:require [reagent.core :as r]
            [woolybear.ad.catalog.utils :as acu]
            [re-com.core :as core]
            [re-com.progress-bar :as pb]))

(def progress (r/atom 75))
(def striped? (r/atom false))

(defn example []
      (acu/demo "Progress Bar"
                [core/v-box :src (core/at)
                 :gap "20px"
                 :children [[core/h-box :src (core/at)
                             :justify :between
                             :children [[core/v-box :src (core/at)
                                         :align-self :center
                                         :children [[pb/progress-bar :src (core/at)
                                                     :model progress
                                                     :width "400px"
                                                     :striped? @striped?]]]
                                        [core/v-box :src (core/at)
                                         :gap "10px"
                                         :style {:min-width        "200px"
                                                 :padding          "15px"
                                                 :border-top       "1px solid #DDD"
                                                 :background-color "#f7f7f7"}
                                         :children [
                                                    [core/title :src (core/at) :level :level3 :label "Interactive Parameters" :style {:margin-top "0"}]
                                                    [core/h-box :src (core/at)
                                                     :gap "10px"
                                                     :children [[core/box :src (core/at) :align :start :child [:code ":model"]]
                                                                [core/slider :src (core/at)
                                                                 :model progress
                                                                 :min 0
                                                                 :max 100
                                                                 :width "200px"
                                                                 :on-change #(reset! progress %)]
                                                                [core/label :src (core/at) :label @progress]]]
                                                    [core/checkbox :src (core/at)
                                                     :label [core/box :src (core/at) :align :start :child [:code ":striped?"]]
                                                     :model striped?
                                                     :on-change #(reset! striped? %)]]]]]]]

                '[core/v-box :src (core/at)
                  :gap "20px"
                  :children [[pb/progress-bar :src (core/at)
                              :model progress
                              :width "350px"
                              :striped? @striped?]
                             [core/v-box :src (core/at)
                              :gap "10px"
                              :style {:min-width        "150px"
                                      :padding          "15px"
                                      :border-top       "1px solid #DDD"
                                      :background-color "#f7f7f7"}
                              :children [
                                         [core/title :src (core/at) :level :level3 :label "Interactive Parameters" :style {:margin-top "0"}]
                                         [core/h-box :src (core/at)
                                          :gap "10px"
                                          :children [[core/box :src (core/at) :align :start :child [:code ":model"]]
                                                     [core/slider :src (core/at)
                                                      :model progress
                                                      :min 0
                                                      :max 100
                                                      :width "200px"
                                                      :on-change #(reset! progress %)]
                                                     [core/label :src (core/at) :label @progress]]]
                                         [core/checkbox :src (core/at)
                                          :label [core/box :src (core/at) :align :start :child [:code ":striped?"]]
                                          :model striped?
                                          :on-change #(reset! striped? %)]]]]]))
