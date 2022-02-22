(ns bh.rccst.views.catalog.forms
  "Catalog and acu/demonstrations of available form and form-field components."
  (:require [re-frame.core :as re-frame]
            [bh.rccst.views.catalog.example.forms.simple-form-label :as simple-label]
            [bh.rccst.views.catalog.example.forms.required-form-label :as required-label]
            [bh.rccst.views.catalog.example.forms.text-input :as text-input]
            [bh.rccst.views.catalog.example.forms.password-input :as pass-input]
            [bh.rccst.views.catalog.example.forms.placeholder-text-input :as place-text-input]
            [bh.rccst.views.catalog.example.forms.disabled-text-input :as disabled-text-input]
            [bh.rccst.views.catalog.example.forms.error-text-input :as error-text-input]
            [bh.rccst.views.catalog.example.forms.simple-select-input :as simple-select-input]
            [bh.rccst.views.catalog.example.forms.custom-select-input :as custom-select-input]
            [bh.rccst.views.catalog.example.forms.disabled-select-input :as disabled-select-input]
            [bh.rccst.views.catalog.example.forms.multi-select-input :as multi-select-input]
            [bh.rccst.views.catalog.example.forms.checkbox :as checkbox]
            [bh.rccst.views.catalog.example.forms.disabled-checkbox :as disabled-checkbox]))


(re-frame/reg-sub
  :catalog/forms-demo
  :<- [:db/catalog]
  (fn [catalog]
    (:forms-demo catalog)))


(re-frame/reg-sub
  :forms-demo/demo
  :<- [:catalog/forms-demo]
  (fn [forms-demo [_ ix]]
    (get forms-demo ix)))


(defn catalog
  []

  [:div

   [simple-label/example]

   [required-label/example]

   [text-input/example]

   [pass-input/example]

   [place-text-input/example]

   [disabled-text-input/example]

   [error-text-input/example]

   [simple-select-input/example]

   [custom-select-input/example]

   [disabled-select-input/example]

   [multi-select-input/example]

   [checkbox/example]

   [disabled-checkbox/example]])
