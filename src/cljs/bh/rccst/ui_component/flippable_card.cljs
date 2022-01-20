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

(defn card [& {:keys [front back style]}]
  [:> FlippingCard {:style (or style default-style)}
   [:> FlippingCardFront front]
   [:> FlippingCardBack back]])
