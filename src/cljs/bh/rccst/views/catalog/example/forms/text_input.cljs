(ns bh.rccst.views.catalog.example.forms.text-input
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Simple text input"
                "Text input components are \"self-aware,\" meaning that when you create a text input, you pass it the
                path to its component data inside the app-db. The component uses this information to initialize its
                internal state, and to provide built-in functionality including on-change event handling and flags for
                :visited? and :active? that can be used in form validation. As a second required option, you must also
                provide a subscription that returns this component data.

                Text inputs support a number of additional options, including the standard :extra-classes and
                :subscribe-to-classes options. See `(doc woolybear.ad.forms/text-input)` for details, or refer to this
                example and those that follow."

                [layout/padded
                 [forms/field-group
                  [forms/label {:for "demo-1"} "Simple Text Input"]
                  [forms/text-input {:id                          "demo-1"
                                     :component-data-path         [:catalog :forms-demo 1]
                                     :subscribe-to-component-data [:forms-demo/demo 1]}]]]
                '[layout/padded
                  [forms/field-group
                   [forms/label {:for "demo-1"} "Simple Text Input"]
                   [forms/text-input {:id                          "demo-1"
                                      :component-data-path         [:catalog :forms-demo 1]
                                      :subscribe-to-component-data [:forms-demo/demo 1]}]]]))
