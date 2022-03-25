(ns bh.rccst.views.atom.diagrams
  (:require [bh.rccst.views.atom.example.diagram.flow :as flow]
            [bh.rccst.views.atom.example.diagram.editable-digraph :as editable]))


(defn examples []
  [:div
   ;[flow/example]
   [editable/example]])
