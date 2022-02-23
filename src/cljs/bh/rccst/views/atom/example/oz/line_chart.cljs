(ns bh.rccst.views.atom.example.oz.line-chart
  (:require [bh.rccst.ui-component.atom.oz.line-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [taoensso.timbre :as log]))


(defn example []
  (let [widget-id "oz-line-chart-demo"]
    [example/component-example
     :title "Line Chart (oz)"
     :widget-id widget-id
     :description "A simple Line Chart built using [Oz](https://github.com/metasoarous/oz)"
     :data chart/sample-data
     :component chart/component
     :component-id (str widget-id "/line-chart")
     :source-code chart/source-code]))