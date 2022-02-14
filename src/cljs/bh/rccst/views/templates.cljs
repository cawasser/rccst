(ns bh.rccst.views.templates
  (:require [bh.rccst.events :as events]

            [taoensso.timbre :as log]

            [bh.rccst.ui-component.tabbed-panel :as tabbed-panel]))


(def navbar [[:templates/all "All" [:div "all Templates"]]])


(defn page
  "Atomic Design 'Templates' page"
  []

  [tabbed-panel/tabbed-panel
   :title "'Templates' Catalog"
   :short-name "templates"
   :description "Based upon [_Atomic Design_](https://bradfrost.com/blog/post/atomic-web-design/) by Brad Frost"
   :children navbar
   :start-panel :templates/all])
