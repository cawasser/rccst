(ns bh.rccst.views.molecule.example.composite.multi-chart-widget
  (:require [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.molecule.composite.multi-chart :as widget]
            [bh.rccst.ui-component.molecule.grid-widget :as grid]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.views.molecule.example.composite.multi-chart-widget")


(defn example []
  (let [container-id     "multi-chart-widget"
        logged-in?       (re-frame/subscribe [::subs/logged-in?])
        pub-sub-started? (re-frame/subscribe [::subs/pub-sub-started?])]

    (if (not @logged-in?)
      (re-frame/dispatch [:bh.rccst.events/login "test-user" "test-pwd"]))

    (fn []
      (if (and @logged-in? @pub-sub-started?)
        (acu/demo "Multiple Charts in a Widget"
          ""
          [layout/frame
           [:div {:style {:width "1000px" :height "800px"}}
            [grid/component
             :data (r/atom widget/ui-definition)
             :component-id (h/path->keyword container-id "widget")]]])
        (acu/demo
          "Multiple Charts in a Widget"
          [rc/alert-box :src (rc/at)
           :alert-type :info
           :heading "Waiting for (demo) Log-in"])))))
