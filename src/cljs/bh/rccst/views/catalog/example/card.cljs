(ns bh.rccst.views.catalog.example.card
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]

            [re-com.core :as rc]
            [bh.rccst.ui-component.flippable-card :as flippable-card]))

(def default-background "#9CA8B3")
(def default-color "#FF")
(def node-style-square {:width           "300" :height "500"
                        :overflow        "hidden"
                        :background      default-background
                        :color           default-color
                        :display         :flex
                        :flex-direction  :column
                        :justify-content :center
                        :align-items     :center})
(def image-style {:width        "200px" :height "200px"
                  :display      :block
                  :margin-left  :auto
                  :margin-right :auto})


(defn example []
  (acu/demo "Card"
    "A simple Card, based upon [Bulma](https://bulma.io)"
    [layout/centered
     [:div.card {:style node-style-square}
      [:div.card-image
       [:figure.image {:style image-style}
        [:img {:src "imgs/rich-hickey.jpeg" :alt "Rich Hickey"}]]]
      [:div.card-content
       [:p.title.is-4 "Rich Hickey"]
       [layout/markdown-block
        "Rich created the [Clojure]() programming language, and the [Datomic]() database."]]]]
    '[]))


(defn flippable-card []
  (acu/demo "Flippable Card"
    "A simple 'flippable' Card, using [react-ui-cards](). This card has a
    `FlippingCardFront` and a `FlippingCardBack` and flips between them when the user moves the mouse
    over the card.
    "
    [layout/centered {:extra-classes :width-50}
     [flippable-card/card
      :style (assoc node-style-square :width "300px" :height "500px")
      :front [rc/v-box
              :gap "10px"
              :children [[:img {:style image-style
                                :src   "/imgs/rich-hickey.jpeg"}]
                         [:p.title.is-4 "Rich Hickey"]]]
      :back [layout/markdown-block
             "Rich created the [Clojure]() programming language, and the [Datomic]() database."]]]))

