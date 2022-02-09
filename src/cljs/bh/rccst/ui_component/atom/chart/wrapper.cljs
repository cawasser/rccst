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

(defn chart [component]
  [rc/v-box :src (rc/at)
   :gap "2px"
   :children [[rc/h-box :src (rc/at)
               :justify :end
               :children [[rc/md-icon-button
                           :md-icon-name "zmdi-settings"
                           :tooltip "configure this chart"
                           :on-click #(log/info "open config panel")]]]
              component]])

;; endregion
