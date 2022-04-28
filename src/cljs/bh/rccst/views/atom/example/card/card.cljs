(ns bh.rccst.views.atom.example.card.card
  (:require [bh.rccst.ui-component.atom.card.card :as card]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))

(def default-background "#9CA8B3")
(def default-color "#FF")
(def node-style {:width      "300px" :height "300px"
                 :margin     :auto
                 :background default-background
                 :color      default-color})


(defn example []
  (acu/demo "Card"
    "A simple Card, based upon [Bulma](https://bulma.io)"
    [layout/centered {:extra-classes :width-50}
     [card/card
      :style node-style
      :title "Rich Hickey"
      :content [layout/markdown-block
                "Rich created the [Clojure]() programming language, and the [Datomic]() database."]]]
    card/source-code))




