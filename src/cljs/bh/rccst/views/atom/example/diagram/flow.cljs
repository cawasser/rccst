(ns bh.rccst.views.atom.example.diagram.flow
  (:require [bh.rccst.ui-component.atom.diagram.flow :as flow]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "flow-diagram-demo"]
    [example/component-example
     :container-id container-id
     :title "Flow Diagram"
     :description "A simple Flow Diagram built using [react-flow](https://reactflow.dev)"
     :data flow/sample-data
     :component flow/component
     :component-id (utils/path->keyword container-id "flow-diagram")
     :source-code flow/source-code]))