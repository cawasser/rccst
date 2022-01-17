(ns  bh.rccst.views.catalog.example.slider
  (:require [woolybear.ad.catalog.utils :as acu]
            [reagent.ratom :as ratom]
            [re-com.core   :refer [at h-box v-box box gap line label title slider checkbox input-text p]]))


(defn example []
      (let [slider-val  (ratom/atom "0")
            slider-min  (ratom/atom "0")
            slider-max  (ratom/atom "100")
            disabled?   (ratom/atom false)]
           (fn []
               (acu/demo "Slider"
                         [v-box
                          :src      (at)
                          :gap      "20px"
                          :children [[slider
                                      :src       (at)
                                      :model     slider-val
                                      :min       slider-min
                                      :max       slider-max
                                      :width     "300px"
                                      :on-change #(reset! slider-val (str %))
                                      :disabled? disabled?]
                                     [gap :src (at) :size "0px"]
                                     [v-box
                                      :src      (at)
                                      :gap "10px"
                                      :style    {:min-width        "150px"
                                                 :padding          "15px"
                                                 :border-top       "1px solid #DDD"
                                                 :background-color "#f7f7f7"}
                                      :children [[title :src (at) :level :level3 :label "Interactive Parameters" :style {:margin-top "0"}]
                                                 [h-box
                                                  :src      (at)
                                                  :gap      "10px"
                                                  :align    :center
                                                  :children [
                                                             [box
                                                              :src      (at)
                                                              :align :start
                                                              :width "60px"
                                                              :child [:code ":model"]]
                                                             [input-text
                                                              :src      (at)
                                                              :model           slider-val
                                                              :width           "60px"
                                                              :height          "26px"
                                                              :on-change       #(reset! slider-val %)
                                                              :change-on-blur? false]]]
                                                 [h-box
                                                  :src      (at)
                                                  :gap      "10px"
                                                  :align    :center
                                                  :children [[box
                                                              :src      (at)
                                                              :align :start
                                                              :width "60px"
                                                              :child [:code ":min"]]
                                                             [input-text
                                                              :src      (at)
                                                              :model           slider-min
                                                              :width           "60px"
                                                              :height          "26px"
                                                              :on-change       #(reset! slider-min %)
                                                              :change-on-blur? false]]]
                                                 [h-box
                                                  :src      (at)
                                                  :gap      "10px"
                                                  :align    :center
                                                  :children [[box
                                                              :src      (at)
                                                              :align :start
                                                              :width "60px"
                                                              :child [:code ":max"]]
                                                             [input-text
                                                              :src      (at)
                                                              :model           slider-max
                                                              :width           "60px"
                                                              :height          "26px"
                                                              :on-change       #(reset! slider-max %)
                                                              :change-on-blur? false]]]
                                                 [checkbox
                                                  :src      (at)
                                                  :label [box
                                                          :src      (at)
                                                          :align :start :child [:code ":disabled?"]]
                                                  :model disabled?
                                                  :on-change (fn [val]
                                                                 (reset! disabled? val))]]]]]))))
