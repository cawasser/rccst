(ns bh.rccst.views.molecule.example.composite.chart-remote-data
  (:require [bh.rccst.subs :as subs]
            [bh.rccst.ui-component.molecule.composite.chart-remote-data :as chart-remote-data]
            [bh.rccst.ui-component.molecule.grid-widget :as grid]
            [bh.rccst.ui-component.utils.helpers :as h]
            [re-com.core :as rc]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(log/info "bh.rccst.views.molecule.example.composite.chart-remote-data")


(defn example []
  (let [container-id     "chart-remote-data-demo"
        logged-in?       (re-frame/subscribe [::subs/logged-in?])
        pub-sub-started? (re-frame/subscribe [::subs/pub-sub-started?])]

    (if (not @logged-in?)
      (re-frame/dispatch [:bh.rccst.events/login "test-user" "test-pwd"]))

    (fn []
      (if (and @logged-in? @pub-sub-started?)
        (acu/demo "Bar chart of remote data"
          "This example shows a Bar Chart displaying data via a subscription to the Server"
          [layout/frame
           ;;
           ;; NOTE: the :height MUST be specified here since the ResponsiveContainer down in bowels of the chart needs a height
           ;; in order to actually draw the Recharts components. just saying "100%" doesn't work, since the
           ;; that really means "be as big as you need" and ResponsiveContainer then doesn't know what to do.
           ;;
           [:div {:style {:width "100%" :height "800px"}}
            [grid/component
             :data (r/atom chart-remote-data/ui-definition)
             :component-id (h/path->keyword container-id "widget")]]]
          chart-remote-data/source-code)
        (acu/demo
          "Bar chart of remote data"
          [rc/alert-box :src (rc/at)
           :alert-type :info
           :heading "Waiting for (demo) Log-in"]
          chart-remote-data/source-code)))))


