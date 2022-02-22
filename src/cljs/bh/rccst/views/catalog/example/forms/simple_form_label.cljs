(ns bh.rccst.views.catalog.example.forms.simple-form-label
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Form field labels (simple)"
                "You can add label components as children of input components, and they will be applied
                appropriately and consistently to the fields they describe. Labels take the standard
                extra-classes and subscribe-to-classes options."
                [layout/padded
                 [forms/label "Simple Label"]]
                '[layout/padded
                  [forms/label "Simple Label"]]))