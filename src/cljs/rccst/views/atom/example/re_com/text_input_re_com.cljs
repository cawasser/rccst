(ns rccst.views.atom.example.re-com.text-input-re-com
  (:require [clojure.string :as string]
            [re-com.core :as rc]
            [reagent.ratom :as ratom]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn- demos [{:keys [disabled? change-on-blur? on-alter?
                      regex text-val status-tooltip
                      status-icon? status slider-val] :as context}]
  [rc/v-box
   :children [[rc/label :src (rc/at) :label "[input-text ... ]"]
              [rc/gap :src (rc/at) :size "5px"]
              [rc/input-text :src (rc/at)
               :model text-val
               :status @status
               :status-icon? @status-icon?
               :status-tooltip @status-tooltip
               :width "300px"
               :placeholder (if @regex "enter number (99.9)" "placeholder message")
               :on-change #(reset! text-val %)
               :validation-regex @regex
               :on-alter (if @on-alter? string/upper-case identity)
               :disabled? disabled?]
              [rc/gap :src (rc/at) :size "20px"]

              [rc/label :src (rc/at) :label "[input-password ... ]"]
              [rc/gap :src (rc/at) :size "5px"]
              [rc/input-password :src (rc/at)
               :model text-val
               :status @status
               :status-icon? @status-icon?
               :status-tooltip @status-tooltip
               :width "300px"
               :placeholder (if @regex "enter number (99.9)" "placeholder message")
               :on-change #(reset! text-val %)
               :validation-regex @regex
               :on-alter (if @on-alter? string/upper-case identity)
               :disabled? disabled?]
              [rc/gap :src (rc/at) :size "20px"]

              [rc/label :src (rc/at) :label "[input-textarea ... ]"]
              [rc/gap :src (rc/at) :size "5px"]
              [rc/input-textarea :src (rc/at)
               :model text-val
               :status @status
               :status-icon? @status-icon?
               :status-tooltip @status-tooltip
               :width "300px"
               :rows @slider-val
               :placeholder (if @regex "enter number (99.9)" "placeholder message")
               :on-change #(reset! text-val %)
               :validation-regex @regex
               :on-alter (if @on-alter? string/upper-case identity)
               :disabled? disabled?]]])


(defn- controls [{:keys [disabled? change-on-blur? on-alter?
                         regex text-val status-tooltip
                         status-icon? status slider-val] :as context}]
  [rc/v-box :src (rc/at)
   :gap "15px"
   :style {:min-width        "150px"
           :padding          "15px"
           :border-top       "1px solid #DDD"
           :background-color "#f7f7f7"}
   :children [
              [rc/title :src (rc/at) :level :level3 :label "Interactive Parameters" :style {:margin-top "0"}]
              [rc/v-box :src (rc/at)
               :children [[rc/box :src (rc/at) :align :start :child [:code ":status"]]
                          [rc/radio-button :src (rc/at)
                           :label "nil/omitted - normal input state"
                           :value nil
                           :model @status
                           :on-change #(do
                                         (reset! status %)
                                         (reset! status-tooltip ""))
                           :style {:margin-left "20px"}]
                          [rc/radio-button :src (rc/at)
                           :label ":success - border color becomes green"
                           :value :success
                           :model @status
                           :on-change #(do
                                         (reset! status %)
                                         (reset! status-tooltip "Success tooltip - this (optionally) appears when an input-text components has validated successfully."))
                           :style {:margin-left "20px"}]
                          [rc/radio-button :src (rc/at)
                           :label ":warning - border color becomes orange"
                           :value :warning
                           :model @status
                           :on-change #(do
                                         (reset! status %)
                                         (reset! status-tooltip "Warning tooltip - this (optionally) appears when there are warnings on input-text components."))
                           :style {:margin-left "20px"}]]]
              [rc/h-box :src (rc/at)
               :align :start
               :gap "5px"
               :children [[rc/checkbox :src (rc/at)
                           :label [:code ":status-icon?"]
                           :model status-icon?
                           :on-change (fn [val]
                                        (reset! status-icon? val))]
                          [:span " (notice the tooltips on the icons)"]]]

              [rc/h-box :src (rc/at)
               :align :start
               :gap "5px"
               :children [[rc/checkbox :src (rc/at)
                           :label [:code ":on-alter"]
                           :model on-alter?
                           :on-change (fn [val]
                                        (reset! on-alter? val))]
                          [:span " (set to " [:code "string/upper-case"] ")"]]]
              [rc/checkbox :src (rc/at)
               :label [rc/box :src (rc/at) :align :start :child [:code ":disabled?"]]
               :model disabled?
               :on-change (fn [val]
                            (reset! disabled? val))]
              [rc/h-box :src (rc/at)
               :gap "10px"
               :children [[rc/h-box :src (rc/at)
                           :align :start
                           :children [[:code ":rows"]
                                      "(textarea)"]]
                          [rc/slider :src (rc/at)
                           :model slider-val
                           :min 1
                           :max 10
                           :width "200px"
                           :on-change #(reset! slider-val %)]
                          [rc/label :src (rc/at) :label @slider-val]]]]])


(defn example []
  (let [context {:disabled?       (ratom/atom false)
                 :change-on-blur? (ratom/atom true)
                 :on-alter?       (ratom/atom false)
                 :regex           (ratom/atom nil)
                 :text-val        (ratom/atom nil)
                 :status-tooltip  (ratom/atom "")
                 :status-icon?    (ratom/atom false)
                 :status          (ratom/atom nil)
                 :slider-val      (ratom/atom 4)}]
    (fn []
      (acu/demo "Input Text"
        [rc/h-box :src (rc/at)
         :gap "10px"
         :children [[layout/centered {:extra-classes :width-50}
                     [controls context]]
                    [layout/centered {:extra-classes :width-50}
                     [demos context]]]]))))
