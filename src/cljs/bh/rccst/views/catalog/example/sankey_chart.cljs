(ns bh.rccst.views.catalog.example.sankey-chart
  (:require [taoensso.timbre :as log]
            [reagent.core :as r]

            [bh.rccst.ui-component.atom.sankey-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))



(defn example []
  (let [widget-id "sankey-chart-demo"]
    [example/example
     :title "Sankey Chart"
     :widget-id widget-id
     :description "A simple Sankey Chart built using [Recharts](https://recharts.org/en-US/api/Sankey)

> Note: the API page for Sankey is woefully incomplete, it does NOT explain how to build the
\"custom\" node or link components used in the example. You need to look at the [demo source](https://github.com/recharts/recharts/blob/master/demo/component/Sankey.tsx)
***AND*** the source for the [custom node](https://github.com/recharts/recharts/blob/master/demo/component/DemoSankeyNode.tsx) to understand how this all works."

     :data (r/atom utils/dag-data)
     :config (chart/config widget-id)
     :data-panel utils/dag-data-panel
     :config-panel chart/config-panel
     :component-panel chart/component
     :source-code chart/source-code]))

