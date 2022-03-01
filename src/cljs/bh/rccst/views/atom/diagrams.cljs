(ns bh.rccst.views.atom.diagrams
  (:require [bh.rccst.views.atom.example.diagram.flow :as flow]))


(defn examples []
  [:div "Diagrams"
   [flow/example]])
