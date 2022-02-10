(ns bh.rccst.ui-component.atom.chart.wrapper
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]
            [reagent.core :as r]
            [woolybear.ad.layout :as layout]
            [bh.rccst.ui-component.utils :as ui-utils]
            [bh.rccst.ui-component.atom.chart.utils :as utils]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; CHART WRAPPERS
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

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
  [& {:keys [data id config-panel component]}]
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
                                     (utils/tabular-data-panel data)
                                     [config-panel data id]]]]
                                  [])
                                [component data id])]]]])))


(defn chart [component]
  [rc/v-box :src (rc/at)
   :gap "2px"
   :children [[rc/h-box :src (rc/at)
               :justify :end
               :children [[rc/md-icon-button
                           :md-icon-name "zmdi-settings"
                           :tooltip "configure this chart"
                           :on-click #(log/info "toggle config panel")]]]
              component]])

;; endregion
