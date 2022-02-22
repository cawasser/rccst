(ns bh.rccst.views.catalog.all
  "Catalog and acu/demonstrations of all available UI components."
  (:require [re-com.core :as rc]

            [bh.rccst.views.catalog.example.layout.page :as page]
            [bh.rccst.views.catalog.example.layout.page-header :as page-header]
            [bh.rccst.views.catalog.example.layout.page-title :as page-title]
            [bh.rccst.views.catalog.example.layout.text-block :as text-block]
            [bh.rccst.views.catalog.example.layout.centered-block :as centered-block]
            [bh.rccst.views.catalog.example.layout.markdown-block :as markdown-block]
            [bh.rccst.views.catalog.example.layout.frame :as frame]
            [bh.rccst.views.catalog.example.layout.section :as section]

            [bh.rccst.views.catalog.example.container.navbar :as navbar]

            [bh.rccst.views.catalog.example.icons.simple-image :as simple-image]
            [bh.rccst.views.catalog.example.icons.standard-icon :as standard-icon]
            [bh.rccst.views.catalog.example.icons.colored-icon :as colored-icon]
            [bh.rccst.views.catalog.example.icons.small-icon :as small-icon]
            [bh.rccst.views.catalog.example.icons.medium-icon :as medium-icon]
            [bh.rccst.views.catalog.example.icons.large-icon :as large-icon]
            [bh.rccst.views.catalog.example.icons.brand-icon :as brand-icon]
            [bh.rccst.views.catalog.example.icons.clickable-icon :as clickable-icon]



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
            [bh.rccst.views.catalog.example.forms.disabled-checkbox :as disabled-checkbox]

            [bh.rccst.views.catalog.example.chart.bar-chart :as bar-chart]
            [bh.rccst.views.catalog.example.chart.line-chart :as line-chart]
            [bh.rccst.views.catalog.example.chart.pie-chart :as pie-chart]
            [bh.rccst.views.catalog.example.chart.colored-pie-chart :as colored-piechart]

            [bh.rccst.views.catalog.example.re-com.table :as table]
            [bh.rccst.views.catalog.example.re-com.alert-box :as alert-box]
            [bh.rccst.views.catalog.example.re-com.line :as line]))



(defn catalog
  []
  [:div

   [page/example]
   [page-header/example]
   [page-title/example]
   [section/example]
   [text-block/example]
   [centered-block/example]
   [markdown-block/example]
   [frame/example]
   [navbar/example]

   [rc/line :size "5px" :color "orange"]

   [bar-chart/example]
   [line-chart/simple-example]
   [pie-chart/example]
   [colored-piechart/example]

   [rc/line :size "5px" :color "orange"]

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
   [disabled-checkbox/example]

   [rc/line :size "2px" :color "orange"]

   [table/example]
   [alert-box/example]
   [line/example]
   [simple-image/example]
   [standard-icon/example]
   [colored-icon/example]
   [small-icon/example]
   [medium-icon/example]
   [large-icon/example]
   [brand-icon/example]
   [clickable-icon/example]])





