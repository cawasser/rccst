(ns bh.rccst.views.catalog.example.oz.bar-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.oz.bar-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]))


(defn example []
  (let [widget-id "oz-bar-chart-demo"]
    [example/component-example
     :title "Bar Chart (oz)"
     :widget-id widget-id
     :description "A simple Bar Chart built using [Oz](https://github.com/metasoarous/oz)"
     :data chart/sample-data
     :component chart/component
     :component-id (str widget-id "/bar-chart")
     :source-code chart/source-code]))

