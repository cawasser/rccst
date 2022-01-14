(ns  bh.rccst.views.catalog.example.text-input-re-com
  (:require [woolybear.ad.catalog.utils :as acu]
            [reagent.ratom :as ratom]
            [re-com.core       :refer [at h-box v-box box gap line input-text input-password input-textarea label checkbox radio-button slider title p]]
            [clojure.string    :as    string]))


(defn example []
      (let [disabled?   (ratom/atom false)
            change-on-blur? (ratom/atom true)
            on-alter?       (ratom/atom false)
            regex           (ratom/atom nil)
            text-val        (ratom/atom nil)
            status-tooltip  (ratom/atom "")
            status-icon?    (ratom/atom false)
            status          (ratom/atom nil)
            slider-val      (ratom/atom 4)]
           (fn []
               (acu/demo "Input Text"
                         [v-box :src (at)
                          :gap      "10px"
                          :children [
                                     [label :src (at) :label "[input-text ... ]"]
                                     [gap :src (at) :size "5px"]
                                     [input-text :src (at)
                                      :model            text-val
                                      :status           @status
                                      :status-icon?     @status-icon?
                                      :status-tooltip   @status-tooltip
                                      :width            "300px"
                                      :placeholder      (if @regex "enter number (99.9)" "placeholder message")
                                      :on-change        #(reset! text-val %)
                                      :validation-regex @regex
                                      :on-alter         (if @on-alter? string/upper-case identity)
                                      :disabled?        disabled?]
                                     [gap :src (at) :size "20px"]
                                     [label :src (at) :label "[input-password ... ]"]
                                     [gap :src (at) :size "5px"]
                                     [input-password :src (at)
                                      :model            text-val
                                      :status           @status
                                      :status-icon?     @status-icon?
                                      :status-tooltip   @status-tooltip
                                      :width            "300px"
                                      :placeholder      (if @regex "enter number (99.9)" "placeholder message")
                                      :on-change        #(reset! text-val %)
                                      :validation-regex @regex
                                      :on-alter         (if @on-alter? string/upper-case identity)
                                      :disabled?        disabled?]
                                     [gap :src (at) :size "20px"]
                                     [label :src (at) :label "[input-textarea ... ]"]
                                     [gap :src (at) :size "5px"]
                                     [input-textarea :src (at)
                                      :model            text-val
                                      :status           @status
                                      :status-icon?     @status-icon?
                                      :status-tooltip   @status-tooltip
                                      :width            "300px"
                                      :rows             @slider-val
                                      :placeholder      (if @regex "enter number (99.9)" "placeholder message")
                                      :on-change        #(reset! text-val %)
                                      :validation-regex @regex
                                      :on-alter         (if @on-alter? string/upper-case identity)
                                      :disabled?        disabled?]

                                     [v-box :src (at)
                                      :gap "15px"
                                      :style    {:min-width        "150px"
                                                 :padding          "15px"
                                                 :border-top       "1px solid #DDD"
                                                 :background-color "#f7f7f7"}
                                      :children [
                                                 [title :src (at) :level :level3 :label "Interactive Parameters" :style {:margin-top "0"}]
                                                 [v-box :src (at)
                                                  :children [[box :src (at) :align :start :child [:code ":status"]]
                                                             [radio-button :src (at)
                                                              :label     "nil/omitted - normal input state"
                                                              :value     nil
                                                              :model     @status
                                                              :on-change #(do
                                                                            (reset! status %)
                                                                            (reset! status-tooltip ""))
                                                              :style {:margin-left "20px"}]
                                                             [radio-button :src (at)
                                                              :label ":success - border color becomes green"
                                                              :value :success
                                                              :model @status
                                                              :on-change #(do
                                                                            (reset! status %)
                                                                            (reset! status-tooltip "Success tooltip - this (optionally) appears when an input-text components has validated successfully."))
                                                              :style {:margin-left "20px"}]
                                                             [radio-button :src (at)
                                                              :label     ":warning - border color becomes orange"
                                                              :value     :warning
                                                              :model     @status
                                                              :on-change #(do
                                                                            (reset! status %)
                                                                            (reset! status-tooltip "Warning tooltip - this (optionally) appears when there are warnings on input-text components."))
                                                              :style     {:margin-left "20px"}]]]
                                                 [h-box :src (at)
                                                  :align :start
                                                  :gap      "5px"
                                                  :children [[checkbox :src (at)
                                                              :label     [:code ":status-icon?"]
                                                              :model     status-icon?
                                                              :on-change (fn [val]
                                                                             (reset! status-icon? val))]
                                                             [:span " (notice the tooltips on the icons)"]]]

                                                 [h-box :src (at)
                                                  :align    :start
                                                  :gap      "5px"
                                                  :children [[checkbox :src (at)
                                                              :label     [:code ":on-alter"]
                                                              :model     on-alter?
                                                              :on-change (fn [val]
                                                                             (reset! on-alter? val))]
                                                             [:span " (set to " [:code "string/upper-case"] ")"]]]
                                                 [checkbox :src (at)
                                                  :label     [box :src (at) :align :start :child [:code ":disabled?"]]
                                                  :model     disabled?
                                                  :on-change (fn [val]
                                                                 (reset! disabled? val))]
                                                 [h-box :src (at)
                                                  :gap "10px"
                                                  :children [[h-box :src (at)
                                                              :align    :start
                                                              :children [[:code ":rows"]
                                                                         "(textarea)"]]
                                                             [slider :src (at)
                                                              :model     slider-val
                                                              :min       1
                                                              :max       10
                                                              :width     "200px"
                                                              :on-change #(reset! slider-val %)]
                                                             [label :src (at) :label @slider-val]]]]]]]))))