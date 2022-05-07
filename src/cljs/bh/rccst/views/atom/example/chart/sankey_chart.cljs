(ns bh.rccst.views.atom.example.chart.sankey-chart
  (:require [bh.rccst.ui-component.atom.chart.sankey-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [taoensso.timbre :as log]))


(defn example []
  (let [container-id "sankey-chart-demo"]
    [example/component-example
     :title "Sankey Chart"
     :container-id container-id
     :description "A simple Sankey Chart built using [Recharts](https://recharts.org/en-US/api/Sankey)

> Note: the API page for Sankey is woefully incomplete, it does NOT explain how to build the
\"custom\" node or link components used in the example. You need to look at the [demo source](https://github.com/recharts/recharts/blob/master/demo/component/Sankey.tsx)
***AND*** the source for the [custom node](https://github.com/recharts/recharts/blob/master/demo/component/DemoSankeyNode.tsx) to understand how this all works."

     :data chart/sample-data
     :component chart/configurable-component
     :component-id (utils/path->keyword container-id "sankey-chart")
     :source-code chart/source-code]))

