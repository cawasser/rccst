(ns rccst.views.atom.example.diagram.editable-digraph
  (:require [bh.ui-component.atom.diagram.diagram.composite-dag-support :as support]
            [bh.ui-component.atom.diagram.editable-digraph :as digraph]
            [bh.ui-component.molecule.example :as example]
            [bh.ui-component.utils :as utils]
            [reagent.core :as r]))


(defn example []
  (let [container-id "editable-flow-diagram-demo"]
    [:<>
     [example/component-example
      :title "Editable Digraph"
      :description "An Editable Digraph, built using [react-flow](https://reactflow.dev)"
      :data (r/atom digraph/sample-data)
      :node-types support/default-node-types
      :tool-types support/default-tool-types
      :component digraph/component
      :component-id (utils/path->keyword container-id "editable-digraph")
      :source-code digraph/source-code]

     [example/component-example
      :title "Editable Digraph using data used for building Composite UI components (ie, \"widgets\")"
      :description "An Editable Digraph, built using [react-flow](https://reactflow.dev)"
      :data (r/atom support/sample-data)
      :node-types support/default-node-types
      :tool-types support/default-tool-types
      :minimap-styles support/default-minimap-styles
      :component digraph/component
      :component-id (utils/path->keyword container-id "editable-digraph-2")
      :source-code digraph/source-code]

     [example/component-example
      :title "Editable Digraph (example data with initial auto-layout)"
      :description "An Editable Digraph, built using [react-flow](https://reactflow.dev)"
      :data (r/atom digraph/sample-data-3)
      :node-types support/default-node-types
      :tool-types support/default-tool-types
      :force-layout? true
      :component digraph/component
      :component-id (utils/path->keyword container-id "editable-digraph-3")
      :source-code digraph/source-code]]))

