(ns rccst.views.organisms
  (:require [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            [rccst.views.organism.ui-grid :as ui-grid]))


(log/info "rccst.views.organisms")


(defn page
  "Atomic Design 'Organisms' page"
  []

  [layout/page {:extra-classes :is-fluid}
   [ui-grid/page]])


