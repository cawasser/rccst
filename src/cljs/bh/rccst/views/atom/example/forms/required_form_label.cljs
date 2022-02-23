(ns bh.rccst.views.atom.example.forms.required-form-label
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.forms :as forms]
            [woolybear.ad.layout :as layout]))


(defn example []
  (acu/demo "Form field labels (required)"
    "Form field label components also support a :required? option which, if present and truthy,
    will add a red asterisk to the label to mark the corresponding field as required."
    [layout/padded
     [forms/label {:required? true} "Required Label"]]
    '[layout/padded
      [forms/label {:required? true} "Required Label"]]))