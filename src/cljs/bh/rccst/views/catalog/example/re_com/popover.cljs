(ns bh.rccst.views.catalog.example.re-com.popover
  (:require [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.catalog.utils :as acu]
            [re-com.core :as rc]
            [reagent.core :as r]

    ; we should wrap this into a ui-component
            ["recharts" :refer [LineChart Line
                                XAxis YAxis CartesianGrid
                                Tooltip Legend]]))



(defn button-anchor-example []
  (acu/demo "Popover (anchored to a button)"
    "A simple `popover` from [Re-com](https://github.com/Day8/re-com), using a `button`
    as the 'anchor'"

    [layout/centered {:extra-classes :width-50}
     (let [showing? (r/atom false)]
       [rc/popover-anchor-wrapper :src (rc/at)
        :showing? showing?
        :position :right-below
        :anchor [rc/button :src (rc/at)
                 :label "Anchor"
                 :on-click #(swap! showing? not)]
        :popover [rc/popover-content-wrapper :src (rc/at)
                  :title "Title"
                  :body "Popover body text"]])]

    '[layout/centered {:extra-classes :width-50}
      (let [showing? (r/atom false)]
        [rc/popover-anchor-wrapper :src (rc/at)
         :showing? showing?
         :position :right-below
         :anchor [rc/button :src (rc/at)
                  :label "Anchor"
                  :on-click #(swap! showing? not)]
         :popover [rc/popover-content-wrapper :src (rc/at)
                   :title "Title"
                   :body "Popover body text"]])]))


(defn link-anchor-example []
  (acu/demo "Popover (anchored to a hyperlink)"
    "Another simple `popover` from [Re-com](https://github.com/Day8/re-com), using a `hyperlink`
    as the 'anchor'"

    [layout/centered {:extra-classes :width-50}
     (let [showing? (r/atom false)]
       [rc/popover-anchor-wrapper :src (rc/at)
        :showing? showing?
        :position :left-above
        :anchor [rc/hyperlink :src (rc/at)
                 :label "click me for popover"
                 :on-click #(swap! showing? not)]
        :popover [rc/popover-content-wrapper :src (rc/at)
                  :width "250px"
                  :title "Popover Title"
                  :body "popover body"]])]

    '[layout/centered {:extra-classes :width-50}
      (let [showing? (r/atom false)]
        [rc/popover-anchor-wrapper :src (rc/at)
         :showing? showing?
         :position :left-above
         :anchor [rc/hyperlink :src (rc/at)
                  :label "click me for popover"
                  :on-click #(swap! showing? not)]
         :popover [rc/popover-content-wrapper :src (rc/at)
                   :width "250px"
                   :title "Popover Title"
                   :body "popover body"]])]))



(def data (r/atom [{:name "Page A" :uv 4000 :pv 2400 :amt 2400}
                   {:name "Page B" :uv 3000 :pv 1398 :amt 2210}
                   {:name "Page C" :uv 2000 :pv 9800 :amt 2290}
                   {:name "Page D" :uv 2780 :pv 3908 :amt 2000}
                   {:name "Page E" :uv 1890 :pv 4800 :amt 2181}
                   {:name "Page F" :uv 2390 :pv 3800 :amt 2500}
                   {:name "Page G" :uv 3490 :pv 4300 :amt 2100}]))


(defn chart-example []
  (acu/demo "Popover (with very complex content)"
    "Another simple `popover` from [Re-com](https://github.com/Day8/re-com), using a `hyperlink`
    as the 'anchor', only now the content is an entire line-chart!"

    [layout/centered {:extra-classes :width-50}
     (let [showing? (r/atom false)]
       [rc/popover-anchor-wrapper :src (rc/at)
        :showing? showing?
        :position :left-above
        :anchor [rc/button :src (rc/at)
                 :label "Line Chart"
                 :on-click #(swap! showing? not)]
        :popover [rc/popover-content-wrapper :src (rc/at)
                  :title "A Line Chart"
                  :body [:> LineChart {:width 400 :height 400 :data @data}
                         [:> CartesianGrid {:strokeDasharray "3 3"}]
                         [:> XAxis {:dataKey :name}]
                         [:> YAxis]
                         [:> Tooltip]
                         [:> Line {:type              "monotone"
                                   :dataKey :uv
                                   :isAnimationActive false
                                   :stroke            "#8884d8"}]
                         [:> Line {:type              "monotone"
                                   :dataKey :pv
                                   :isAnimationActive false
                                   :stroke            "#82ca9d"}]
                         [:> Line {:type              "monotone"
                                   :dataKey :amt
                                   :isAnimationActive false
                                   :stroke            "#ff00ff"}]]]])]

    '[layout/centered {:extra-classes :width-50}
      (let [showing? (r/atom false)]
        [rc/popover-anchor-wrapper :src (rc/at)
         :showing? showing?
         :position :left-above
         :anchor [rc/button :src (rc/at)
                  :label "Line Chart"
                  :on-click #(swap! showing? not)]
         :popover [rc/popover-content-wrapper :src (rc/at)
                   :title "A Line Chart"
                   :body [:> LineChart {:width 400 :height 400 :data @data}
                          [:> CartesianGrid {:strokeDasharray "3 3"}]
                          [:> XAxis {:dataKey :name}]
                          [:> YAxis]
                          [:> Tooltip]
                          [:> Line {:type              "monotone"
                                    :dataKey :uv
                                    :isAnimationActive false
                                    :stroke            "#8884d8"}]
                          [:> Line {:type              "monotone"
                                    :dataKey :pv
                                    :isAnimationActive false
                                    :stroke            "#82ca9d"}]
                          [:> Line {:type              "monotone"
                                    :dataKey :amt
                                    :isAnimationActive false
                                    :stroke            "#ff00ff"}]]]])]))
