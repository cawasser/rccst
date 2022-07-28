(ns rccst.views.atom.example.forms.disabled-select-input
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.forms :as forms]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Disabled select input"
    "Pass the \"disabled\" status via the :subscribe-to-disabled? key"
    [layout/padded
     [forms/label "Choose one:"]
     [forms/select-input {:id                          "select-demo-3"
                          :component-data-path         [:catalog :forms-demo :select-3]
                          :subscribe-to-component-data [:forms-demo/demo :select-3]
                          :subscribe-to-option-items   (atom ["Pikachu" "Bulbasaur" "Squirtle"])
                          :subscribe-to-disabled?      (atom true)}]]
    '[layout/padded
      [forms/label "Choose one:"]
      [forms/select-input {:id                          "select-demo-3"
                           :component-data-path         [:catalog :forms-demo :select-3]
                           :subscribe-to-component-data [:forms-demo/demo :select-3]
                           :subscribe-to-option-items   (atom ["Pikachu" "Bulbasaur" "Squirtle"])
                           :subscribe-to-disabled?      (atom true)}]]))