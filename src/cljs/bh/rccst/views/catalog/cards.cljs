(ns bh.rccst.views.catalog.cards
  (:require [bh.rccst.views.catalog.example.card :as card]))


(defn catalog []
  [:div
   [card/example]
   [card/flippable-card]])

