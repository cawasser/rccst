(ns bh.rccst.views.catalog.all
  "Catalog and acu/demonstrations of all available UI components."
  (:require [bh.rccst.views.catalog.example.page :as page]
            [bh.rccst.views.catalog.example.page-header :as page-header]
            [bh.rccst.views.catalog.example.page-title :as page-title]
            [bh.rccst.views.catalog.example.text-block :as text-block]
            [bh.rccst.views.catalog.example.centered-block :as centered-block]
            [bh.rccst.views.catalog.example.markdown-block :as markdown-block]
            [bh.rccst.views.catalog.example.frame :as frame]
            [bh.rccst.views.catalog.example.section :as section]
            [bh.rccst.views.catalog.example.simple-image :as simple-image]
            [bh.rccst.views.catalog.example.standard-icon :as standard-icon]
            [bh.rccst.views.catalog.example.colored-icon :as colored-icon]
            [bh.rccst.views.catalog.example.small-icon :as small-icon]
            [bh.rccst.views.catalog.example.medium-icon :as medium-icon]
            [bh.rccst.views.catalog.example.large-icon :as large-icon]
            [bh.rccst.views.catalog.example.brand-icon :as brand-icon]
            [bh.rccst.views.catalog.example.clickable-icon :as clickable-icon]))



(defn catalog
  []
  [:div

   [page/example]

   [page-header/example]

   [page-title/example]

   [section/example]

   [text-block/example]

   [centered-block/example]

   [markdown-block/example]

   [frame/example]

   [simple-image/example]

   [standard-icon/example]

   [colored-icon/example]

   [small-icon/example]

   [medium-icon/example]

   [large-icon/example]

   [brand-icon/example]

   [clickable-icon/example]])

