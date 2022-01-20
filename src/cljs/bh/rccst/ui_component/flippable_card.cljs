(ns bh.rccst.ui-component.flippable-card
  (:require ["react-ui-cards" :refer (FlippingCard FlippingCardBack FlippingCardFront)]))


(def default-background "#9CA8B3")
(def default-color "#FF")
(def default-style {:width           "100%" :height "100%"
                    :overflow        "hidden"
                    :background      default-background
                    :color           default-color
                    :display         :flex
                    :backgroundSize  "contain"
                    :flex-direction  :column
                    :justify-content :center
                    :align-items     :center})
(def default-size {:width 280 :height 400})

(defn card
  "returns a 'flippable card' react component (via [react-ui-cards](https://github.com/nukeop/react-ui-cards)).

  Works similar to [re-com](https://github.com/Day8/re-com) where the arguments are 'named` by keyword, but ***not***
  required to be inside a hash-map.
  We very cleverly combine Clojure's [variadics](https://clojure.org/guides/learn/functions#_variadic_functions) with
  [destructuring](https://clojure.org/guides/destructuring) to treat the 'exploded' key/value pairs
  as if they _had_ come from a hash-map...

  ---

  | key       | type     | description                                               |
  |:----------|:---------|-----------------------------------------------------------|
  | `:front`  | hiccup   | a reagent/react component, typically in [hiccup]() format for the front (unflipped) side of the card |
  | `:back`   | hiccup   | a reagent/react component, typically in [hiccup]() format for the back (flipped) side of the card |
  | `:style`  | hash-map | hash-map of any html/css style properties (minus the `:style` part itself, i.e., just the content part), typically used to specify the `:width` and `:height` of the card |
  "
  [& {:keys [front back style]}]
  [:> FlippingCard {:style (or style default-style)}
   [:> FlippingCardFront front]
   [:> FlippingCardBack back]])
