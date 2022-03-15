(ns bh.rccst.views.atom.example.re-com.label
  (:require [bh.rccst.ui-component.atom.re-com.label :as label]
            [re-com.core :as rc]
            [reagent.core :as r]
            [woolybear.ad.catalog.utils :as acu]))


(defn- label []
  [label/label :value (r/atom "This is a \"regular\" label")])


(defn- label-sm []
  [label/label-sm :value (r/atom "This is a \"small\" label")])


(defn- label-md []
  [label/label-md :value (r/atom "This is a \"medium\" label")])


(defn- label-lg []
  [label/label-lg :value (r/atom "This is a \"large\" label")])


(defn- label-hg []
  [label/label-hg :value (r/atom "This is a \"huge\" label")])


(defn example []
  (acu/demo
    "Labels"
    [rc/v-box :src (rc/at)
     :gap "5px"
     :children [[label]
                [label-sm]
                [label-md]
                [label-lg]
                [label-hg]]]
    '[:div
      [label/label :value (r/atom "This is a \"regular\" label")]
      [label/label-sm :value (r/atom "This is a \"small\" label")]
      [label/label-md :value (r/atom "This is a \"medium\" label")]
      [label/label-lg :value (r/atom "This is a \"large\" label")]
      [label/label-hg :value (r/atom "This is a \"huge\" label")]]))

