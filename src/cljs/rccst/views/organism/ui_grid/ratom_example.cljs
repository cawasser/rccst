(ns rccst.views.organism.ui-grid.ratom-example
  (:require [rccst.subs :as subs]
            [bh.ui-component.organism.ui-grid :as grid]
            [bh.ui-component.molecule.composite.chart-remote-data :as chart-remote-data]
            [bh.ui-component.molecule.composite.coverage-plan :as coverage-plan]
            [bh.ui-component.molecule.composite.simple-multi-chart :as simple-multi-chart]
            [bh.ui-component.molecule.composite.simple-multi-chart-2 :as simple-multi-chart-2]
            [bh.ui-component.molecule.grid-container :as grid-container]
            [bh.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(log/info "rccst.views.organism.ui-grid.ratom-example")


(def container-id "ui-grid-ratom-demo")

(def bar-chart-widget ["bar-chart" "Bar Chart"
                       [grid-container/component
                        :data (r/atom chart-remote-data/ui-definition)
                        :component-id (h/path->keyword container-id "bar-chart")
                        :resizable true]
                       :green :white])
(def multi-chart-widget ["multi-chart" "Multi-Chart"
                         [grid-container/component
                          :data (r/atom simple-multi-chart/ui-definition)
                          :component-id (h/path->keyword container-id "multi-chart")
                          :resizable true]
                         :blue :white])
(def multi-chart-2-widget ["multi-chart-2" "Multi-Chart-2"
                           [grid-container/component
                            :data (r/atom simple-multi-chart-2/ui-definition)
                            :component-id (h/path->keyword container-id "multi-chart-2")
                            :resizable true]
                           :rebeccapurple :white])
(def coverage-plan-widget ["coverage-plan" "Coverage Plan"
                           [grid-container/component
                            :data (r/atom coverage-plan/ui-definition)
                            :component-id (h/path->keyword container-id "coverage-plan")
                            :resizable true]
                           :yellow :black])
(def default-widgets #{bar-chart-widget})

(def bar-chart-layout {:i "bar-chart" :x 0 :y 0 :w 8 :h 15})
(def multi-chart-layout {:i "multi-chart" :x 0 :y 10 :w 8 :h 15})
(def multi-chart-2-layout {:i "multi-chart-2" :x 8 :y 21 :w 12 :h 15})
(def coverage-plan-layout {:i "coverage-plan" :x 8 :y 0 :w 12 :h 21})
(def default-layout #{bar-chart-layout})


(def empty-widgets #{})
(def empty-layout  #{})


(def widgets (r/atom default-widgets))
(def layout (r/atom default-layout))


(defn- grid-reset [widgets layout widget-val layout-val]
  (reset! widgets widget-val)
  (reset! layout layout-val))


(defn- toggle-val [s val]
  (if (contains? s val)
    (disj s val)
    (conj s val)))


(defn- grid-update [widgets layout widget-val layout-val]
  (reset! widgets (toggle-val @widgets widget-val))
  (reset! layout (toggle-val @layout layout-val)))


(defn- widget-tools [widgets layout default-widgets]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Widgets:"]
              [rc/button :on-click #(grid-reset widgets layout empty-widgets empty-layout)
               :label "Empty"]
              [rc/button :on-click #(grid-reset widgets layout default-widgets default-layout)
               :label "Default"]
              [rc/button :on-click #(grid-update widgets layout bar-chart-widget bar-chart-layout)
               :label "! Bar Chart"]
              [rc/button :on-click #(grid-update widgets layout multi-chart-widget multi-chart-layout)
               :label "! Multi Chart"]
              [rc/button :on-click #(grid-update widgets layout multi-chart-2-widget multi-chart-2-layout)
               :label "! Multi Chart 2"]
              [rc/button :on-click #(grid-update widgets layout coverage-plan-widget coverage-plan-layout)
               :label "! Coverage Plan"]]])


(defn example []
  (let [logged-in?       (re-frame/subscribe [::subs/logged-in?])
        pub-sub-started? (re-frame/subscribe [::subs/pub-sub-started?])]

    (if (not @logged-in?)
      (re-frame/dispatch [:rccst.events/login "test-user" "test-pwd"]))

    (fn []
      (acu/demo "Widget Grid (ratom-based)"
        "A grid of widget, which are composed of UI Components using a data structure that defines a directed graph."

        (if (and @logged-in? @pub-sub-started?)
          [layout/page {:extra-classes :is-fluid}

           [rc/v-box :src (rc/at)
            :gap "5px"
            :children [[grid/component
                        :widgets widgets
                        :layout layout
                        :container-id container-id]
                       [widget-tools widgets layout default-widgets default-layout]]]]


          [rc/alert-box :src (rc/at)
           :alert-type :info
           :heading "Waiting for (demo) Log-in"])
        '[grid/component
          :widgets widgets
          :layout layout
          :container-id container-id]))))
