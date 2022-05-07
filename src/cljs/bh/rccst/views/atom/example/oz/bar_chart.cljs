(ns bh.rccst.views.atom.example.oz.bar-chart
  (:require [bh.rccst.ui-component.atom.oz.bar-chart :as chart]
            [bh.rccst.ui-component.utils :as utils]
            [bh.rccst.ui-component.molecule.example :as example]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "oz-bar-chart-demo"]
    [example/component-example
     :title "Bar Chart (oz)"
     :container-id container-id
     :description "A simple Bar Chart built using [Oz](https://github.com/metasoarous/oz)"
     :data chart/sample-data
     :component chart/component
     :component-id (utils/path->keyword container-id "bar-chart")
     :source-code chart/source-code]))

