(ns bh.rccst.views.catalog.buttons
  "Catalog and demonstrations of available button components."
  (:require [bh.rccst.views.catalog.example.button.simple-button :as simple-button]
            [bh.rccst.views.catalog.example.button.specialized-button :as specialized-button]
            [bh.rccst.views.catalog.example.button.icon-button :as icon-button]
            [bh.rccst.views.catalog.example.button.toggle-button :as toggle-button]))

(defn catalog
  []

  [:div

   [simple-button/example]

   [specialized-button/example]

   [icon-button/example]

   [toggle-button/example]])