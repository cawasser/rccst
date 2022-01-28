(ns bh.rccst.views.catalog.diagrams
  (:require [bh.rccst.views.catalog.example.globe.globe-component :as globe]))



(defn catalog []
  [:div "Diagrams"
   [:div
    [globe/example]]])