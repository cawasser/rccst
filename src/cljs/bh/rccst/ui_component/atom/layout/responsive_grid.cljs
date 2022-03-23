(ns bh.rccst.ui-component.atom.layout.responsive-grid
  (:require ["react-grid-layout" :rename {Responsive ResponsiveGridLayout}]))


(defn grid
  "use [react-grid-layout](https://github.com/react-grid-layout/react-grid-layout) to organize a bunch of children in a draggable grid

  ---

  Parameters are keyword identified as follows:

  | keyword     | type     | description            |
  |:------------|:--------:|:-----------------------|
  | `:id`       | string   | uniquely identify this particular grid, in case you have multiples |
  | `:children` | vector   | vector of hiccup that define each child to be placed into the grid |
  | `:layout`   | atom     | atom of layout 'records' that track where each child is drawn in the grid |
  | `:layoutFn` | function | function to update the layout atom when children are resized or move |
  | `:cols`     | atom     | atom wrapping an integer, which specifies the number of 'grid columns' |

> See also
>
> [react-grid-layout](https://github.com/react-grid-layout/react-grid-layout)
>
> [re-com](https://github.com/Day8/re-com)
  "
  [& {:keys [id children layout layoutFn widthFn
             cols width rowHeight compactType
             draggableHandle draggableCancel
             isDraggable isResizable]

      :as   args}]
  ;(log/info "grid" id children layout layoutFn)
  (into [:> ResponsiveGridLayout {:id              id
                                  :layout          @layout
                                  :breakpoints     {:lg 850 :md 600 :sm 400}
                                  :cols            {:lg 20 :md 15 :sm 10} ;(or @cols 12)
                                  :width           (or width 600)
                                  :rowHeight       (or rowHeight 25)
                                  :onLayoutChange  (or layoutFn #())
                                  :onWidthChange   (or widthFn #())
                                  :isDraggable     (or isDraggable true)
                                  :isResizable     (or isResizable true)
                                  :draggableHandle (or draggableHandle ".grid-toolbar")
                                  :draggableCancel (or draggableCancel ".grid-content")
                                  :compactType     (or compactType :vertical)}]
    children))