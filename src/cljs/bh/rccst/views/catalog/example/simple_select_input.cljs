(ns bh.rccst.views.catalog.example.simple-select-input
  (:require [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]
            [woolybear.ad.forms :as forms]))

(defn example []
      (acu/demo "Select input with simple values"
                "See `(doc select-input)` for details concerning the options you can or must pass to
                a select list. This example demonstrates using select-input with a simple list of
                strings."
                [layout/padded
                 [forms/select-input {:id                          "select-demo-1"
                                      :component-data-path         [:catalog :forms-demo :select-1]
                                      :subscribe-to-component-data [:forms-demo/demo :select-1]
                                      :subscribe-to-option-items   (atom ["Jan" "Feb" "Mar" "Apr" "May" "Jun"
                                                                          "Jul" "Aug" "Sep" "Oct" "Nov" "Dec"])}]]
                '[layout/padded
                  [forms/select-input {:id                          "select-demo-1"
                                       :component-data-path         [:catalog :forms-demo :select-1]
                                       :subscribe-to-component-data [:forms-demo/demo :select-1]
                                       :subscribe-to-option-items   (atom ["Jan" "Feb" "Mar" "Apr" "May" "Jun"
                                                                           "Jul" "Aug" "Sep" "Oct" "Nov" "Dec"])}]]))