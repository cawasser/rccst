(ns bh.rccst.views.atom.example.re-com.date-picker
  (:require-macros
    [reagent.ratom :refer [reaction]])
  (:require [cljs-time.coerce :refer [to-local-date]]
            [cljs-time.core :refer [before? day-of-week days minus plus today]]
            [cljs-time.format :refer [formatter unparse]]
            [goog.date.Date]
            [re-com.core :as core]
            [re-com.datepicker :as dp]
            [re-com.util :refer [now->utc px]]
            [re-com.validate :refer [date-like?]]
            [reagent.core :as r]
            [woolybear.ad.catalog.utils :as acu]))


(def ^:private days-map
  {:Su "S" :Mo "M" :Tu "T" :We "W" :Th "T" :Fr "F" :Sa "S"})


(defn- toggle-inclusion!
  "convenience function to include/exclude member from"
  [set-atom member]
  (reset! set-atom
    (if (contains? @set-atom member)
      (disj @set-atom member)
      (conj @set-atom member))))


(defn- checkbox-for-day
  [day enabled-days]
  [core/v-box
   :src (core/at)
   :align :center
   :children [[core/label
               :src (core/at)
               :style {:font-size "smaller"}
               :label (day days-map)]
              [core/checkbox
               :src (core/at)
               :model (@enabled-days day)
               :on-change #(toggle-inclusion! enabled-days day)]]])


(defn- parameters-with
  "Toggle controls for some parameters."
  [content enabled-days as-days disabled? show-today? show-weeks? start-of-week-choices start-of-week]

  [core/h-box :src (core/at)
   :gap "15px"
   :justify :between
   :children [content
              [core/v-box
               :src (core/at)
               :gap "10px"
               :style {:min-width        "550px"
                       :padding          "15px"
                       :border-top       "1px solid #DDD"
                       :background-color "#f7f7f7"}
               :children [[core/title
                           :src (core/at)
                           :style {:margin-top "0"}
                           :level :level3 :label "Interactive Parameters"]
                          [core/checkbox
                           :src (core/at)
                           :label [core/box
                                   :src (core/at)
                                   :align :start
                                   :child [:code ":disabled?"]]
                           :model disabled?
                           :on-change #(reset! disabled? %)]
                          [core/checkbox
                           :src (core/at)
                           :label [core/box
                                   :src (core/at)
                                   :align :start
                                   :child [:code ":show-today?"]]
                           :model show-today?
                           :on-change #(reset! show-today? %)]
                          [core/checkbox
                           :src (core/at)
                           :label [core/box
                                   :src (core/at)
                                   :align :start
                                   :child [:code ":show-weeks?"]]
                           :model show-weeks?
                           :on-change #(reset! show-weeks? %)]
                          [core/h-box :src (core/at)
                           :gap "5px"
                           :align :end
                           :children [[:code ":start-of-week"]
                                      [core/single-dropdown
                                       :src (core/at)
                                       :choices start-of-week-choices
                                       :model start-of-week
                                       :on-change #(reset! start-of-week %)
                                       :width "110px"]]]
                          [core/h-box
                           :src (core/at)
                           :gap "2px"
                           :align :end
                           :children [[core/box
                                       :src (core/at)
                                       :align :end
                                       :child [:code ":selectable-fn"]]
                                      [core/gap
                                       :src (core/at)
                                       :size "5px"]
                                      [checkbox-for-day :Su enabled-days]
                                      [checkbox-for-day :Mo enabled-days]
                                      [checkbox-for-day :Tu enabled-days]
                                      [checkbox-for-day :We enabled-days]
                                      [checkbox-for-day :Th enabled-days]
                                      [checkbox-for-day :Fr enabled-days]
                                      [checkbox-for-day :Sa enabled-days]
                                      [core/gap
                                       :src (core/at)
                                       :size "5px"]
                                      [core/box
                                       :src (core/at)
                                       :align :end
                                       :child [:code (str "(fn [d]\n (" @as-days " (.getDay d)))")]]]]]]]])


(defn- date->string
  [date]
  (if (date-like? date)
    (unparse (formatter "dd MMM, yyyy") date)
    "no date"))


(defn- show-variant
  [variation]
  (let [model1 (r/atom #_nil  #_(today) (now->utc))         ;; Test 3 valid data types
        model2 (r/atom #_nil  #_(plus (today) (days 120)) (plus (now->utc) (days 120))) ;; (today) = goog.date.Date, (now->utc) = goog.date.UtcDateTime
        model3 (r/atom nil)
        model4 (r/atom (today))
        disabled? (r/atom false)
        show-today? (r/atom true)
        show-weeks? (r/atom false)
        start-of-week (r/atom 6)
        start-of-week-right (r/atom 0)
        start-of-week-choices [{:id 0 :label "Monday"}
                               {:id 1 :label "Tuesday"}
                               {:id 2 :label "Wednesday"}
                               {:id 3 :label "Thursday"}
                               {:id 4 :label "Friday"}
                               {:id 5 :label "Saturday"}
                               {:id 6 :label "Sunday"}]
        enabled-days (r/atom (-> days-map keys set))
        as-days (reaction (->> (map #(% {:Su 7 :Sa 6 :Fr 5 :Th 4 :We 3 :Tu 2 :Mo 1}) @enabled-days) (map #(if (= 7 %) 0 %)) sort set))
        selectable-pred (fn [^js/goog.date.UtcDateTime date] (@as-days (.getDay date)))] ; Simply allow selection based on day of week.
    (case variation
      :inline [(fn inline-fn
                 []
                 [parameters-with
                  [core/v-box :src (core/at)
                   :gap "15px"
                   :children [[dp/datepicker :src (core/at)
                               :model model1
                               :disabled? disabled?
                               :show-today? @show-today?
                               :show-weeks? @show-weeks?
                               :selectable-fn selectable-pred
                               :start-of-week @start-of-week
                               :on-change #(do #_(js/console.log "model1:" %) (reset! model1 %))]
                              [core/label :src (core/at) :label [:span [:code ":model"] " is " (date->string @model1)]]
                              #_[h-box :src (at)
                                 :gap "6px"
                                 :margin "10px 0px 0px 0px"
                                 :align :center
                                 :children [[label :src (at) :style label-style :label "Change model:"]
                                            [md-icon-button :src (at)
                                             :md-icon-name "zmdi-arrow-left"
                                             :size :smaller
                                             :disabled? (not (date-like? @model1))
                                             :on-click #(when (date-like? @model1)
                                                          (reset! model1 (minus @model1 (days 1))))]
                                            [md-icon-button :src (at)
                                             :md-icon-name "zmdi-arrow-right"
                                             :size :smaller
                                             :disabled? (if (and (date-like? @model1) (date-like? @model2))
                                                          (not (before? (to-local-date @model1)
                                                                 (to-local-date @model2)))
                                                          true)
                                             :on-click #(when (date-like? @model1)
                                                          (reset! model1 (plus @model1 (days 1))))]
                                            [button :src (at)
                                             :label "Reset"
                                             :class "btn btn-default"
                                             :style {:padding "1px 4px"}
                                             :on-click #(reset! model1 nil)]]]]]
                  enabled-days
                  as-days
                  disabled?
                  show-today?
                  show-weeks?
                  start-of-week-choices
                  start-of-week])]

      :dropdown [(fn dropdown-fn
                   []
                   [parameters-with
                    [core/v-box
                     :src (core/at)
                     :gap "15px"
                     :children [[core/datepicker-dropdown
                                 :src (core/at)
                                 :model model3
                                 :show-today? @show-today?
                                 :show-weeks? @show-weeks?
                                 :selectable-fn selectable-pred
                                 :start-of-week @start-of-week
                                 :placeholder "Select a date"
                                 :format "dd MMM, yyyy"
                                 :disabled? disabled?
                                 :on-change #(reset! model3 %)]
                                [core/label
                                 :src (core/at)
                                 :label [:span [:code ":model"] " is " (date->string @model3)]]]]
                    enabled-days
                    as-days
                    disabled?
                    show-today?
                    show-weeks?
                    start-of-week-choices
                    start-of-week])])))


(def variations ^:private
  [{:id :inline :label "Inline"}
   {:id :dropdown :label "Dropdown"}])


(defn datepicker-examples
  []
  (let [selected-variation (r/atom :inline)]
    (fn examples-fn []
      [core/v-box :src (core/at)
       :size "auto"
       :gap "10px"
       :children [[core/h-box
                   :src (core/at)
                   :gap "100px"
                   :children [[core/v-box
                               :src (core/at)
                               :gap "20px"
                               :size "auto"
                               :children [[core/h-box
                                           :src (core/at)
                                           :gap "10px"
                                           :align :center
                                           :children [[core/label
                                                       :src (core/at)
                                                       :label "Select a demo"]
                                                      [core/single-dropdown
                                                       :src (core/at)
                                                       :choices variations
                                                       :model selected-variation
                                                       :width "200px"
                                                       :on-change #(reset! selected-variation %)]]]
                                          [show-variant @selected-variation]]]]]]])))


(defn example []
  (acu/demo "Date Picker" [datepicker-examples]
    '[core/v-box :src (core/at)
      :size "auto"
      :gap "10px"
      :children [[core/h-box
                  :src (core/at)
                  :gap "100px"
                  :children [[core/v-box
                              :src (core/at)
                              :gap "20px"
                              :size "auto"
                              :children [[core/h-box
                                          :src (core/at)
                                          :gap "10px"
                                          :align :center
                                          :children [[core/label
                                                      :src (core/at)
                                                      :label "Select a demo"]
                                                     [core/single-dropdown
                                                      :src (core/at)
                                                      :choices variations
                                                      :model selected-variation
                                                      :width "200px"
                                                      :on-change #(reset! selected-variation %)]]]
                                         [show-variant @selected-variation]]]]]]]))
