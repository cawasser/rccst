(ns bh.rccst.views.catalog.example.forms.disabled-checkbox
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Disabled checkbox"
                "Disabled checkbox component."
                [layout/padded
                 [forms/field-group
                  [forms/checkbox {:id                          "checkbox-demo-2"
                                   :name                        "checkbox-demo-2"
                                   :subscribe-to-disabled?      (atom true)
                                   :component-data-path         [:catalog :forms-demo :checkbox-2]
                                   :subscribe-to-component-data [:forms-demo/demo :checkbox-2]
                                   :on-change                   #(js/console.log "Checkbox toggled")}
                   "Send me more junk email"]]]
                '[layout/padded
                  [forms/field-group
                   [forms/checkbox {:id                          "checkbox-demo-2"
                                    :name                        "checkbox-demo-2"
                                    :subscribe-to-disabled?      (atom true)
                                    :component-data-path         [:catalog :forms-demo :checkbox-2]
                                    :subscribe-to-component-data [:forms-demo/demo :checkbox-2]
                                    :on-change                   #(js/console.log "Checkbox toggled")}
                    "Send me more junk email"]]]))