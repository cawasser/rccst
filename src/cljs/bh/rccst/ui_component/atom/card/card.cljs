(ns bh.rccst.ui-component.atom.card.card)



(def source-code '[:div.card {:style (or style default-card-style)}
                   [:div.card-header
                    [:div.card-header-title title]]
                   [:div.card-content
                    (or content [:div#empty])]])

(def default-background "#9CA8B3")
(def default-color "#FF")
(def default-card-style {:width           "200px"
                         :height          "100px"
                         :overflow        "hidden"
                         :background      default-background
                         :color           default-color})


(defn card [& {:keys [style header-style title content]}]
  [:div.card {:style (or style default-card-style)}
   [:div.card-header
    [:div.card-header-title title]]

   [:div.card-content
    (or content [:div#empty])]])


(def meta-data {:component card
                :ports     {:title   :port/sink
                            :content :port/sink}})