(ns bh.rccst.views.catalog.example.forms.error-text-input
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Text input with errors"
                "To validate form fields, create a subscription that subscribes to the field's component data.
                This will give you a map with the keys :value (current value), :visited? (whether the field
                has ever been interacted with), :active? (whether the field currently has the focus), :path
                (self-referential path), and :original-value (value passed to the field when it was first
                created). Use these values to return a vector of error messages, or nil if no errors. Each
                error message should be a map with the keys :msg and :class, where the :msg value is the
                text string to display, and :class is either :error, :warning, or :info."

                [layout/padded
                 [forms/field-group
                  [forms/label {:for "demo-4-a"} "Field with errors"]
                  [forms/text-input {:id                          "demo-4-a"
                                     :subscribe-to-errors         (atom [{:msg "This is an error message" :class :error}])
                                     :component-data-path         [:catalog :forms-demo "4a"]
                                     :subscribe-to-component-data [:forms-demo/demo "4a"]}]]
                 [forms/field-group
                  [forms/label {:for "demo-4-b"} "Field with warning"]
                  [forms/text-input {:id                          "demo-4-b"
                                     :subscribe-to-errors         (atom [{:msg "This is warning" :class :warning}])
                                     :component-data-path         [:catalog :forms-demo "4b"]
                                     :subscribe-to-component-data [:forms-demo/demo "4b"]}]]
                 [forms/field-group
                  [forms/label {:for "demo-4-c"} "Field with info"]
                  [forms/text-input {:id                          "demo-4-c"
                                     :subscribe-to-errors         (atom [{:msg "This is just FYI" :class :info}])
                                     :component-data-path         [:catalog :forms-demo "4c"]
                                     :subscribe-to-component-data [:forms-demo/demo "4c"]}]]]
                '[layout/padded
                  [forms/field-group
                   [forms/label {:for "demo-4-a"} "Field with errors"]
                   [forms/text-input {:id                          "demo-4-a"
                                      :subscribe-to-errors         (atom [{:msg "This is an error message" :class :error}])
                                      :component-data-path         [:catalog :forms-demo "4a"]
                                      :subscribe-to-component-data [:forms-demo/demo "4a"]}]]
                  [forms/field-group
                   [forms/label {:for "demo-4-b"} "Field with warning"]
                   [forms/text-input {:id                          "demo-4-b"
                                      :subscribe-to-errors         (atom [{:msg "This is warning" :class :warning}])
                                      :component-data-path         [:catalog :forms-demo "4b"]
                                      :subscribe-to-component-data [:forms-demo/demo "4b"]}]]
                  [forms/field-group
                   [forms/label {:for "demo-4-c"} "Field with info"]
                   [forms/text-input {:id                          "demo-4-c"
                                      :subscribe-to-errors         (atom [{:msg "This is just FYI" :class :info}])
                                      :component-data-path         [:catalog :forms-demo "4c"]
                                      :subscribe-to-component-data [:forms-demo/demo "4c"]}]]]))