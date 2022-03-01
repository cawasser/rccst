(ns bh.rccst.views.atom.example.re-com.popover
  (:require [bh.rccst.ui-component.atom.chart.area-chart :as area-chart]
            [bh.rccst.ui-component.atom.chart.line-chart :as line-chart]
            [bh.rccst.ui-component.atom.chart.sankey-chart :as sankey-chart]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


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



(defn- line-chart-popover [data component-id]
  (let [showing? (r/atom false)]
    [rc/popover-anchor-wrapper :src (rc/at)
     :showing? showing?
     :position :above-left
     :anchor [rc/button :src (rc/at)
              :label "Line Chart"
              :on-click #(swap! showing? not)]
     :popover [rc/popover-content-wrapper :src (rc/at)
               :title "A Line Chart"
               :body [line-chart/component data component-id]]]))


(defn- area-chart-popover [data component-id]
  (let [showing? (r/atom false)]
    [rc/popover-anchor-wrapper :src (rc/at)
     :showing? showing?
     :position :above-center
     :anchor [rc/button :src (rc/at)
              :label "Area Chart"
              :on-click #(swap! showing? not)]
     :popover [rc/popover-content-wrapper :src (rc/at)
               :title "An Area Chart"
               :body [area-chart/component data component-id]]]))


(defn- sankey-chart-popover [data component-id]
  (let [showing? (r/atom false)]
    [rc/popover-anchor-wrapper :src (rc/at)
     :showing? showing?
     :position :above-center
     :anchor [rc/button :src (rc/at)
              :label "Sankey Chart"
              :on-click #(swap! showing? not)]
     :popover [rc/popover-content-wrapper :src (rc/at)
               :title "A Sankey Chart"
               :body [sankey-chart/component data component-id]]]))


(defn chart-example []
  (let [tabular-data line-chart/sample-data
        dag-data sankey-chart/sample-data]
    (acu/demo "Popover (with very complex content)"
      "A few simple `popovers` from [Re-com](https://github.com/Day8/re-com), using a `hyperlink`
    as the 'anchor', only now the content is an entire chart!"

      [layout/centered {:extra-classes :width-50}
       [rc/h-box :src (rc/at)
        :gap "10px"
        :children [[line-chart-popover tabular-data "popover/line-chart"]
                   [area-chart-popover tabular-data "popover/area-chart"]
                   [sankey-chart-popover dag-data "popover/sankey-chart"]]]]

      '[layout/centered {:extra-classes :width-50}
        [rc/h-box :src (rc/at)
         :gap "10px"
         :children [[line-chart-popover data "popover/line-chart"]
                    [area-chart-popover data "popover/area-chart"]
                    [sankey-chart-popover dag-data "popover/sankey-chart"]]]])))
