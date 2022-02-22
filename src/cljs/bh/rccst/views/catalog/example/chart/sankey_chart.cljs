(ns bh.rccst.views.catalog.example.chart.sankey-chart
  (:require [taoensso.timbre :as log]

            [bh.rccst.ui-component.atom.chart.sankey-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]))


(defn example []
  (let [widget-id "sankey-chart-demo"]
    [example/component-example
     :title "Sankey Chart"
     :widget-id widget-id
     :description "A simple Sankey Chart built using [Recharts](https://recharts.org/en-US/api/Sankey)

> Note: the API page for Sankey is woefully incomplete, it does NOT explain how to build the
\"custom\" node or link components used in the example. You need to look at the [demo source](https://github.com/recharts/recharts/blob/master/demo/component/Sankey.tsx)
***AND*** the source for the [custom node](https://github.com/recharts/recharts/blob/master/demo/component/DemoSankeyNode.tsx) to understand how this all works."

     :data chart/sample-data
     :component chart/component
     :component-id (str widget-id "/sankey-chart")
     :source-code chart/source-code]))

