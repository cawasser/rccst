(ns bh.rccst.ui-component.layout-grid
  (:require [taoensso.timbre :as log]
            ["react-grid-layout" :as ReactGridLayout]))



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
  [& {:keys [id children layout layoutFn cols width rowHeight compactType]
      :as args}]
  ;(log/info "grid" id children layout layoutFn)
  (into [:> ReactGridLayout {:id             id
                             :layout         @layout
                             :compactType    (or compactType nil)
                             :cols           (or @cols 12)
                             :width          (or width 600)
                             :rowHeight      (or rowHeight 25)
                             :onLayoutChange (or layoutFn #())}]
    children))


