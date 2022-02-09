(ns bh.rccst.views.catalog.example.funnel-chart
  (:require [reagent.core :as r]
            [reagent.ratom :refer-macros [reaction]]
            [taoensso.timbre :as log]
            [bh.rccst.ui-component.atom.funnel-chart :as chart]
            [bh.rccst.ui-component.molecule.example :as example]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))


(defn example []
      (let [widget-id "funnel-chart-demo"]
           [example/example
            :title "Funnel Chart"
            :widget-id widget-id
            :description "A simple Funnel Chart built using [Recharts]()"
            :data (r/atom utils/paired-data)
            :config (chart/config widget-id)
            :data-panel utils/tabular-data-panel
            :config-panel chart/config-panel
            :component-panel chart/component
            :source-code "temp"]))


;(defn example []
;      (utils/init-config-panel "funnel-chart-demo")
;
;      (let [data (r/atom utils/paired-data)]
;           (bcu/configurable-demo
;             "Funnel Chart"
;             "Funnel Chart with different colors for each chunk.  This requires embedding `Cell` elements inside the `Funnel` element"
;             [:funnel-chart-demo/config :funnel-chart-demo/data :funnel-chart-demo/tab-panel :funnel-chart-demo/value]
;             [utils/tabular-data-panel data]
;             [config-panel data config]
;             [component data config]
;             '[:> FunnelChart {:height 400 :width 500}
;               (utils/non-gridded-chart-components config)
;               [:> Funnel {:dataKey :value :data @data :isAnimationActive @isAnimationActive?}
;                (doall
;                  (map-indexed (fn [idx {name :name}]
;                                   [:> Cell {:key (str "cell-" idx) :fill (get-in @config [:colors name])}])
;                               @data))
;                [:> LabelList {:position :right :fill "#000000" :stroke "none" :dataKey :name}]]])))