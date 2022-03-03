(ns bh.rccst.views.atom.cards
  (:require [bh.rccst.views.atom.example.card.card :as card]
            [bh.rccst.views.atom.example.card.flippable-card :as flippable-card]
            [bh.rccst.views.atom.example.card.image-card :as image-card]))


(defn examples []
  [:div
   [card/example]
   [image-card/example]
   [flippable-card/example]])

