(ns bh.rccst.ui-component.molecule.example
  (:require [taoensso.timbre :as log]
            [bh.rccst.views.catalog.utils :as bcu]
            ;[bh.rccst.views.catalog.example.chart.utils :as utils]

            [bh.rccst.ui-component.utils :as ui-utils]))
            ;[reagent.core :as r]))



(defn example [& {:keys [title widget-id description
                         data config
                         data-panel config-panel component-panel
                         source-code]}]

  (log/info "example widget" title config)

  (ui-utils/init-widget widget-id config)

  (let [config-key (keyword widget-id "config")
        data-key (keyword widget-id "data")
        tab-panel (keyword widget-id "tab-panel")
        selected-tab (keyword widget-id "tab-panel.value")]
    (bcu/configurable-demo
      title
      description
      [config-key data-key tab-panel selected-tab]
      [data-panel data]
      [config-panel data widget-id]
      [component-panel data widget-id]
      source-code)))