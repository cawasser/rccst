(ns bh.rccst.views.molecule.general
  "Catalog and demonstrations of general components."
  (:require [taoensso.timbre :as log]
            [bh.rccst.views.molecule.example.header-bar :as header-bar]
            [bh.rccst.views.molecule.example.login :as login]
            [bh.rccst.views.molecule.example.two-d-three-d-globe :as two-d-three-d-globe]))


(defn catalog
  []

  [:div
   ;[header-bar/example]
   ;[login/example]
   [two-d-three-d-globe/example]])
