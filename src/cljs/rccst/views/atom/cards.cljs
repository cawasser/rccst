(ns rccst.views.atom.cards
  (:require [rccst.views.atom.example.card.card :as card]
            [rccst.views.atom.example.card.flippable-card :as flippable-card]
            [rccst.views.atom.example.card.image-card :as image-card]))


(defn examples []
  [:div
   [card/example]
   [image-card/example]
   [flippable-card/example]])

