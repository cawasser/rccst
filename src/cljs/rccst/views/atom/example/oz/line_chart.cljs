(ns rccst.views.atom.example.oz.line-chart
  (:require [bh.ui-component.atom.oz.line-chart :as chart]
            [bh.ui-component.molecule.example :as example]
            [bh.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "oz-line-chart-demo"]
    [example/component-example
     :title "Line Chart (oz)"
     :container-id container-id
     :description "A simple Line Chart built using [Oz](https://github.com/metasoarous/oz)"
     :data chart/sample-data
     :component chart/component
     :component-id (utils/path->keyword container-id "line-chart")
     :source-code chart/source-code]))
