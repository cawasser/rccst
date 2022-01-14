(ns bh.rccst.views.catalog.example.checkbox
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Checkbox"
                "Simple checkbox component."
                [layout/padded
                 [forms/field-group
                  [forms/checkbox {:id                          "checkbox-demo-1"
                                   :name                        "checkbox-demo-1"
                                   :component-data-path         [:catalog :forms-demo :checkbox-1]
                                   :subscribe-to-component-data [:forms-demo/demo :checkbox-1]
                                   :on-change                   #(js/console.log "Checkbox toggled")}
                   "Unsubscribe from all future junk email"]]]
                '[layout/padded
                  [forms/field-group
                   [forms/checkbox {:id                          "checkbox-demo-1"
                                    :name                        "checkbox-demo-1"
                                    :component-data-path         [:catalog :forms-demo :checkbox-1]
                                    :subscribe-to-component-data [:forms-demo/demo :checkbox-1]
                                    :on-change                   #(js/console.log "Checkbox toggled")}
                    "Unsubscribe from all future junk email"]]]))
