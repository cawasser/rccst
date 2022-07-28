(ns rccst.views.atom.example.re-com.slider
  (:require [re-com.core :as rc]
            [reagent.core :as r]
            [bh.ui-component.atom.re-com.slider :as slider]
            [woolybear.ad.catalog.utils :as acu]))


(defn example []
  (let [slider-val (r/atom "0")
        slider-min (r/atom "0")
        slider-max (r/atom "100")
        disabled? (r/atom false)]
    (fn []
      (acu/demo "Slider"
        [rc/h-box
         :src (rc/at)
         :justify :between
         :children [[slider/slider :value slider-val :range (r/atom [@slider-min @slider-max])]
                    [rc/gap :src (rc/at) :size "0px"]
                    [rc/v-box
                     :src (rc/at)
                     :gap "10px"
                     :style {:min-width        "150px"
                             :padding          "15px"
                             :border-top       "1px solid #DDD"
                             :background-color "#f7f7f7"}
                     :children [[rc/title :src (rc/at) :level :level3 :label "Interactive Parameters" :style {:margin-top "0"}]
                                [rc/h-box
                                 :src (rc/at)
                                 :gap "10px"
                                 :align :center
                                 :children [[rc/box
                                             :src (rc/at)
                                             :align :start
                                             :width "60px"
                                             :child [:code ":model"]]
                                            [rc/input-text
                                             :src (rc/at)
                                             :model slider-val
                                             :width "60px"
                                             :height "26px"
                                             :on-change #(reset! slider-val %)
                                             :change-on-blur? false]]]
                                [rc/h-box
                                 :src (rc/at)
                                 :gap "10px"
                                 :align :center
                                 :children [[rc/box
                                             :src (rc/at)
                                             :align :start
                                             :width "60px"
                                             :child [:code ":min"]]
                                            [rc/input-text
                                             :src (rc/at)
                                             :model slider-min
                                             :width "60px"
                                             :height "26px"
                                             :on-change #(reset! slider-min %)
                                             :change-on-blur? false]]]
                                [rc/h-box
                                 :src (rc/at)
                                 :gap "10px"
                                 :align :center
                                 :children [[rc/box
                                             :src (rc/at)
                                             :align :start
                                             :width "60px"
                                             :child [:code ":max"]]
                                            [rc/input-text
                                             :src (rc/at)
                                             :model slider-max
                                             :width "60px"
                                             :height "26px"
                                             :on-change #(reset! slider-max %)
                                             :change-on-blur? false]]]
                                [rc/checkbox
                                 :src (rc/at)
                                 :label [rc/box
                                         :src (rc/at)
                                         :align :start :child [:code ":disabled?"]]
                                 :model disabled?
                                 :on-change (fn [val]
                                              (reset! disabled? val))]]]]]

        '[rc/slider
          :src (rc/at)
          :model slider-val
          :min slider-min
          :max slider-max
          :width "300px"
          :on-change #(reset! slider-val (str %))
          :disabled? disabled?]))))
