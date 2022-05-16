(ns bh.rccst.views.organisms
  (:require [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            [bh.rccst.views.organism.ui-grid :as ui-grid]))


(log/info "bh.rccst.views.organisms")


(defn page
  "Atomic Design 'Organisms' page"
  []

  [layout/page {:extra-classes :is-fluid}
   [ui-grid/page]])


