(ns bh.rccst.views.atom.re-com
  (:require [bh.rccst.views.atom.example.re-com.table :as table]
            [bh.rccst.views.atom.example.re-com.checkbox-re-com :as checkbox]
            [bh.rccst.views.atom.example.re-com.progress-bar :as progress-bar]
            [bh.rccst.views.atom.example.re-com.throbber :as throbber]
            [bh.rccst.views.atom.example.re-com.date-picker :as date-picker]
            [bh.rccst.views.atom.example.re-com.input-time :as input-time]
            [bh.rccst.views.atom.example.re-com.alert-box :as alert-box]
            [bh.rccst.views.atom.example.re-com.line :as line]
            [bh.rccst.views.atom.example.re-com.radio-button :as radio-button]
            [bh.rccst.views.atom.example.re-com.text-input-re-com :as input-text]
            [bh.rccst.views.atom.example.re-com.slider :as slider]
            [bh.rccst.views.atom.example.re-com.popover :as popover]
            [bh.rccst.views.atom.example.re-com.splitter :as splitter]))


(defn examples
  []
  [:div
   [table/example]
   [checkbox/example]
   [radio-button/example]
   [input-text/example]
   [alert-box/example]
   [line/example]
   [progress-bar/example]
   [throbber/example]
   [date-picker/example]
   [input-time/example]
   [slider/example]
   [popover/button-anchor-example]
   [popover/link-anchor-example]
   [popover/chart-example]
   [splitter/horizontal-example]
   [splitter/example]
   [splitter/vertical-example]])