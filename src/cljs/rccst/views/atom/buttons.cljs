(ns rccst.views.atom.buttons
  "Catalog and demonstrations of available button components."
  (:require [rccst.views.atom.example.button.simple-button :as simple-button]
            [rccst.views.atom.example.button.specialized-button :as specialized-button]
            [rccst.views.atom.example.button.icon-button :as icon-button]
            [rccst.views.atom.example.button.toggle-button :as toggle-button]))

(defn examples
  []

  [:div
   [simple-button/example]
   [specialized-button/example]
   [icon-button/example]
   [toggle-button/example]])
