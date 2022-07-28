(ns rccst.views.organism.ui-grid
  (:require [rccst.views.organism.ui-grid.ratom-example :as ratom-example]
            [rccst.views.organism.ui-grid.sub-example :as sub-example]))


(defn page []
  [:div
   [ratom-example/example]
   [sub-example/example]])

