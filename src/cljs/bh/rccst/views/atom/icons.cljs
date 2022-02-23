(ns bh.rccst.views.atom.icons
  "Catalog and demonstrations of available icon components."
  (:require [bh.rccst.views.atom.example.icons.simple-image :as simple-image]
            [bh.rccst.views.atom.example.icons.standard-icon :as standard-icon]
            [bh.rccst.views.atom.example.icons.colored-icon :as colored-icon]
            [bh.rccst.views.atom.example.icons.small-icon :as small-icon]
            [bh.rccst.views.atom.example.icons.medium-icon :as medium-icon]
            [bh.rccst.views.atom.example.icons.large-icon :as large-icon]
            [bh.rccst.views.atom.example.icons.brand-icon :as brand-icon]
            [bh.rccst.views.atom.example.icons.clickable-icon :as clickable-icon]))


(defn examples
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



