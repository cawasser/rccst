(ns bh.rccst.views.catalog.example.shy-block
  (:require [reagent.ratom :as ratom]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.containers :as containers]
            [woolybear.ad.buttons :as buttons]))

(defn shy-block-demo
  []
  (let [shy-block-active? (ratom/atom true)
        click-dispatcher (fn [_]
                           (swap! shy-block-active? not))]
    (fn []
      (let [active? @shy-block-active?]
        [layout/frame
         [layout/columns
          [layout/column {:extra-classes :is-one-fifth}
           [layout/padded
            [buttons/button {:on-click click-dispatcher} "Toggle Visibility"]]]
          [:div.column
           [containers/shy-block {:active? active?}
            [layout/text-block acu/lorem]]]]]))))

(defn example []
  (acu/demo "Shy block"
    "A 'shy' block is a container that is only visible when its `:active?` option
    is true. Used as a sub-component of other components such as the 'spoiler'
    panel. Takes standard `:extra-classes` and `:subscribe-to-classes` options."
    [shy-block-demo]

    '(let [active? (reagent/atom false)]
       [layout/frame
        [buttons/button {:on-click (swap! active? not)} "Toggle Visibility"]
        [containers/shy-block {:active? active?}
         [layout/text-block acu/lorem]]])))
