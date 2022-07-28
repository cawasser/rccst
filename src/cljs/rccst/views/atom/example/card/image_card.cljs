(ns rccst.views.atom.example.card.image-card
  (:require [bh.ui-component.atom.card.image-card :as card]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(def default-background "#9CA8B3")
(def default-color "#FF")
(def node-style-square {:width           "300px" :height "500px"
                        :overflow        "hidden"
                        :background      default-background
                        :color           default-color
                        :justify-content :center
                        :align-items     :center
                        :margin          :auto})
(def image-style {:width        "200px" :height "200px"
                  :display      :block
                  :margin-left  :auto
                  :margin-right :auto})


(defn example []
  (acu/demo "Image Card"
    "A Card with an image, based upon [Bulma](https://bulma.io)"
    [layout/centered {:extra-classes :width-50}
     [card/card
      :title "Rich Hickey"
      :image "imgs/giants/rich-hickey.jpeg"
      :content [layout/markdown-block
                "Rich created the [Clojure](https://clojure.org) programming language, and the [Datomic](https://www.datomic.com) database."]
      :style node-style-square
      :image-style image-style]]
    card/source-code))

