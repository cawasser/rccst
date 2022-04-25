(ns bh.rccst.views.molecule.general
  "Catalog and demonstrations of general components."
  (:require [taoensso.timbre :as log]
            [bh.rccst.views.molecule.example.header-bar :as header-bar]
            [bh.rccst.views.molecule.example.login :as login]
            [bh.rccst.views.molecule.example.two-d-three-d-globe :as two-d-three-d-globe]
            [bh.rccst.views.molecule.example.composite.coverage-plan :as coverage-plan]
            [bh.rccst.views.molecule.example.composite.chart-remote-data :as chart-remote-data]
            [bh.rccst.views.molecule.example.composite.multi-chart-widget :as multi-chart-widget]))



(defn catalog
  []

  [:div
   [chart-remote-data/example]
   ;[coverage-plan/ww-example]
   ;[coverage-plan/r-example]
   ;[header-bar/example]
   ;[login/example]
   ;[two-d-three-d-globe/example]
   [multi-chart-widget/example]])
