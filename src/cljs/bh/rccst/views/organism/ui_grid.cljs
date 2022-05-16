(ns bh.rccst.views.organism.ui-grid
  (:require [bh.rccst.views.organism.ui-grid.ratom-example :as ratom-example]
            [bh.rccst.views.organism.ui-grid.sub-example :as sub-example]))


(defn page []
  [:div
   [ratom-example/example]
   [sub-example/example]])

