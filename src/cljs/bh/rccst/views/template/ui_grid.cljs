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

(def default-widgets
  [[:bar-chart "Bar Chart"
    [grid-container/component
     :data (r/atom chart-remote-data/ui-definition)
     :component-id (h/path->keyword container-id "bar-chart")
     :resizable true]
    :green :white]
   [:multi-chart "Multi-Chart"
    [grid-container/component
     :data (r/atom simple-multi-chart/ui-definition)
     :component-id (h/path->keyword container-id "multi-chart")
     :resizable true]
    :blue :white]
   [:coverage-plan "Coverage Plan"
    [grid-container/component
     :data (r/atom coverage-plan/ui-definition)
     :component-id (h/path->keyword container-id "coverage-plan")
     :resizable true]
    :yellow :black]])


(def default-layout [{:i :bar-chart :x 0 :y 0 :w 8 :h 15}
                     {:i :multi-chart :x 0 :y 10 :w 8 :h 15}
                     {:i :coverage-plan :x 8 :y 0 :w 12 :h 21}])


(def example-widgets {:widgets default-widgets
                      :layout  default-layout})


(def widgets (r/atom example-widgets))


(defn page []
  (let [logged-in?       (re-frame/subscribe [::subs/logged-in?])
        pub-sub-started? (re-frame/subscribe [::subs/pub-sub-started?])]

    (if (not @logged-in?)
      (re-frame/dispatch [:bh.rccst.events/login "test-user" "test-pwd"]))

    (fn []
      (if (and @logged-in? @pub-sub-started?)
        [layout/page {:extra-classes :is-fluid}

         [grid/component
          :widgets widgets
          :container-id container-id]]

        [rc/alert-box :src (rc/at)
         :alert-type :info
         :heading "Waiting for (demo) Log-in"]))))



(comment
  (def res (h/resolve-value widgets))

  (:widgets @res)
  (:layout @res)

  (swap! widgets assoc
    :widgets (conj default-widgets
               [:multi-chart-2 "Multi-Chart-2"
                [grid-container/component
                 :data (r/atom simple-multi-chart-2/ui-definition)
                 :component-id (h/path->keyword container-id "multi-chart-2")
                 :resizable true]
                :rebeccapurple :white])
    :layout (conj default-layout {:i :multi-chart-2 :x 8 :y 21 :w 12 :h 15}))


  ())