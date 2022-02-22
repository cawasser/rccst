(ns bh.rccst.views.catalog.icons
  "Catalog and demonstrations of available icon components."
  (:require [bh.rccst.views.catalog.example.icons.simple-image :as simple-image]
            [bh.rccst.views.catalog.example.icons.standard-icon :as standard-icon]
            [bh.rccst.views.catalog.example.icons.colored-icon :as colored-icon]
            [bh.rccst.views.catalog.example.icons.small-icon :as small-icon]
            [bh.rccst.views.catalog.example.icons.medium-icon :as medium-icon]
            [bh.rccst.views.catalog.example.icons.large-icon :as large-icon]
            [bh.rccst.views.catalog.example.icons.brand-icon :as brand-icon]
            [bh.rccst.views.catalog.example.icons.clickable-icon :as clickable-icon]))


(defn catalog
  []

  [:div
   [simple-image/example]
   [standard-icon/example]
   [colored-icon/example]
   [small-icon/example]
   [medium-icon/example]
   [large-icon/example]
   [brand-icon/example]
   [clickable-icon/example]])



