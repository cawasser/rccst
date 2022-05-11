(ns bh.rccst.views.template.ui-grid
  (:require [bh.rccst.views.template.ui-grid.ratom-example :as ratom-example]))


(defn page []
  [:div
   [ratom-example/example]])

