(ns bh.rccst.ui-component.molecule.example
  (:require [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.views.atom.utils :as bcu]
            [taoensso.timbre :as log]
            [woolybear.ad.catalog.utils :as acu]
            [woolybear.ad.layout :as layout]))


(defn component-example [& {:keys [title
                                   description
                                   data
                                   container-id
                                   component
                                   component-id
                                   source-code
                                   extra-classes
                                   extra-params]}]

  ;(log/info "component-example" title  "///" widget-id
  ;  "///" component-id "///" component)

  (ui-utils/init-container component-id)

  (acu/demo
    title
    description
    [layout/centered (or extra-classes {})
     [:div {:style {:width "1000px" :height "700px"}}
      (apply into
        [component :data data :component-id component-id :container-id container-id]
        extra-params)]]
    source-code))


(defn example [& {:keys [title container-id description
                         data config
                         data-panel config-panel component-panel
                         source-code]}]

  ;(log/info "example widget" title config)

  (ui-utils/init-widget container-id config)

  (let [config-key (keyword container-id "config")
        data-key (keyword container-id "data")
        tab-panel (keyword container-id "tab-panel")
        selected-tab (keyword container-id "tab-panel.value")]
    (bcu/configurable-demo
      title
      description
      [config-key data-key tab-panel selected-tab]
      [data-panel data]
      [config-panel data container-id]
      [:div {:style {:width "1500px" :height "700px"}}
       [component-panel data container-id]]
      source-code)))



(comment
  (def extra-params {:node-types "dummy"})
  (def params {})

  (let [{:keys [extra-params]} params]
    (apply into
      ["component" :data "data" :component-id "component-id" :container-id "container-id"]
      extra-params))

  ())