(ns bh.rccst.views.atom.example.forms.placeholder-text-input
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.forms :as forms]
            [woolybear.ad.layout :as layout]))


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
