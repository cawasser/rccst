(ns bh.rccst.views.molecule.example.composite.coverage-plan
  (:require [bh.rccst.ui-component.molecule.composite.coverage-plan :as coverage-plan]
            [bh.rccst.ui-component.molecule.composite :as composite]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as ui-utils]
            [re-frame.core :as re-frame]
            [re-com.core :as rc]
            [bh.rccst.subs :as subs]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]))


(log/info "bh.rccst.views.molecule.example.composite.coverage-plan")


(defn example []
  (let [container-id "coverage-plan-demo"
        logged-in? (re-frame/subscribe [::subs/logged-in?])
        pub-sub-started? (re-frame/subscribe [::subs/pub-sub-started?])]

    (if (not @logged-in?)
      (re-frame/dispatch [:bh.rccst.events/login "string" "string"]))

    (fn []
      (if (and @logged-in? @pub-sub-started?)
        [example/component-example
         :title "Coverage Plan"
         :widget-id container-id
         :description "First exercise in our new COMPOSABLE UI"
         :data coverage-plan/ui-definition
         :component composite/component
         :component-id (ui-utils/path->keyword container-id "component")
         :container-id container-id
         :source-code composite/source-code]
        (acu/demo
          "Coverage Plan"
          [rc/alert-box :src (rc/at)
           :alert-type :info
           :heading "Waiting for (demo) Log-in"])))))


(comment
  (def logged-in? (re-frame/subscribe [::subs/logged-in?]))

  (if (not @logged-in?)
    (re-frame/dispatch [:bh.rccst.events/login "string" "string"]))

  ())

