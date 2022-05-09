(ns bh.rccst.views.template.ui-grid
  (:require [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.atom.template.ui-grid :as grid]
            [bh.rccst.ui-component.molecule.composite.chart-remote-data :as chart-remote-data]
            [bh.rccst.ui-component.molecule.composite.coverage-plan :as coverage-plan]
            [bh.rccst.ui-component.molecule.composite.simple-multi-chart :as simple-multi-chart]
            [bh.rccst.ui-component.molecule.composite.simple-multi-chart-2 :as simple-multi-chart-2]
            [bh.rccst.ui-component.molecule.grid-container :as grid-container]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.views.template.ui-grid")


(def container-id "ui-grid-demo")

(def bar-chart-widget [:bar-chart "Bar Chart"
                       [grid-container/component
                        :data (r/atom chart-remote-data/ui-definition)
                        :component-id (h/path->keyword container-id "bar-chart")
                        :resizable true]
                       :green :white])
(def multi-chart-widget [:multi-chart "Multi-Chart"
                         [grid-container/component
                          :data (r/atom simple-multi-chart/ui-definition)
                          :component-id (h/path->keyword container-id "multi-chart")
                          :resizable true]
                         :blue :white])
(def multi-chart-2-widget [:multi-chart-2 "Multi-Chart-2"
                           [grid-container/component
                            :data (r/atom simple-multi-chart-2/ui-definition)
                            :component-id (h/path->keyword container-id "multi-chart-2")
                            :resizable true]
                           :rebeccapurple :white])
(def coverage-plan-widget [:coverage-plan "Coverage Plan"
                           [grid-container/component
                            :data (r/atom coverage-plan/ui-definition)
                            :component-id (h/path->keyword container-id "coverage-plan")
                            :resizable true]
                           :yellow :black])
(def default-widgets [bar-chart-widget])

(def bar-chart-layout {:i :bar-chart :x 0 :y 0 :w 8 :h 15})
(def multi-chart-layout {:i :multi-chart :x 0 :y 10 :w 8 :h 15})
(def multi-chart-2-layout {:i :multi-chart-2 :x 8 :y 21 :w 12 :h 15})
(def coverage-plan-layout {:i :coverage-plan :x 8 :y 0 :w 12 :h 21})
(def default-layout [bar-chart-layout])


(def example-widgets {:widgets default-widgets
                      :layout  default-layout})


(def widgets (r/atom example-widgets))


(defn- grid-reset [widgets widget-val layout-val]
  (reset! widgets
    {:widgets widget-val
     :layout layout-val}))


(defn- grid-update [widgets widget-val layout-val]
  (swap! widgets assoc
    :widgets (conj (:widgets @widgets) widget-val)
    :layout (conj (:layout @widgets) layout-val)))


(defn- widget-tools [widgets default-widgets]
  [rc/h-box :src (rc/at)
   :gap "10px"
   :style {:border     "1px solid" :border-radius "3px"
           :box-shadow "5px 5px 5px 2px"
           :margin     "5px" :padding "5px"}
   :children [[:label.h5 "Widgets:"]
              [rc/button :on-click #(reset! widgets []) :label "Empty"]
              [rc/button :on-click #(grid-reset widgets default-widgets default-layout)
               :label "Default"]
              [rc/button :on-click #(grid-update widgets bar-chart-widget bar-chart-layout)
               :label "Bar Chart"]
              [rc/button :on-click #(grid-update widgets multi-chart-widget multi-chart-layout)
               :label "Multi Chart"]
              [rc/button :on-click #(grid-update widgets multi-chart-2-widget multi-chart-2-layout)
               :label "Multi Chart 2"]
              [rc/button :on-click #(grid-update widgets coverage-plan-widget coverage-plan-layout)
               :label "Coverage Plan"]]])


(defn page []
  (let [logged-in?       (re-frame/subscribe [::subs/logged-in?])
        pub-sub-started? (re-frame/subscribe [::subs/pub-sub-started?])]

    (if (not @logged-in?)
      (re-frame/dispatch [:bh.rccst.events/login "test-user" "test-pwd"]))

    (fn []
      (if (and @logged-in? @pub-sub-started?)
        [layout/page {:extra-classes :is-fluid}

         [rc/v-box :src (rc/at)
          :gap "5px"
          :children [[grid/component
                      :widgets widgets
                      :container-id container-id]
                     [widget-tools widgets default-widgets default-layout]]]]


        [rc/alert-box :src (rc/at)
         :alert-type :info
         :heading "Waiting for (demo) Log-in"]))))



(comment
  (def res (h/resolve-value widgets))

  (:widgets @res)
  (:layout @res)

  (swap! widgets assoc
    :widgets (conj (:widgets @widgets) multi-chart-2-widget)
    :layout (conj (:layout @widgets) multi-chart-2-layout))

  (swap! widgets assoc
    :widgets (conj (:widgets @widgets) coverage-plan-widget)
    :layout (conj (:layout @widgets) coverage-plan-layout))



  ())