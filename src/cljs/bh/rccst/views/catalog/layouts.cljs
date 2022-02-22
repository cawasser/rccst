(ns bh.rccst.views.catalog.layouts
  "Catalog and acu/demonstrations of available layout components."
  (:require [bh.rccst.views.catalog.example.layout.page :as page]
            [bh.rccst.views.catalog.example.layout.page-header :as page-header]
            [bh.rccst.views.catalog.example.layout.page-title :as page-title]
            [bh.rccst.views.catalog.example.layout.text-block :as text-block]
            [bh.rccst.views.catalog.example.layout.centered-block :as centered-block]
            [bh.rccst.views.catalog.example.layout.markdown-block :as markdown-block]
            [bh.rccst.views.catalog.example.layout.frame :as frame]
            [bh.rccst.views.catalog.example.layout.section :as section]
            [bh.rccst.views.catalog.example.layout.layout-grid :as layout-grid]))


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

   [layout-grid/example]])
