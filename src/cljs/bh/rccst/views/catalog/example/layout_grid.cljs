(ns bh.rccst.views.catalog.example.layout-grid
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.icons :as icons]
            [re-com.core :as rc]
            [reagent.core :as r]

            [bh.rccst.ui-component.layout-grid :as layout-grid]))



(defn- make-widget [[id icon bk-color txt-color]]
  [:div.grid-toolbar
   {:key id :style {:border           :solid
                    :background-color bk-color
                    :color            txt-color}}
   [layout/centered
    [rc/h-box
     :gap "5px"
     :children [[icons/icon {:icon icon :size :small}]
                [layout/markdown-block id]]]]])


(defn example []
  (let [widgets [["one" "anchor" :green :white]
                 ["two" "globe-americas" :blue :white]
                 ["three" "helicopter" :yellow :black]]
        layout (r/atom [{:i "one" :x 0 :y 0 :w 2 :h 3 :static true}
                        {:i "two" :x 1 :y 0 :w 3 :h 2}
                        {:i "three" :x 4 :y 0 :w 4 :h 2}])
        cols (r/atom 12)]
    (acu/demo "Layout Grid"
      "Provides a grid-based layout manager for other content (`:div`)"
      [layout/frame
       [layout-grid/grid :id "layout-grid-example"
        :children (map make-widget widgets)
        :cols cols
        :layout layout]]

      '[layout/frame
        [layout/text-block "Here is a text block"]])))
