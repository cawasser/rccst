(ns bh.rccst.views.catalog.example.multi-select-input
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.utils :as adu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Select input with multiple selection."
                "If you set the \":multiple?\" option to true, you can display a select list that allows more than
                one option item to be selected at a time."
                [layout/padded {:extra-classes :height-12}
                 [forms/select-input {:id                          "select-demo-3"
                                      :multiple?                   true
                                      :size                        5
                                      :none-value                  "--Choose one or more species--"
                                      :component-data-path         [:catalog :forms-demo :select-3]
                                      :subscribe-to-component-data [:forms-demo/demo :select-3]
                                      :subscribe-to-option-items   (atom ["House cat" "Lion" "Puma" "Jaguar" "Tiger"
                                                                          "Ocelot" "Panther" "Lynx" "Bobcat"])
                                      :on-change                   (fn [_ new-val] (js/console.log "Demo 3, Selected %o" (adu/js-event-val new-val)))}]]
                '[layout/padded
                  [forms/select-input {:id                          "select-demo-3"
                                       :multiple?                   true
                                       :size                        5
                                       :none-value                  "--Choose one or more species--"
                                       :component-data-path         [:catalog :forms-demo :select-3]
                                       :subscribe-to-component-data [:forms-demo/demo :select-3]
                                       :subscribe-to-option-items   (atom ["House cat" "Lion" "Puma" "Jaguar" "Tiger"
                                                                           "Ocelot" "Panther" "Lynx" "Bobcat"])
                                       :on-change                   (fn [_ new-val] (fn [_ new-val] (js/console.log "Demo 3, Selected %o" (adu/js-event-val new-val))))}]]))