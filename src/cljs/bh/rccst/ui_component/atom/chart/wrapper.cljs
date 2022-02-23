(ns bh.rccst.ui-component.atom.chart.wrapper
  (:require [bh.rccst.ui-component.utils :as ui-utils]
            [re-com.core :as rc]
            [reagent.core :as r]
            [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]))


(defn configurable-chart
  "takes a component and wraps it with a 'settings' button which can show/hide the
  appropriate data and config panels.

  ---

  - data : (atom) the data to be shown
  - config : (hash-map) the data structure providing the local state for the component and the config-panel
  - id : (string) unique identifier for this component instance
  - config-panel : (hiccup) the configuration 'molecule' for the component
  - component : (hiccup) the component itself

  Returns : (hiccup) the Reagent component representing the entire 'package' (component + config-panel + button)
  "
  [& {:keys [data id config-panel data-panel component]}]
  (let [open? (r/atom false)
        config-key (keyword id "config")
        data-key (keyword id "data")
        tab-panel (keyword id "tab-panel")
        selected-tab (keyword id "tab-panel.value")
        chart-events [config-key data-key tab-panel selected-tab]]

    (fn [& {:keys [data id config-panel component]}]
      ;(log/info "configurable-chart-2" id config-panel component)
      [rc/v-box :src (rc/at)
       :gap "2px"
       :children [[rc/h-box :src (rc/at)
                   :justify :end
                   :children [[rc/md-icon-button
                               :md-icon-name "zmdi-settings"
                               :tooltip "configure this chart"
                               :on-click #(do
                                            (log/info "toggle" @open?)
                                            (swap! open? not)
                                            (log/info "now" @open?))]]]

                  [layout/centered {:extra-classes :is-one-third}
                   [rc/h-box :src (rc/at)
                    :gap "5px"
                    :children (conj
                                (if @open?
                                  [[layout/centered {:extra-classes :is-one-third}
                                    [ui-utils/chart-config
                                     chart-events
                                     [data-panel data]
                                     [config-panel data id]]]]
                                  [])
                                [component data id])]]]])))


(defn chart [& {:keys [data id component]}]
  [component data id])


(defn base-chart [& {:keys [data config
                            component-id container-id
                            data-panel config-panel component-panel]}]

  ;(log/info "base-chart"
  ;  data component-id container-id
  ;  data-panel config-panel component-panel)

  (let [id (r/atom nil)
        not-configurable? (nil? config-panel)]

    (fn []
      (when (nil? @id)
        (reset! id component-id)
        (ui-utils/init-widget @id config)
        (ui-utils/dispatch-local @id [:container] container-id))

      (if not-configurable?
        [chart
         :data data
         :id @id
         :component component-panel]

        [configurable-chart
         :data data
         :id @id
         :data-panel data-panel
         :config-panel config-panel
         :component component-panel]))))
