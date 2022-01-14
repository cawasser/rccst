(ns bh.rccst.views.catalog.example.placeholder-text-input
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Text input with placeholder"
                "Use a placeholder instead of a label if you like."
                [layout/padded
                 [forms/text-input {:id                          "demo-3"
                                    :placeholder                 "your-email@your-company.com"
                                    :component-data-path         [:catalog :forms-demo 3]
                                    :subscribe-to-component-data [:forms-demo/demo 3]}]]
                '[layout/padded
                  [forms/text-input {:id                          "demo-3"
                                     :placeholder                 "your-email@your-company.com"
                                     :component-data-path         [:catalog :forms-demo 3]
                                     :subscribe-to-component-data [:forms-demo/demo 3]}]]))
