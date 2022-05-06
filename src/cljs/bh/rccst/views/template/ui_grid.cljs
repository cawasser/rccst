(ns bh.rccst.views.template.ui-grid
  (:require [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.atom.layout.responsive-grid :as grid]
            [bh.rccst.ui-component.molecule.composite.chart-remote-data :as chart-remote-data]
            [bh.rccst.ui-component.molecule.composite.coverage-plan :as coverage-plan]
            [bh.rccst.ui-component.molecule.composite.simple-multi-chart :as simple-multi-chart]
            [bh.rccst.ui-component.molecule.grid-widget :as widget]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.views.template.ui-grid")


(defn example-widgets [container-id]
  [[:bar-chart "Bar Chart"
    [widget/component
     :data (r/atom chart-remote-data/ui-definition)
     :component-id (h/path->keyword container-id "bar-chart")]
    :green :white]
   [:multi-chart "Multi-Chart"
    [widget/component
     :data (r/atom simple-multi-chart/ui-definition)
     :component-id (h/path->keyword container-id "multi-chart")]
    :blue :white]
   [:coverage-plan "Coverage Plan"
    [widget/component
     :data (r/atom coverage-plan/ui-definition)
     :component-id (h/path->keyword container-id "coverage-plan")]
    :yellow :black]])


(def example-layout [{:i :bar-chart :x 0 :y 0 :w 8 :h 15}
                     {:i :multi-chart :x 0 :y 10 :w 8 :h 15}
                     {:i :coverage-plan :x 8 :y 0 :w 12 :h 21}])


(defn- make-widget [[id title content bk-color txt-color]]
  [:div.widget-parent {:key id}
   [:div.grid-toolbar.title-wrapper.move-cursor
    {:style {:background-color bk-color
             :color            txt-color}}
    title]
   [:div.widget.widget-content
    {:style         {:width       "100%"
                     :height      "90%"
                     :cursor      :default
                     :align-items :stretch
                     :display     :flex}
     :on-mouse-down #(.stopPropagation %)}
    content]])


(defn page []
  (let [container-id     "ui-grid-demo"
        cols             20
        logged-in?       (re-frame/subscribe [::subs/logged-in?])
        pub-sub-started? (re-frame/subscribe [::subs/pub-sub-started?])]

    (if (not @logged-in?)
      (re-frame/dispatch [:bh.rccst.events/login "test-user" "test-pwd"]))

    (fn []
      (if (and @logged-in? @pub-sub-started?)
        [layout/page {:extra-classes :is-fluid}

         [grid/grid :id "ui-grid-example"
          :children (doall (map make-widget (example-widgets container-id)))
          :cols cols
          :layout example-layout]]

        [rc/alert-box :src (rc/at)
         :alert-type :info
         :heading "Waiting for (demo) Log-in"]))))