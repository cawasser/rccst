(ns bh.rccst.views.atom.example.chart.bar-chart.data-structure-example
  (:require [bh.rccst.ui-component.atom.chart.bar-chart-2 :as chart]
            [bh.rccst.ui-component.atom.chart.utils :as chart-utils]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.utils :as utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]))


(log/info "bh.rccst.views.atom.example.chart.bar-chart.data-structure-example")


(defn example []
  (let [container-id "bar-chart-2-data-structure-demo"
        component-id (utils/path->keyword container-id "bar-chart-2")]
    [example/component-example
     :title "Bar Chart 2 (Live Data - structure)"
     :description "A Bar Chart (2) built using [Recharts](https://recharts.org/en-US/api/BarChart). This example shows how
   charts can take [ratoms](http://reagent-project.github.io/docs/master/reagent.ratom.html) as input and re-render as the data changes.

   > In _this_ case, we are using a plain data structure for the data, so there is no way to update it (it lives
   > only inside the chart, with no way to get at it from outside)."
     :data chart/sample-data
     :component chart/component
     :container-id container-id
     :component-id component-id
     :extra-params {:data-panel chart-utils/meta-tabular-data-panel
                    :config-panel chart/config-panel}
     :source-code chart/source-code]))
