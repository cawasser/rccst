(ns bh.rccst.ui-component.atom.chart.wrapper
  (:require [taoensso.timbre :as log]
            [re-com.core :as rc]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; CHART
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; region

(defn configurable-chart [open? component]
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
              component]])


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
