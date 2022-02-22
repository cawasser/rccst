(ns bh.rccst.views.catalog.example.forms.password-input
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Password input"
                "If you use a text-input component with a :type option of :password, the field will display
                a standard password input instead of a regular text input."

                [layout/padded
                 [forms/field-group
                  [forms/label {:for "demo-2"} "Password Input"]
                  [forms/text-input {:id                          "demo-2"
                                     :type                        :password
                                     :component-data-path         [:catalog :forms-demo 2]
                                     :subscribe-to-component-data [:forms-demo/demo 2]}]]]
                '[layout/padded
                  [forms/field-group
                   [forms/label {:for "demo-2"} "Password Input"]
                   [forms/text-input {:id                          "demo-2"
                                      :type                        :password
                                      :component-data-path         [:catalog :forms-demo 2]
                                      :subscribe-to-component-data [:forms-demo/demo 2]}]]]))