(ns bh.rccst.views.atom.example.diagram.editable-digraph
  (:require [bh.rccst.ui-component.atom.diagram.editable-digraph :as diagraph]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "editable-flow-diagram-demo"]
    [:<>
     [example/component-example
      :widget-id container-id
      :title "Editable Digraph"
      :description "An Editable Digraph,  built using [react-flow](https://reactflow.dev)"
      :data diagraph/sample-data
      :component diagraph/component
      :component-id (utils/path->keyword container-id "editable-digraph")
      :source-code diagraph/source-code]]))

     ;[example/component-example
     ; :widget-id container-id
     ; :title "Editable Digraph (more data)"
     ; :description "An Editable Digraph,  built using [react-flow](https://reactflow.dev)"
     ; :data diagraph/sample-data-2
     ; :extra-params {:node-types diagraph/default-node-types
     ;                :minimap-styles diagraph/default-minimap-styles}
     ; :component diagraph/component
     ; :component-id (utils/path->keyword container-id "editable-digraph-2")
     ; :source-code diagraph/source-code]
     ;
     ;[example/component-example
     ; :widget-id container-id
     ; :title "Editable Digraph (example data)"
     ; :description "An Editable Digraph,  built using [react-flow](https://reactflow.dev)"
     ; :data diagraph/sample-data-3
     ; :component diagraph/component
     ; :component-id (utils/path->keyword container-id "editable-digraph-3")
     ; :source-code diagraph/source-code]]))

