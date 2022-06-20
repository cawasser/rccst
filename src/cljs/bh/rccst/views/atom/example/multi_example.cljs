(ns bh.rccst.views.atom.example.multi-example
  (:require [reagent.core :as r]
            [re-com.core :as rc]))



(defn examples [the-examples]
  (let [buttons (->> the-examples
                  (map (fn [[label _]]
                         {:id label :tooltip label :label label}))
                  (into []))
        selected? (r/atom (-> the-examples keys first))]

    (fn []
      [:div.box {:style {:width "100%" :height "100%" :background "#faeee8"}}
       [rc/v-box :src (rc/at)
        :justify :end
        :width "100%"
        :height "100%"
        :gap "5px"
        :children [[rc/h-box :src (rc/at)
                    :justify :end
                    :children [[rc/horizontal-bar-tabs
                                :model selected?
                                :tabs buttons
                                :on-change #(reset! selected? %)]]]
                   (get the-examples @selected?)]]])))



(comment
  (def the-examples {"data-ratom" [:div "one"]
                     "data-struct" [:div "two"]
                     "data-sub" [:div "three"]
                     "config-ratom" [:div "four"]
                     "config-struct" [:div "five"]
                     "config-sub" [:div "six"]
                     "dummy-ratom" [:div "seven"]})

  (->> the-examples
    (map (fn [[label component]]
           {:id label :tooltip label :label label}))
    (into []))

  (def selected? (r/atom (-> the-examples keys first)))

  (get the-examples @selected?)


  ())

