(ns bh.rccst.ui-component.molecule.example
  (:require [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.views.atom.utils :as bcu]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn component-example [& {:keys [title
                                   description
                                   data
                                   widget-id
                                   component
                                   component-id
                                   source-code
                                   extra-classes]}]

  ;(log/info "component-example" title  "///" widget-id
  ;  "///" component-id "///" component)

  (ui-utils/init-container component-id)

  (acu/demo
    title
    description
    [layout/centered (or extra-classes {})
     [component data component-id widget-id]]
    source-code))


(defn example [& {:keys [title widget-id description
                         data config
                         data-panel config-panel component-panel
                         source-code]}]

  ;(log/info "example widget" title config)

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