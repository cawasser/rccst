(ns rccst.views.molecule.general
  "Catalog and demonstrations of general components."
  (:require [taoensso.timbre :as log]
            [rccst.views.molecule.example.header-bar :as header-bar]
            [rccst.views.molecule.example.login :as login]
            [rccst.views.molecule.example.two-d-three-d-globe :as two-d-three-d-globe]
            [rccst.views.molecule.example.composite.coverage-plan :as coverage-plan]
            [rccst.views.molecule.example.composite.chart-remote-data :as chart-remote-data]
            [rccst.views.molecule.example.composite.simple-multi-chart :as simple-multi-chart]
            [rccst.views.molecule.example.composite.simple-multi-chart-2 :as simple-multi-chart-2]
            [rccst.views.molecule.example.composite.with-fn-example :as with-fn]
            [rccst.views.molecule.example.composite.multi-chart-widget :as multi-chart-widget]))


(log/info "rccst.views.molecule.general")


(defn catalog []

  [:div
   ;[chart-remote-data/example]
   ;[simple-multi-chart/example]
   ;[simple-multi-chart-2/example]
   ;[multi-chart-widget/example]
   ;[with-fn/example]
   ;[coverage-plan/ww-example]
   ;[coverage-plan/r-example]
   [header-bar/example]])
   ;[login/example]
   ;[two-d-three-d-globe/example]])
