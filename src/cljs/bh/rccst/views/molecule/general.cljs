(ns bh.rccst.views.molecule.general
  "Catalog and demonstrations of general components."
  (:require [taoensso.timbre :as log]
            [bh.rccst.views.molecule.example.header-bar :as header-bar]
            [bh.rccst.views.molecule.example.login :as login]
            [bh.rccst.views.molecule.example.two-d-three-d-globe :as two-d-three-d-globe]
            [bh.rccst.views.molecule.example.composite.multi-chart :as multi-chart]
            [bh.rccst.views.molecule.example.composite.coverage-plan :as coverage-plan]
            [bh.rccst.views.molecule.example.composite.grid-widget :as grid-widget]))


(defn catalog
  []

  [:div
   [coverage-plan/example]
   [grid-widget/example]
   [header-bar/example]
   [login/example]
   [two-d-three-d-globe/example]
   [multi-chart/example]])
