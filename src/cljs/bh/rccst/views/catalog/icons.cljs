(ns bh.rccst.views.catalog.icons
  "Catalog and demonstrations of available icon components."
  (:require [bh.rccst.views.catalog.example.simple-image :as simple-image]
            [bh.rccst.views.catalog.example.standard-icon :as standard-icon]
            [bh.rccst.views.catalog.example.colored-icon :as colored-icon]
            [bh.rccst.views.catalog.example.small-icon :as small-icon]
            [bh.rccst.views.catalog.example.medium-icon :as medium-icon]
            [bh.rccst.views.catalog.example.large-icon :as large-icon]
            [bh.rccst.views.catalog.example.brand-icon :as brand-icon]
            [bh.rccst.views.catalog.example.clickable-icon :as clickable-icon]

            [bh.rccst.views.catalog.example.color-picker :as picker]))


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
   [clickable-icon/example]

   [picker/example]])



