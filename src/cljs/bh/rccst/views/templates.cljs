(ns bh.rccst.views.templates
  (:require [bh.rccst.events :as events]
            [taoensso.timbre :as log]
            [woolybear.ad.layout :as layout]
            [bh.rccst.ui-component.tabbed-panel :as tabbed-panel]))


(log/info "bh.rccst.views.templates")


(def navbar [[:templates/all "All" [:div "all Templates"]]])


(defn page
  "Atomic Design 'Templates' page"
  []

  [layout/page {:extra-classes :is-fluid}

   ;[layout/page-header {:extra-classes :is-fluid}
   ; [:h1.has-text-info "'Templates'"]]

   [tabbed-panel/tabbed-panel
    :extra-classes {:extra-classes :is-fluid
                    :height "85vh"}
    :title ""
    :short-name "templates"
    :description ""
    :children navbar
    :start-panel :templates/all]])
