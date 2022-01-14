(ns bh.rccst.views.catalog.re-com
  (:require [bh.rccst.views.catalog.example.table :as table]
            [bh.rccst.views.catalog.example.checkbox-re-com :as checkbox]
            [bh.rccst.views.catalog.example.progress-bar :as progress-bar]
            [bh.rccst.views.catalog.example.throbber :as throbber]
            [bh.rccst.views.catalog.example.date-picker :as date-picker]
            [bh.rccst.views.catalog.example.input-time :as input-time]
            [bh.rccst.views.catalog.example.alert-box :as alert-box]
            [bh.rccst.views.catalog.example.line :as line]
            [bh.rccst.views.catalog.example.radio-button :as radio-button]
            [bh.rccst.views.catalog.example.text-input-re-com :as input-text]))



(defn catalog
  []
  [:div
   [table/example]
   [checkbox/example]
   [radio-button/example]
   [input-text/example]
   [radio-button/example]
   [alert-box/example]
   [line/example]
   [progress-bar/example]
   [throbber/example]
   [date-picker/example]
   [input-time/example]])