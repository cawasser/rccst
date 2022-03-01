(ns bh.rccst.views.atom.cards
  (:require [bh.rccst.views.atom.example.card.card :as card]))


(defn examples []
  [:div
   [card/example]
   [card/flippable-card]])

