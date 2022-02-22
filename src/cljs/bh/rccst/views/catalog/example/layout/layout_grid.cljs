(ns bh.rccst.views.catalog.example.layout.layout-grid
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
      "Provides a grid-based layout manager, using [react-grid-layout](https://github.com/react-grid-layout/react-grid-layout)
for other content (something like a `:div`).

In this example, Box-2 and Box-3 can be resized and dragged around the frame, but
Box-1 is fixed in size and position. This is determined by the `:layout` property passed
into the `grid`.

> See [react-grid-layout](https://github.com/react-grid-layout/react-grid-layout)
> for details on the [Grid Item Props](https://github.com/react-grid-layout/react-grid-layout#grid-item-props)
 data structure."
      [layout/frame
       [layout-grid/grid :id "layout-grid-example"
        :children (doall (map make-widget widgets))
        :cols cols
        :layout layout]]

      '[layout/frame
        (let [widgets [["one" "anchor" :green :white]
                       ["two" "globe-americas" :blue :white]
                       ["three" "helicopter" :yellow :black]]
              layout (r/atom [{:i "one" :x 0 :y 0 :w 2 :h 3 :static true}
                              {:i "two" :x 1 :y 0 :w 3 :h 2}
                              {:i "three" :x 4 :y 0 :w 4 :h 2}])
              cols (r/atom 12)]
          [layout-grid/grid :id "layout-grid-example"
           :children (doall (map make-widget widgets))
           :cols cols
           :layout layout])])))
