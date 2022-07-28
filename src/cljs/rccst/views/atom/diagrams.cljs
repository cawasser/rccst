(ns rccst.views.atom.diagrams
  (:require [rccst.views.atom.example.diagram.flow :as flow]
            [rccst.views.atom.example.diagram.editable-digraph :as editable]))


(defn examples []
  [:div
   ;[flow/example]
   [editable/example]])
