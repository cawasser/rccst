(ns bh.rccst.views.catalog.example.input-time
  (:require [reagent.core :as r]
            [woolybear.ad.catalog.utils :as acu]
            [re-com.core :as core]
            [re-com.input-time :as time]))

(defn- simulated-bools
       [disabled? hide-border? show-icon?]
       [core/v-box :src (core/at)
        :gap "20px"
        :align :start
        :children [[core/h-box :src (core/at)
                    :gap "15px"
                    :align :start
                    :children [[core/checkbox :src (core/at)
                                :label [core/box :src (core/at) :align :start :child [:code ":disabled?"]]
                                :model @disabled?
                                :on-change #(reset! disabled? %)]
                               [core/checkbox :src (core/at)
                                :label [core/box :src (core/at) :align :start :child [:code ":hide-border?"]]
                                :model @hide-border?
                                :on-change #(reset! hide-border? %)]
                               [core/checkbox :src (core/at)
                                :label [core/box :src (core/at) :align :start :child [:code ":show-icon?"]]
                                :model @show-icon?
                                :on-change #(reset! show-icon? %)]]]]])

(defn basics-demo
      []
      (let [disabled?    (r/atom false)
            hide-border? (r/atom false)
            show-icon?   (r/atom true)
            an-int-time  (r/atom 900)                      ;; start at 9am
            init-minimum 0
            minimum      (r/atom init-minimum)
            init-maximum 2359
            maximum      (r/atom init-maximum)]
           (fn []
               [core/v-box :src (core/at)
                :gap "10px"
                :children [[core/v-box :src (core/at)
                            :children [[core/v-box :src (core/at)
                                        :width    "140px"
                                        :gap      "30px"
                                        :children [[time/input-time :src (core/at)
                                                    :model        an-int-time
                                                    :minimum      @minimum
                                                    :maximum      @maximum
                                                    :on-change    #(reset! an-int-time %)
                                                    :disabled?    disabled?
                                                    :hide-border? @hide-border?
                                                    :show-icon?   @show-icon?]]]
                                       [core/gap :src (core/at) :size "30px"]
                                       [core/v-box :src (core/at)
                                        :gap      "10px"
                                        :style {:min-width        "550px"
                                                :padding          "15px"
                                                :border-top       "1px solid #DDD"
                                                :background-color "#f7f7f7"}
                                        :children [[core/title :src (core/at) :level :level3 :label "Interactive Parameters" :style {:margin-top "0"}]
                                                   [simulated-bools disabled? hide-border? show-icon?]
                                                   [core/gap :src (core/at) :size "20px"]
                                                   [core/title :src (core/at) :level :level3 :label "Model resets"]
                                                   [core/h-box :src (core/at)
                                                    :gap "10px"
                                                    :align :center
                                                    :children [[core/button :src (core/at)
                                                                :label    "11am"
                                                                :class    "btn btn-default"
                                                                :on-click #(reset! an-int-time 1100)]
                                                               [core/button :src (core/at)
                                                                :label    "5pm"
                                                                :class    "btn btn-default"
                                                                :on-click #(reset! an-int-time 1700)]]]
                                                   [core/gap :src (core/at) :size "20px"]
                                                   [core/title :src (core/at) :level :level3 :label "Simulated minimum & maximum changes"]
                                                   [core/h-box :src (core/at)
                                                    :gap      "10px"
                                                    :align    :center
                                                    :children [[core/label :src (core/at) :label ":minimum"]
                                                               [core/label :src (core/at) :label @minimum :style {:width "40px" :font-size "11px" :text-align "center"}]
                                                               [core/label :src (core/at) :label ":maximum"]
                                                               [core/label :src (core/at) :label @maximum :style {:width "40px" :font-size "11px" :text-align "center"}]]]
                                                   [core/h-box :src (core/at)
                                                    :gap      "10px"
                                                    :align    :center
                                                    :children [[core/checkbox :src (core/at)
                                                                :label     [core/box :src (core/at) :align :start :child [:code ":minimum 10am"]]
                                                                :model     (not= @minimum init-minimum)
                                                                :on-change #(reset! minimum (if % 1000 init-minimum))]
                                                               [core/checkbox :src (core/at)
                                                                :label     [core/box :src (core/at) :align :start :child [:code ":maximum 2pm"]]
                                                                :model     (not= @maximum init-maximum)
                                                                :on-change #(reset! maximum (if % 1400 init-maximum))]]]]]]]]])))


(defn example []
      (acu/demo "Input Time" [basics-demo]))
