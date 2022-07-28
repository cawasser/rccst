(ns rccst.views.atom.layouts
  "Catalog and acu/demonstrations of available layout components."
  (:require [rccst.views.atom.example.layout.page :as page]
            [rccst.views.atom.example.layout.page-header :as page-header]
            [rccst.views.atom.example.layout.page-title :as page-title]
            [rccst.views.atom.example.layout.text-block :as text-block]
            [rccst.views.atom.example.layout.centered-block :as centered-block]
            [rccst.views.atom.example.layout.markdown-block :as markdown-block]
            [rccst.views.atom.example.layout.frame :as frame]
            [rccst.views.atom.example.layout.section :as section]
            [rccst.views.atom.example.layout.layout-grid :as layout-grid]))


(defn examples
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
