(ns bh.rccst.views.molecule.example.composite.multi-chart
  (:require [bh.rccst.ui-component.molecule.composite.multi-chart :as multi-chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as ui-utils]
            [taoensso.timbre :as log]))

(log/info "bh.rccst.views.molecule.example.composite.multi-chart")


(defn example []
  (let [container-id "multi-chart-demo"]
    [example/component-example
     :title "Composite with Multiple Charts"
     :widget-id container-id
     :description "Working out how to place multiple components into a single composite so they can exchange information."
     :data multi-chart/sample-data
     :component multi-chart/component
     :component-id (ui-utils/path->keyword container-id "multi-chart")
     :source-code multi-chart/source-code]))

