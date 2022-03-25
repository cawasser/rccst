(ns bh.rccst.views.atom.example.diagram.editable-digraph
  (:require [bh.rccst.ui-component.atom.diagram.editable-digraph :as diagraph]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "editable-flow-diagram-demo"]
    [example/component-example
     :widget-id container-id
     :title "Editable Digraph"
     :description "An Editable Digraph,  built using [react-flow](https://reactflow.dev)"
     :data diagraph/sample-data
     :component diagraph/component
     :component-id (utils/path->keyword container-id "editable-digraph")
     :source-code diagraph/source-code]))