(ns bh.rccst.views.molecule.example.composite.coverage-plan
  (:require [bh.rccst.ui-component.molecule.composite.coverage-plan :as coverage-plan]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as ui-utils]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.molecule.example.composite.coverage-plan")


(defn example []
  (let [container-id "coverage-plan-demo"]
    [example/component-example
     :title "Coverage Plan "
     :widget-id container-id
     :description "First exercise in our new COMPOSABLE UI"
     :data coverage-plan/sample-data
     :component coverage-plan/component
     :component-id (ui-utils/path->keyword container-id "multi-chart")
     :source-code coverage-plan/source-code]))